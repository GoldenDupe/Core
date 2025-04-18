package xyz.goldendupe.models;

import com.google.common.collect.ImmutableList;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.DecoratedPot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionType;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.utils.MemberType;

import java.io.File;
import java.util.*;

@Getter
public class GDSettings {
	private GoldenDupe goldenDupe;
	private final Map<UUID, GDMessageGroup> messageGroups = new HashMap<>();
	private Set<Material> illegalDupeCombat;
	private Set<Material> illegalDupe;
	private Set<Material> illegalPlacements;
	private Set<Material> illegalBlocksMenu;
	private RandomItemsData randomItemData;
	@Setter
	@Deprecated(forRemoval = true)
	private long timesDuped;
	@Setter
	@Deprecated(forRemoval = true)
	private long itemsDuped;
	@Setter
	@Deprecated(forRemoval = true)
	private long randomItemsGenerated;
	@Setter
	private boolean globalChatMute = false;
	@Setter
	private AllowedUsers globalChatMuteAllowedUsers = AllowedUsers.ALL;
	private List<String> uwuString;

	public GDSettings(Set<Material> illegalDupeCombat, Set<Material> illegalDupe, Set<Material> illegalPlacements, Set<Material> illegalBlocksMenu, RandomItemsData randomItemData, List<String> uwuString) {
		this.illegalDupeCombat = illegalDupeCombat;
		this.illegalDupe = illegalDupe;
		this.illegalPlacements = illegalPlacements;
		this.illegalBlocksMenu = illegalBlocksMenu;
		this.randomItemData = randomItemData;
		this.uwuString = uwuString;
	}

	public GoldenDupe getGoldenDupe(){
		fetchGoldenDupe();
		return goldenDupe;
	}

	public void fetchGoldenDupe(){
		if (goldenDupe==null){
			goldenDupe = GoldenDupe.instance();
		}
	}


	public void reload(){
		fetchGoldenDupe();
		reloadIllegals();

	}

	/*
	 * ILLEGALS
	 */

	@Deprecated(forRemoval = true)
	public void reloadIllegals(){
		fetchGoldenDupe();
		File file = new File(goldenDupe.getDataFolder(), "illegals.yml");
		YamlConfiguration illegalConfig = YamlConfiguration.loadConfiguration(file);

		illegalDupe = new HashSet<>();
		illegalDupeCombat = new HashSet<>();


		addMaterials(illegalDupe, illegalConfig.getStringList("dupe.illegals"));
		illegalDupe.add(Material.AIR);
		addMaterials(illegalDupeCombat, illegalConfig.getStringList("dupe.combat"));
		addMaterials(illegalPlacements, illegalConfig.getStringList("placement"));
	}

