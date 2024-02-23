package xyz.goldendupe;

import bet.astral.guiman.InventoryListener;
import bet.astral.messenger.placeholder.Placeholder;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.command.CommandFinder;
import xyz.goldendupe.command.internal.Permission;
import xyz.goldendupe.command.internal.Permissions;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.legacy.*;
import xyz.goldendupe.database.astronauts.ReportDatabase;
import xyz.goldendupe.database.astronauts.ReportUserDatabase;
import xyz.goldendupe.messenger.GoldenMessenger;
import com.samjakob.spigui.SpiGUI;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.DecoratedPot;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.goldendupe.listeners.GDListener;
import xyz.goldendupe.models.GDSpawn;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static xyz.goldendupe.utils.Resource.loadResourceAsTemp;
import static xyz.goldendupe.utils.Resource.loadResourceToFile;

public final class GoldenDupe extends JavaPlugin {
    public static final long FIRST_RELEASED = 1591254000L;
    public static final long NEW_RELEASE = 1591254000L;
    public static int season = 1;
    public static final long SEASON_1 = 1591254000L;
    private boolean isDebug = false;
    private SpiGUI spiGUI;
    public final NamespacedKey KEY_UNDUPABLE = new NamespacedKey(this, "undupable");
    private final Map<String, GDSpawn> spawns = new HashMap<>();
    private final Set<MessageReload> commands = new HashSet<>();
    private GoldenMessenger defaultMessenger;
    private GoldenMessenger commandMessenger;
    private GoldenMessenger debugMessenger;
    private YamlConfiguration commandConfig;
    private GDCommandInfo defaultCommandInfo;
    private List<Material> illegalDupe;
    private List<ItemStack> randomItems;
    private List<Material> illegalPlacement;
    private YamlConfiguration config;
    private PlayerDatabase playerDatabase;
    private ReportDatabase reportDatabase;
    private ReportUserDatabase reportUserDatabase;
    private Chat vaultChat = null;
    private Economy vaultEconomy = null;
    private LuckPerms luckPerms = null;
    private PaperCommandManager<CommandSender> paperCommandManager;

    @Override
    public void onEnable() {
        uploadUploads();

        spiGUI = new SpiGUI(this);
        spiGUI.setBlockDefaultInteractions(true);
        spiGUI.setDefaultToolbarBuilder(null);
        spiGUI.setEnableAutomaticPagination(false);

        if (getServer().getPluginManager().getPlugin("Vault") != null){

            RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
            if (chatProvider != null){
                vaultChat = chatProvider.getProvider();
            }
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null){
                vaultEconomy = economyProvider.getProvider();
            }
        }

        if (getServer().getPluginManager().getPlugin("LuckPerms") != null){
            luckPerms = LuckPermsProvider.get();
        }


        // config.yml
        reloadConfig();
        // illegals.yml
        reloadIllegals();
        // commands.yml
        getLogger().info("Loading commands..!");
        loadCommands();
        getLogger().info("Loaded commands!");
        // listeners (Reflected)
        getLogger().info("Loading event listeners..!");
        loadListeners();
        getLogger().info("Loaded event listeners!");
        registerListener(new InventoryListener());

        // Messengers
        reloadMessengers();

        playerDatabase = new PlayerDatabase(this);
        reportUserDatabase = new ReportUserDatabase(this);
        reportDatabase = new ReportDatabase(this);


