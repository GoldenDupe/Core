package xyz.goldendupe.datagen.defaults;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.GDSettings;

import java.util.*;

public class SettingsDefault extends GDSettings {
	public SettingsDefault() {
		super(null, null, null, null, null, null);
	}

	@Override
	public GoldenDupe getGoldenDupe() {
		fetchGoldenDupe();
		return super.getGoldenDupe();
	}

	@Override
	public Set<Material> getIllegalDupeCombat() {
		Set<Material> materials = new HashSet<>();
		materials.add(Material.ENDER_PEARL);
		materials.add(Material.TOTEM_OF_UNDYING);
		materials.add(Material.POTION);
		materials.add(Material.SPLASH_POTION);
		materials.add(Material.LINGERING_POTION);
		materials.add(Material.CHORUS_FRUIT);
		materials.add(Material.WIND_CHARGE);
		materials.add(Material.END_CRYSTAL);
		materials.add(Material.RESPAWN_ANCHOR);
		return materials;
	}

	@Override
	public Set<Material> getIllegalDupe() {
		Set<Material> materials = new HashSet<>();
		materials.add(Material.LIGHT);
		materials.add(Material.DRAGON_EGG);
		materials.add(Material.SPAWNER);
		materials.add(Material.TRIAL_SPAWNER);
		materials.add(Material.VAULT);

		materials.add(Material.WITHER_SKELETON_SKULL);
		materials.add(Material.SKELETON_SKULL);

		Registry.MATERIAL.forEach(item->{
			if (item.getKey().getKey().contains("_spawn_egg")) {
				materials.add(item);
			} else if (item.getKey().getKey().contains("_head")) {
				materials.add(item);
			}
		});
		return materials;
	}

	@Override
	public Set<Material> getIllegalPlacements() {
		Set<Material> materials = new HashSet<>();
		materials.add(Material.BEDROCK);
		materials.add(Material.JIGSAW);
		materials.add(Material.BARRIER);
		materials.add(Material.STRUCTURE_VOID);
		materials.add(Material.STRUCTURE_BLOCK);
		materials.add(Material.REINFORCED_DEEPSLATE);
		materials.add(Material.COMMAND_BLOCK);
		materials.add(Material.CHAIN_COMMAND_BLOCK);
		materials.add(Material.REPEATING_COMMAND_BLOCK);
		materials.add(Material.END_PORTAL_FRAME);
		materials.add(Material.LIGHT);
		materials.add(Material.DRAGON_EGG);
		materials.add(Material.TRIAL_SPAWNER);
		materials.add(Material.VAULT);
		return materials;
	}

	@Override
	public Set<Material> getIllegalBlocksMenu() {
		Set<Material> materials = new HashSet<>(getIllegalPlacements());
		materials.add(Material.SPAWNER);
		materials.add(Material.BUDDING_AMETHYST);

		materials.add(Material.SUSPICIOUS_GRAVEL);
		materials.add(Material.SUSPICIOUS_SAND);

		materials.add(Material.DIAMOND_BLOCK);
		materials.add(Material.NETHERITE_BLOCK);
		materials.add(Material.COAL_ORE);
		materials.add(Material.RAW_IRON_BLOCK);
		materials.add(Material.RAW_COPPER_BLOCK);
		materials.add(Material.RAW_GOLD_BLOCK);
		materials.add(Material.IRON_BLOCK);
		materials.add(Material.GOLD_BLOCK);
		materials.add(Material.EMERALD_BLOCK);

		Registry.MATERIAL.forEach(item->{
			if (item.getKey().getKey().contains("_ore")) {
				materials.add(item);
			} else if (item.getKey().getKey().contains("anvil")) {
				materials.add(item);
			} else if (item.getKey().getKey().contains("shulker_box")) {
				materials.add(item);
			} else if (item.getKey().getKey().contains("_spawn_egg")) {
				materials.add(item);
			} else if (item.getKey().getKey().contains("_head")) {
				materials.add(item);
			}

		});
		materials.add(Material.WITHER_SKELETON_SKULL);
		materials.add(Material.SKELETON_SKULL);

		Registry.MATERIAL.forEach(item->{
		});
		materials.add(Material.ANCIENT_DEBRIS);
		materials.add(Material.ENCHANTING_TABLE);
		materials.add(Material.BOOKSHELF);
		materials.add(Material.TURTLE_EGG);
		materials.add(Material.SNIFFER_EGG);

		materials.add(Material.CHEST);
		materials.add(Material.CRAFTING_TABLE);
		materials.add(Material.FURNACE);
		materials.add(Material.JUKEBOX);
		materials.add(Material.SCULK_SHRIEKER);
		materials.add(Material.ENDER_CHEST);

		materials.add(Material.BEACON);
		materials.add(Material.CONDUIT);

		materials.add(Material.TRAPPED_CHEST);
		materials.add(Material.SLIME_BLOCK);
		materials.add(Material.HONEY_BLOCK);
		materials.add(Material.STICKY_PISTON);
		materials.add(Material.PISTON);
		materials.add(Material.REDSTONE_BLOCK);
		materials.add(Material.REDSTONE);
		materials.add(Material.REPEATER);
		materials.add(Material.COMPARATOR);
		materials.add(Material.DROPPER);
		materials.add(Material.DISPENSER);
		materials.add(Material.OBSERVER);
		materials.add(Material.TNT);

		materials.add(Material.NETHER_WART);
		materials.add(Material.BREWING_STAND);
		materials.add(Material.BARREL);
		materials.add(Material.SMOKER);
		materials.add(Material.BLAST_FURNACE);
		materials.add(Material.GRINDSTONE);
		materials.add(Material.SMITHING_TABLE);
		materials.add(Material.STONECUTTER);
		materials.add(Material.BELL);

		materials.add(Material.OBSIDIAN);
		materials.add(Material.CRYING_OBSIDIAN);
		materials.add(Material.RESPAWN_ANCHOR);
		return materials;
	}

	@Override
	public RandomItemsData getRandomItemData() {
		Set<Material> illegals = new HashSet<>(getIllegalDupe());

		return new RandomItemsData(illegals,
				Set.of(Enchantment.MENDING.getKey(), Enchantment.SHARPNESS.getKey(), Enchantment.PROTECTION.getKey(), Enchantment.POWER.getKey()),
				true,
				true,
				true,
				true,
				3,
				true,
				true,
				true,
				true,
				5
		);
	}

	@Override
	public boolean isGlobalChatMute() {
		return false;
	}

	@Override
	public AllowedUsers getGlobalChatMuteAllowedUsers() {
		return AllowedUsers.ALL;
	}

	@Override
	public List<String> getUwuString() {
		List<String> messages = new LinkedList<>();
		messages.add("I weally weally love GowdenDupe~");
		messages.add("I weally weally wuv gowdendewp~");
		messages.add("I'm gay");
		return messages;
	}
}