	private void addMaterials(Collection<Material> materials, List<String> mats){
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

	public ItemStack patchRandomItem(ItemStack itemStack, Random random) {
		fetchGoldenDupe();
		ItemMeta meta = itemStack.getItemMeta();
		if (randomItemData == null){
			// Might be overridden so, trying to fetch random item data is a good idea.
			randomItemData = getRandomItemData();
		}
		if (randomItemData.allowUpdatedGoatHorns && meta instanceof MusicInstrumentMeta instrumentMeta){
			List<MusicInstrument> instruments = Registry.INSTRUMENT.stream().toList();
			if (random.nextDouble()>0.1){
				instrumentMeta.setInstrument(instruments.get(random.nextInt(0, instruments.size()-1)));
			}
		}
		if (randomItemData.allowUpdatedDecoratedPots && meta instanceof BlockStateMeta blockStateMeta && blockStateMeta.getBlockState() instanceof DecoratedPot decoratedPot){
			List<Material> materials = Tag.ITEMS_DECORATED_POT_INGREDIENTS.getValues().stream().toList();
			for (DecoratedPot.Side side : DecoratedPot.Side.values()) {
				if (random.nextDouble() > 0.45) {
					Material material = materials.get(random.nextInt(0, materials.size()));
					decoratedPot.setSherd(side, material);
				}
			}
			blockStateMeta.setBlockState(decoratedPot);
		}
		if (randomItemData.allowUpdatedEnchantmentBooks && meta instanceof EnchantmentStorageMeta enchantmentStorageMeta){
			if (random.nextDouble()>0.20){
				List<Enchantment> enchantments = new ArrayList<>(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
						.stream().filter(enchantment -> !randomItemData.illegalEnchants.contains(enchantment.getKey())).toList());
				if (randomItemData.allowOnlyVanillaEnchantedBooks){
					enchantments.removeIf(enchant->!enchant.getKey().getNamespace().equalsIgnoreCase("minecraft"));
				}
				Enchantment enchantment = enchantments.get(random.nextInt(0, enchantments.size()-1));
				if (enchantment.getMaxLevel() == 1){
					enchantmentStorageMeta.addStoredEnchant(enchantment, 1, false);
				} else {
					enchantmentStorageMeta.addStoredEnchant(enchantment, random.nextInt(1, enchantment.getMaxLevel()), false);
				}
			}
		}
		if (randomItemData.allowUpdatedFireworks && meta instanceof FireworkMeta fireworkMeta){
			int power = random.nextInt(0, randomItemData.maxFireworkBoost);
			if (power > 0){
				fireworkMeta.setPower(power);
			}
		} if (randomItemData.allowUpdatedArrows && meta instanceof PotionMeta potionMeta && itemStack.getType()==Material.TIPPED_ARROW){
			List<PotionType> types = new ArrayList<>(Registry.POTION.stream().toList());
			types.removeAll(List.of(PotionType.MUNDANE, PotionType.LUCK, PotionType.THICK, PotionType.WATER));
			PotionType type = types.get(random.nextInt(0, types.size()-1));
			potionMeta.setBasePotionType(type);
			potionMeta.setColor(Registry.POTION_EFFECT_TYPE.get(type.getKey()).getColor());
		} else if (randomItemData.allowUpdatedPotions && meta instanceof PotionMeta potionMeta) {
			List<PotionType> types = new ArrayList<>(Registry.POTION.stream().toList());
			types.removeAll(List.of(PotionType.MUNDANE, PotionType.LUCK, PotionType.THICK, PotionType.WATER));
			PotionType type = types.get(random.nextInt(0, types.size()-1));
			potionMeta.setBasePotionType(type);
			potionMeta.setColor(Registry.POTION_EFFECT_TYPE.get(type.getKey()).getColor());
		} else if (randomItemData.allowUpdateOminousBottles && meta instanceof OminousBottleMeta ominousBottleMeta){
			int tier = random.nextInt(1, randomItemData.getMaxOminousTier());
			ominousBottleMeta.setAmplifier(tier);
		}

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	@Getter
	public enum AllowedUsers {
		ALL("", MemberType.MODERATOR.permissionOf("mute-chat")),
		DONATOR(MemberType.DONATOR.permissionOf("mute-chat-bypass"), MemberType.ADMINISTRATOR.permissionOf("mute-chat.donator")),
		STAFF(MemberType.MODERATOR.permissionOf("mute-chat-bypass"), MemberType.MODERATOR.permissionOf("mute-chat.staff")),
		ADMIN(MemberType.ADMINISTRATOR.permissionOf("mute-chat-bypass"), MemberType.ADMINISTRATOR.permissionOf("mute-chat")),
		OWNER(MemberType.OWNER.permissionOf("mute-chat-bypass"), MemberType.OWNER.permissionOf("mute-chat")),
		;

		private final String bypass;
		private final String command;
		AllowedUsers(String bypass, String command) {
			this.bypass = bypass;
			this.command = command;
		}
	}

	@Getter
	public static class RandomItemsData {
		private final List<ItemStack> allowedItems;
		private final Set<Material> illegalsItems;
		private final Set<NamespacedKey> illegalEnchants;
		private final boolean allowUpdatedDecoratedPots;
		private final boolean allowUpdatedEnchantmentBooks;
		private final boolean allowOnlyVanillaEnchantedBooks;
		private final boolean allowUpdatedFireworks;
		private final int maxFireworkBoost;
		private final boolean allowUpdatedGoatHorns;
		private final boolean allowUpdatedArrows;
		private final boolean allowUpdatedPotions;
		private final boolean allowUpdateOminousBottles;
		private final int maxOminousTier;

		public RandomItemsData(Set<Material> illegals, Set<NamespacedKey> illegalEnchants, boolean allowUpdatedSherds, boolean allowUpdatedBooks, boolean allowOnlyVanillaEnchants, boolean allowUpdatedFireworks, int maxFireworkBoost, boolean allowUpdatedGoatHorns, boolean allowUpdatedArrows, boolean allowUpdatedPotions, boolean allowUpdateOminousBottles, int maxOminousTier) {
			this.illegalsItems = illegals;
			this.illegalEnchants = illegalEnchants;
			this.allowUpdatedDecoratedPots = allowUpdatedSherds;
			this.allowUpdatedEnchantmentBooks = allowUpdatedBooks;
			this.allowOnlyVanillaEnchantedBooks = allowOnlyVanillaEnchants;
			this.allowUpdatedFireworks = allowUpdatedFireworks;
			this.maxFireworkBoost = maxFireworkBoost;
			this.allowUpdatedGoatHorns = allowUpdatedGoatHorns;
			this.allowUpdatedArrows = allowUpdatedArrows;
			this.allowUpdatedPotions = allowUpdatedPotions;
			this.allowUpdateOminousBottles = allowUpdateOminousBottles;
			this.maxOminousTier = maxOminousTier;


			Registry<Material> materials = Registry.MATERIAL;
			World world = Bukkit.getWorlds().getFirst();
			@SuppressWarnings("removal")
            List<ItemStack> items = materials.stream().filter(material-> !illegalsItems.contains(material)).filter(material->material.isEnabledByFeature(world)).filter(Material::isItem).map(ItemStack::new).toList();
			allowedItems = ImmutableList.copyOf(items);
		}

		public List<ItemStack> getAllowedItems() {
			return ImmutableList.copyOf(allowedItems);
		}
	}
}
