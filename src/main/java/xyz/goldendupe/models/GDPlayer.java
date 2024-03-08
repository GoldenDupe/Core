package xyz.goldendupe.models;

import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.chatcolor.GDChatColor;
import xyz.goldendupe.utils.Position;
import xyz.goldendupe.utils.annotations.RequiresOnlinePlayer;
import xyz.goldendupe.utils.annotations.temporal.RequireSave;
import xyz.goldendupe.utils.flaggable.Flag;
import xyz.goldendupe.utils.flaggable.FlagImpl;
import xyz.goldendupe.utils.flaggable.Flaggable;
import xyz.goldendupe.utils.impl.SpawnPosition;
import xyz.goldendupe.utils.reference.FactionPlayerReference;

import java.util.*;

@SuppressWarnings({"UnusedReturnValue", "unused"})
@RequireSave
public class GDPlayer implements Flaggable, FactionPlayerReference {
	@NotNull private final GoldenDupe goldenDupe;
	@NotNull private final UUID uniqueId;
	private SpawnPosition teleportingSpawn;
	private GDChat chat;
	@RequireSave
	private boolean autoConfirmClearInv;
	@RequireSave
	private GDChatColor color = GDChatColor.DEFAULT;
	@RequireSave
	private boolean vanished;
	@RequireSave
	private boolean isToggled = true;
	@RequireSave
	private boolean isToggleDropItem = false;
	@RequireSave
	private boolean isTogglePickupItem = false;
	@RequireSave
	private boolean isToggleNightVision = true;
	@RequireSave
	private boolean isTogglePotionBottles = false;
	@RequireSave
	private boolean isToggleSpeed = false;
	@RequireSave
	@Getter(AccessLevel.PUBLIC) private final Map<String, Position> homes = new HashMap<>();
	@Getter
	private final Map<UUID, GDMessageGroup> messagegroups = new HashMap<>();
	@NotNull
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();

	public GDPlayer(@NotNull GoldenDupe goldenDupe, @NotNull Player player){
		this.goldenDupe = goldenDupe;
		this.uniqueId = player.getUniqueId();
		this.chat = GDChat.GLOBAL;
		this.teleportingSpawn = null;
		this.autoConfirmClearInv = false;
	}

	public boolean isToggled() {
		return isToggled;
	}

	public GDPlayer setToggled(boolean toggled) {
		isToggled = toggled;
		return this;
	}


	public SpawnPosition teleportingSpawn() {
		return teleportingSpawn;
	}

	public GDPlayer setTeleportingSpawn(SpawnPosition teleportingSpawn) {
		this.teleportingSpawn = teleportingSpawn;
		return this;
	}

	public GDChat chat() {
		return chat;
	}

	public GDPlayer setChat(GDChat chat) {
		this.chat = chat;
		return this;
	}

	public boolean autoConfirmClearInv() {
		return autoConfirmClearInv;
	}

	public GDPlayer setAutoConfirmClearInv(boolean autoConfirmClearInv) {
		this.autoConfirmClearInv = autoConfirmClearInv;
		return this;
	}

	public GDChatColor color() {
		return color;
	}

	public GDPlayer setColor(GDChatColor color) {
		this.color = color;
		return this;
	}

	public boolean vanished() {
		return vanished;
	}

	public GDPlayer setVanished(boolean vanished) {
		this.vanished = vanished;
		return this;
	}

	public boolean isToggleDropItem() {
		return isToggleDropItem;
	}

	public GDPlayer setToggleDropItem(boolean toggleDropItem) {
		isToggleDropItem = toggleDropItem;
		return this;
	}

	public boolean isTogglePickupItem() {
		return isTogglePickupItem;
	}

	public GDPlayer setTogglePickupItem(boolean togglePickupItem) {
		isTogglePickupItem = togglePickupItem;
		return this;
	}

	public boolean isToggleNightVision() {
		return isToggleNightVision;
	}

	public GDPlayer setToggleNightVision(boolean toggleNightVision) {
		isToggleNightVision = toggleNightVision;
		return this;
	}

	public boolean isTogglePotionBottles() {
		return isTogglePotionBottles;
	}

	public GDPlayer setTogglePotionBottles(boolean togglePotionBottles) {
		isTogglePotionBottles = togglePotionBottles;
		return this;
	}

	public boolean isToggleSpeed() {
		return isToggleSpeed;
	}

	public GDPlayer setToggleSpeed(boolean toggleSpeed) {
		isToggleSpeed = toggleSpeed;
		return this;
	}

	public int getMaxHomes() {
		//noinspection DataFlowIssue
		return LuckPermsProvider.get()
				.getUserManager()
				.getUser(uniqueId)
				.getCachedData()
				.getMetaData()
				.getMetaValue("homes", Integer::parseInt)
				.orElse(3);
	}

	@Override
	public <V> void addFlag(@NotNull Flag<V> flag) {
		flags.put(flag.getKey(), flag);
	}

	@Override
	public <V> void editFlag(@NotNull NamespacedKey key, @Nullable V newValue) throws IllegalStateException {
		if (flags.get(key) != null){
			//noinspection unchecked
			Flag<V> flag = (Flag<V>) flags.get(key);
			assert newValue != null;
			flag.setValue(newValue);
			return;
		}
		throw new IllegalStateException("Couldn't edit a flag which is not set!");
	}

	@Override
	public <V> void setIfAbsent(@NotNull Flag<V> flag) {
		flags.putIfAbsent(flag.getKey(), flag);
	}

	@Override
	public <V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue) {
		flags.putIfAbsent(key, new FlagImpl<>(key, defaultValue, defaultValue));
	}

	@Override
	public <V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue, @Nullable V currentValue) {
		flags.putIfAbsent(key, new FlagImpl<>(key, defaultValue, currentValue));
	}

	@Override
	public @NotNull <V> Flag<V> getFlag(@NotNull NamespacedKey key, @NotNull Flag<V> defaultFlag) {
		return getFlag(key) != null ? Objects.requireNonNull(getFlag(key)) : defaultFlag;
	}

	@Override
	public @Nullable <V> Flag<V> getFlag(@NotNull NamespacedKey key) {
		//noinspection unchecked
		return (Flag<V>) flags.get(key);
	}
	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}

	@Override
	@RequiresOnlinePlayer
	public @NotNull FPlayer factionPlayer() {
		return goldenDupe.getFactions().getPlayerManager().convert(player());
	}

	@Nullable
	@RequiresOnlinePlayer
	@Override
	public java.util.UUID factionId() {
		return factionPlayer().getFactionId();
	}

	@Override
	@RequiresOnlinePlayer
	public @Nullable Faction faction() {
		return goldenDupe.getFactions().getFactionManager().get(factionPlayer().getFactionId());
	}
}