        getComponentLogger().info("GoldenDupe has enabled!");
    }

    @Override
    public void onDisable() {


        getComponentLogger().info("GoldenDupe has disabled!");
    }


    public void reloadMessengers(){
        defaultMessenger = loadMessenger(false, "messages.yml");
        commandMessenger = loadMessenger(false, "commands.yml");
        debugMessenger = loadMessenger(true, "messages.yml");
        for (MessageReload reload : commands){
            reload.reloadMessengers();
        }
    }
    private GoldenMessenger loadMessenger(boolean debug, String name){
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(getDataFolder(), name));
        GoldenMessenger goldenMessenger = new GoldenMessenger(configuration, new HashMap<>(), debug);
        getLogger().info("Loading placeholders for messages...");
        Map<String, Placeholder> placeholderMap = goldenMessenger.loadPlaceholders("placeholders");
        getLogger().info("Loaded placeholders for messages...");
        // Debug
        if (placeholderMap == null){
            placeholderMap = new HashMap<>();
        } else {
            placeholderMap = new HashMap<>(placeholderMap);
        }
        getLogger().info("Overriding message placeholders...");
        goldenMessenger.overrideDefaultPlaceholders(placeholderMap);
        getLogger().info("Overrode message placeholders...");
        return goldenMessenger;
    }


    public void reloadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        setIfNotSet(config, "season", 1, Integer.class);

        season = config.getInt("season");
        isDebug = config.getBoolean("debug");
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        return config;
    }

    public void setIfNotSet(Configuration configuration, String key, Object defaultValue, Class<?>... allowedTypes){
        if (configuration.get(key) != null){
            for (Class<?> clazz : allowedTypes){
                if (clazz.isInstance(configuration.get(key))){
                    return;
                }
            }
        }
        configuration.set(key, defaultValue);
    }

    private void loadListeners(){
        Class<?> command = GDListener.class;
        String pkg = command.getPackage().getName();
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo().acceptPackages(pkg).scan()) {
            ClassInfoList classInfo = scanResult.getClassesImplementing(Listener.class);
            List<Class<?>> classes = classInfo.loadClasses();
            for (Class<?> clazz : classes) {
                registerListener(clazz);
            }
        }
    }

    public void reloadIllegals() {
        File file = new File(getDataFolder(), "illegals.yml");
        YamlConfiguration illegalConfig = YamlConfiguration.loadConfiguration(file);

        illegalDupe = new LinkedList<>();
        randomItems = new LinkedList<>();
        illegalPlacement = new LinkedList<>();


        addMaterials(illegalDupe, illegalConfig.getStringList("placement"));
        illegalDupe.add(Material.AIR);
        addMaterials(illegalPlacement, illegalConfig.getStringList("dupe"));

        List<String> randomIllegals = illegalConfig.getStringList("random.illegals");
        for (Material material : Material.values()){
            if (randomIllegals.contains(material.name())){
                continue;
            }
            randomItems.add(new ItemStack(material));
        }
        if (illegalConfig.getBoolean("random.all-goat-horns", false)){
            for (MusicInstrument instrument : Registry.INSTRUMENT){
                ItemStack itemStack = new ItemStack(Material.GOAT_HORN);
                MusicInstrumentMeta meta = (MusicInstrumentMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.GOAT_HORN));
                meta.setInstrument(instrument);
                itemStack.setItemMeta(meta);
                randomItems.add(itemStack);
            }
        }
        if (illegalConfig.getBoolean("random.full-decorated-potteries", false)){
            for (Material material : Tag.ITEMS_BREAKS_DECORATED_POTS.getValues()){
                ItemStack itemStack = new ItemStack(Material.DECORATED_POT);
                BlockStateMeta meta = (BlockStateMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.DECORATED_POT));
                DecoratedPot decoratedPot = (DecoratedPot) meta.getBlockState();
                decoratedPot.setSherd(DecoratedPot.Side.BACK, material);
                decoratedPot.setSherd(DecoratedPot.Side.FRONT, material);
                decoratedPot.setSherd(DecoratedPot.Side.LEFT, material);
                decoratedPot.setSherd(DecoratedPot.Side.RIGHT, material);
                meta.setBlockState(decoratedPot);
                itemStack.setItemMeta(meta);
                randomItems.add(itemStack);
            }
        }
        if (illegalConfig.getBoolean("random.all-enchanted-books", true)){
            for (Enchantment enchantment : Registry.ENCHANTMENT){
                if (enchantment == Enchantment.MENDING){
                    // FUCK MENDING
                    continue;
                }
                for (int i = 0; i < enchantment.getMaxLevel(); i++){
                    ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.ENCHANTED_BOOK));
                    meta.addStoredEnchant(enchantment, i, false);
                    itemStack.setItemMeta(meta);
                    randomItems.add(itemStack);
                }
            }
        }
        for (int i = 0; i < illegalConfig.getInt("random.random-firework-boost", 0); i++){
            ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
            FireworkMeta meta = (FireworkMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.FIREWORK_ROCKET));
            meta.setPower(i);
            itemStack.setItemMeta(meta);
        }
    }

    public boolean canDupe(ItemStack itemStack){
        if (itemStack.hasItemMeta()){
            ItemMeta meta = itemStack.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (container.has(KEY_UNDUPABLE) && Boolean.TRUE.equals(container.get(KEY_UNDUPABLE, PersistentDataType.BOOLEAN))){
                return false;
            }
        }
        return !(illegalDupe.contains(itemStack.getType()));
    }

    public boolean canBePlaced(Block block) {
        return !illegalPlacement.contains(block.getType());
    }


    private void addMaterials(List<Material> materials, List<String> mats){
        for (String mat : mats){
            try {
                Material material = Material.valueOf(mat);
                if (!materials.contains(material)) {
                    materials.add(material);
                }
            } catch (IllegalStateException ignore){
            }
        }
    }


    public void registerListener(Listener listener){
        if (cannotInject(listener.getClass())){
            getLogger().warning("Couldn't initialize listener: "+ listener.getClass().getName() + " as it's not available in this season!");
            return;
        }

        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerListener(Class<?> listener){
        if (cannotInject(listener)){
            getLogger().warning("Couldn't initialize listener: "+ listener.getName() + " as it's not available in this season or the class is cannot be reflected!");
            return;
        }
        registerPermissions(listener);
        Constructor<?> constructor;
        try {
            constructor = getConstructor(listener, GoldenDupe.class);
            try {
                constructor.setAccessible(true);
                Listener eventListener = (Listener) constructor.newInstance(this);
                getServer().getPluginManager().registerEvents(eventListener, this);
                getLogger().info("Registered listener: "+ listener.getName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to initialize event constructor with params GoldenDupe.class", e);
            }
        } catch (NoSuchMethodException ignore) {
	        try {
                constructor = getConstructor(listener);
                try {
                    constructor.setAccessible(true);
                    Listener eventListener = (Listener) constructor.newInstance();
                    getServer().getPluginManager().registerEvents(eventListener, this);
                    getLogger().info("Registered listener: "+ listener.getName());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Failed to initialize event constructor no params", e);
                }
	        } catch (NoSuchMethodException e) {
		        throw new RuntimeException("Failed to find correct constructor for class: "+ listener.getName(), e);
	        }
        }
    }

    private Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) throws NoSuchMethodException {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException ignore) {
            return clazz.getDeclaredConstructor(params);
        }
    }

    public void registerPermissions(Class<?> clazz){
        if (clazz.isAnnotationPresent(Permissions.class)){
            Permissions permissions = clazz.getAnnotation(Permissions.class);
            if (permissions.value().length==0){
                return;
            }
            for (Permission permission : permissions.value()){
                org.bukkit.permissions.Permission bukkitPermission = new org.bukkit.permissions.Permission(permission.value());
                if (getServer().getPluginManager().getPermission(permission.value())==null) {
                    getServer().getPluginManager().addPermission(bukkitPermission);
                }
            }
        }
    }
    public void registerPermission(String name) {
        org.bukkit.permissions.Permission bukkitPermission = new org.bukkit.permissions.Permission(name);
        if (getServer().getPluginManager().getPermission(name)==null) {
            getServer().getPluginManager().addPermission(bukkitPermission);
        }
    }
    private boolean cannotInject(Class<?> clazz){
        GDCommandInfo.DoNotReflect doNotReflect = clazz.getAnnotation(GDCommandInfo.DoNotReflect.class);
        if (doNotReflect != null){
            return true;
        }

        Season season = clazz   .getAnnotation(Season.class);
        if (season != null){
            // Make season specific classes easier to manage
            // This command is not supposed to be unlocked in this season
            return Arrays.stream(season.unlock()).noneMatch(val -> val == GoldenDupe.season) && !season.alwaysUnlocked();
        }
        return false;
    }

    private void loadCommands(){
        commandConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "commands.yml"));
        defaultCommandInfo = GDCommandInfo.defaultValues(commandConfig);

        paperCommandManager = new PaperCommandManager<>(
                this,
                ExecutionCoordinator.asyncCoordinator(),
                SenderMapper.identity()
        );
        paperCommandManager.registerBrigadier();
        paperCommandManager.registerAsynchronousCompletions();

        String pkg = CommandFinder.class.getPackage().getName();
        String[] subPackages = new String[]{"admin", "defaults", "defaults.spawn", "donator", "staff"};
        for (String subPackage : subPackages){
            String packageName = pkg+"."+subPackage;
            try (ScanResult scanResult = new ClassGraph()
                    .enableAllInfo().acceptPackages(packageName).scan()) {
                ClassInfoList classInfo = scanResult.getClassesImplementing(CommandFinder.class);
                List<String> classes = classInfo.getNames();
                for (String clazzName : classes){
                    Class<?> clazz = Class.forName(clazzName);
                    registerCommand(clazz);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void registerCommand(Class<?> clazz){
        if (cannotInject(clazz)){
            getLogger().info("Reflection based implementation for "+ clazz.getName() + " is not allowed. Skipping!");
            return;
        }
        getLogger().info(clazz.getName());
        if (clazz.isAnnotationPresent(Cloud.class)){
            getLogger().warning("Found a cloud command! " + clazz.getName());
            /*
             * This is the cloud command framework
             */
	        try {
		        Constructor<?> constructor = getConstructor(clazz, GoldenDupe.class, PaperCommandManager.class);
                constructor.setAccessible(true);
                MessageReload reload = (MessageReload) constructor.newInstance(this, paperCommandManager);
                commands.add(reload);

                getLogger().info("Loaded cloud command: " + clazz.getName());

	        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
	                 IllegalAccessException e) {
                e.printStackTrace();
	        }
        } else {

            /*
             * This is the legacy command manager
             */
            Class<GDCommand> gdCommandClass;
            try {
                //noinspection unchecked
                gdCommandClass = (Class<GDCommand>) clazz;
            } catch (ClassCastException e){
                return;
            }
            GDCommandInfo.Command command = clazz.getAnnotation(GDCommandInfo.Command.class);
            MemorySection commandSection = (MemorySection) commandConfig.getConfigurationSection(command.name());

            if (commandSection == null) {
                getLogger().severe("Command will not be initialized! Couldn't find command section for command " + command.name().toUpperCase() + " in the COMMANDS.YML.");
                return;
            }
            GDCommandInfo gdCommandInfo = new GDCommandInfo(defaultCommandInfo, commandSection, command);

            try {
                Constructor<?> goldenCommandConstructor = getConstructor(gdCommandClass, GoldenDupe.class, GDCommandInfo.class);
                goldenCommandConstructor.setAccessible(true);
                GDCommand goldenCommand = (GDCommand) goldenCommandConstructor.newInstance(this, gdCommandInfo);
                CommandMap commandMap = getServer().getCommandMap();
                InternalGDCommand internalGDCommand = new InternalGDCommand(goldenCommand);

                // goldendupe-default
                // goldendupe-donator
                // goldendupe-staff
                // goldendupe-admin
                commandMap.register("goldendupe-" + command.memberType().name().toLowerCase(), internalGDCommand);
                commands.add(goldenCommand);
                getLogger().info("Registered command: goldendupe-" + command.memberType().name().toLowerCase() + ":" + internalGDCommand.getName());
                registerPermissions(clazz);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                getLogger().severe("Command will not be initialized! Couldn't reflect command " + command.name().toUpperCase() + ". Because of this it will not be enabled!");
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }

    private void uploadUploads(){
        String[] files = new String[]{
                "commands|yml",
                "config|yml",
                "illegals|yml",
                "messages|yml",
        };
        for (String name : files){
            name = name.replace("dm/", "discord-messages/");

            String[] split = name.split("\\|");
            String fileName = split[0];
            String ending = split[1];
            File fileTemp = loadResourceAsTemp("/upload/"+fileName, ending);
            File file = loadResourceToFile("/upload/"+fileName, ending, new File(getDataFolder(), fileName+"."+ending), true);
            if (ending.matches("(?i)yml") || ending.matches("(?i)yaml")){
                loadConfig(getConfig(fileTemp), getConfig(file), file);
            }
        }
    }

    private void loadConfig(FileConfiguration tempConfig, FileConfiguration config, File file){
        Set<String> keys = tempConfig.getKeys(false);
        for (String key : keys){
            addDefaults(key, tempConfig, config);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addDefaults(String key, Configuration tempConfig, Configuration config) {
        List<String> comment = tempConfig.getComments(key);
        if (!comment.isEmpty() && config.getInlineComments(key).isEmpty()) {
            config.setComments(key, comment);
        }
        comment = tempConfig.getInlineComments(key);
        if (!comment.isEmpty() && config.getInlineComments(key).isEmpty()) {
            config.setInlineComments(key, comment);
        }
        Object value = tempConfig.get(key); // Retrieve the value from the tempConfig
        if (value instanceof ConfigurationSection section) {
            for (String k : section.getKeys(false)) {
                addDefaults(key + "." + k, tempConfig, config); // Append current key
            }
        }
    }


    private FileConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    public Map<String, GDSpawn> spawns() {
        return spawns;
    }

    public void setDebug(boolean debug){
        this.isDebug = debug;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public GoldenMessenger messenger() {
        return defaultMessenger;
    }

    public GoldenMessenger debugMessenger() {
        return debugMessenger;
    }

    public GoldenMessenger commandMessenger() {
        return commandMessenger;
    }

    public PlayerDatabase playerDatabase() {
        return playerDatabase;
    }

	public void saveSpawns() {
        // TODO
	}

    public Chat vaultChat() {
        return vaultChat;
    }

    public Economy vaultEconomy() {
        return vaultEconomy;
    }

    public SpiGUI spiGUI() {
        return spiGUI;
    }

    public ReportDatabase reportDatabase() {
        return reportDatabase;
    }

    public ReportUserDatabase reportUserDatabase() {
        return reportUserDatabase;
    }

    public LuckPerms luckPerms() {
        return luckPerms;
    }

}
