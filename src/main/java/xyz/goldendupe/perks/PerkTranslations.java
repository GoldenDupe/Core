package xyz.goldendupe.perks;

import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.translation.Translation;

import static bet.astral.messenger.v2.translation.Translation.text;

public class PerkTranslations {
	public static final Translation GUI_PERKS_TITLE = new Translation("gui.perks.title").add(ComponentType.CHAT, text("<!italic>Perks"));
	public static final Translation GUI_PERK_KILLS_TITLE = new Translation("gui.kill-perks.title").add(ComponentType.CHAT, text("<!italic>Kills (%slot%)"));

	/*
	 * KILL PERKS
	 */

	public static final Translation GUI_KILL_PERK_TITLE_1 = new Translation("gui.perks.kill.1.name").add(ComponentType.CHAT, text("<!italic><yellow>Kill Perk 1"));
	public static final Translation GUI_KILL_PERK_DESCRIPTION_1 = new Translation("gui.perks.kill.1.description").add(ComponentType.CHAT,
			text("<!italic><gray>No kill perk selected").appendNewline()
	);
	public static final Translation GUI_KILL_PERK_TITLE_2 = new Translation("gui.perks.kill.2.name").add(ComponentType.CHAT, text("<!italic><yellow>Kill Perk 2"));
	public static final Translation GUI_KILL_PERK_DESCRIPTION_2 = new Translation("gui.perks.kill.2.description").add(ComponentType.CHAT,
			text("<!italic><gray>No kill perk selected").appendNewline()
	);
	public static final Translation GUI_KILL_PERK_TITLE_3 = new Translation("gui.perks.kill.3.name").add(ComponentType.CHAT, text("<!italic><yellow>Kill Perk 3"));
	public static final Translation GUI_KILL_PERK_DESCRIPTION_3 = new Translation("gui.perks.kill.3.description").add(ComponentType.CHAT,
			text("<!italic><gray>No kill perk selected").appendNewline()
	);
	public static final Translation GUI_DAMAGE_PERK_TITLE_1 = new Translation("gui.perks.damage-dealt.1.name").add(ComponentType.CHAT, text("<!italic><red>Damage Perk 4"));
	public static final Translation GUI_DAMAGE_PERK_DESCRIPTION_1 = new Translation("gui.perks.damage-dealt.1.description").add(ComponentType.CHAT,
			text("<!italic><gray>No damage perk selected").appendNewline()
	);
	public static final Translation GUI_DAMAGE_PERK_TITLE_2 = new Translation("gui.perks.damage-dealt.2.name").add(ComponentType.CHAT, text("<!italic><red>Damage Perk 4"));
	public static final Translation GUI_DAMAGE_PERK_DESCRIPTION_2 = new Translation("gui.perks.damage-dealt.2.description").add(ComponentType.CHAT,
			text("<!italic><gray>No damage perk selected").appendNewline()
	);
	public static final Translation GUI_DAMAGE_PERK_TITLE_3 = new Translation("gui.perks.damage-dealt.3.name").add(ComponentType.CHAT, text("<!italic><red>Damage Perk 5"));
	public static final Translation GUI_DAMAGE_PERK_DESCRIPTION_3 = new Translation("gui.perks.damage-dealt.3.description").add(ComponentType.CHAT,
			text("<!italic><gray>No damage perk selected").appendNewline()
	);
	public static final Translation GUI_DAMAGE_PERK_TITLE_4 = new Translation("gui.perks.damage-dealt.4.name").add(ComponentType.CHAT, text("<!italic><red>Damage Perk 5"));
	public static final Translation GUI_DAMAGE_PERK_DESCRIPTION_4 = new Translation("gui.perks.damage-dealt.4.description").add(ComponentType.CHAT,
			text("<!italic><gray>No damage perk selected").appendNewline()
	);
	public static final Translation GUI_DAMAGE_PERK_TITLE_5 = new Translation("gui.perks.damage-dealt.5.name").add(ComponentType.CHAT, text("<!italic><red>Damage Perk 6"));
	public static final Translation GUI_DAMAGE_PERK_DESCRIPTION_5 = new Translation("gui.perks.damage-dealt.5.description").add(ComponentType.CHAT,
			text("<!italic><gray>No damage perk selected").appendNewline()
	);
	public static final Translation GUI_DAMAGE_PERK_TITLE_6 = new Translation("gui.perks.damage-dealt.6.name").add(ComponentType.CHAT, text("<!italic><red>Damage Perk 6"));
	public static final Translation GUI_DAMAGE_PERK_DESCRIPTION_6 = new Translation("gui.perks.damage-dealt.6.description").add(ComponentType.CHAT,
			text("<!italic><gray>No damage perk selected").appendNewline()
	);
	/*
	 * DEATH PERKS
	 */

	public static final Translation GUI_DEATH_PERK_TITLE_1 = new Translation("gui.perks.death.1.name").add(ComponentType.CHAT, text("<!italic><dark_red>Death Perk 1"));
	public static final Translation GUI_DEATH_PERK_DESCRIPTION_1 = new Translation("gui.perks.death.1.description").add(ComponentType.CHAT,
			text("<!italic><gray>No death perk selected").appendNewline()
	);
	public static final Translation GUI_DEATH_PERK_TITLE_2 = new Translation("gui.perks.death.2.name").add(ComponentType.CHAT, text("<!italic><dark_red>Death Perk 2"));
	public static final Translation GUI_DEATH_PERK_DESCRIPTION_2 = new Translation("gui.perks.death.2.description").add(ComponentType.CHAT,
			text("<!italic><gray>No death perk selected").appendNewline()
	);
	public static final Translation GUI_DEATH_PERK_TITLE_3 = new Translation("gui.perks.death.3.name").add(ComponentType.CHAT, text("<!italic><dark_red>Death Perk 3"));
	public static final Translation GUI_DEATH_PERK_DESCRIPTION_3 = new Translation("gui.perks.death.3.description").add(ComponentType.CHAT,
			text("<!italic><gray>No death perk selected").appendNewline()
	);

	/*
	 * TRAILS
	 */

	public static final Translation GUI_TRAIL_WALK_TITLE = new Translation("gui.perks.trail.walk.name").add(ComponentType.CHAT, text("<!italic><blue>Movement Trail"));
	public static final Translation GUI_TRAIL_WALK_DESCRIPTION = new Translation("gui.perks.trail.walk.description").add(ComponentType.CHAT,
			text("<!italic><gray>No movement trail selected")
	);
	public static final Translation GUI_TRAIL_ELYTRA_TITLE = new Translation("gui.perks.trail.elytra.name").add(ComponentType.CHAT, text("<!italic><blue>Elytra Trail"));
	public static final Translation GUI_TRAIL_ELYTRA_DESCRIPTION = new Translation("gui.perks.trail.elytra.description").add(ComponentType.CHAT,
			text("<!italic><gray>No elytra trail selected")
	);

	public static final Translation GUI_TRAIL_FLIGHT_TITLE = new Translation("gui.perks.trail.flight.name").add(ComponentType.CHAT, text("<!italic><blue>Flight Particle"));
	public static final Translation GUI_TRAIL_FLIGHT_DESCRIPTION = new Translation("gui.perks.trail.flight.description").add(ComponentType.CHAT,
			text("<!italic><gray>No flight trail selected")
	);

	/*
	 *  TEAM PERKS
	 */

	public static final Translation GUI_TEAM_PERK_TITLE_1 = new Translation("gui.perks.team.1.name").add(ComponentType.CHAT, text("<!italic>Team Perk 1"));
	public static final Translation GUI_TEAM_PERK_DESCRIPTION_1 = new Translation("gui.perks.team.1.description").add(ComponentType.CHAT,
			text("<!italic><gray>No team perk selected\n<red>Coming soon..!")
	);
	public static final Translation GUI_TEAM_PERK_TITLE_2 = new Translation("gui.perks.team.2.name").add(ComponentType.CHAT, text("<!italic>Team Perk 2"));
	public static final Translation GUI_TEAM_PERK_DESCRIPTION_2 = new Translation("gui.perks.team.2.description").add(ComponentType.CHAT,
			text("<!italic><gray>No team perk selected\n<red>Coming soon..!")
	);
	public static final Translation GUI_TEAM_PERK_TITLE_3 = new Translation("gui.perks.team.3.name").add(ComponentType.CHAT, text("<!italic>Team Perk 3"));
	public static final Translation GUI_TEAM_PERK_DESCRIPTION_3 = new Translation("gui.perks.team.3.description").add(ComponentType.CHAT,
			text("<!italic><gray>No team perk selected\n<red>Coming soon..!")
	);

	/*
	 * Kill perk menu
	 */

	public static final Translation GUI_BUY_PERK_KILL_TITLE = new Translation("gui.button.buy-perk.title")
			.add(ComponentType.CHAT, text("<!italic><aqua>Buy perk"));
	public static final Translation GUI_UPGRADE_PERK_TITLE = new Translation("gui.button.upgrade-perk.title")
			.add(ComponentType.CHAT, text("<!italic><green>Level Up Perk"));
	public static final Translation GUI_PERK_SELECT_USED_TIER = new Translation("gui.button.change-perk-tier.title")
			.add(ComponentType.CHAT, text("<!italic><yellow>Change used perk tier"));
	public static final Translation GUI_SELECT_PERK_KILL_TITLE = new Translation("gui.button.select-perk.kill.title")
			.add(ComponentType.CHAT, text("<!italic><green>Select for kill perk %slot%"));
	public static final Translation GUI_UNSELECT_PERK_KILL_TITLE = new Translation("gui.button.unselect-perk.kill.title")
			.add(ComponentType.CHAT, text("<!italic><red>Unselect for kill perk %slot%"));

	/*
	 * Messages
	 */

	public static final Translation MESSAGE_INSUFFICIENT_FUNDS = new  Translation("message.perks.insufficient-funds")
			.add(ComponentType.CHAT, text("<red>Insufficient Funds! You do not have enough money!"));
	public static final Translation MESSAGE_PURCHASED = new  Translation("message.perks.purchased")
			.add(ComponentType.CHAT, text("<green>Successfully purchased! You are now proud owner of <white>%perk%<red>"));

	/*
	 * PERKS
	 */
	public static final Translation PERK_KILL_STRENGTH_TITLE = new Translation("perk.kill.strength.name").add(ComponentType.CHAT, text("<!italic>Wrath"));
	public static final Translation PERK_KILL_STRENGTH_DESCRIPTION = new Translation("perk.kill.strength.description").add(ComponentType.CHAT, text("<!i><gray>Every kill has a chance to grant <white>Strength<gray>."));
	public static final Translation PERK_KILL_STRENGTH_UPGRADABLE_DESCRIPTION = new Translation("perk.kill.strength.upgradable.description").add(ComponentType.CHAT, text("<!i><gray>Every kill has a chance to grant <white>Strength<gray>.\n\nCurrent Level:\n<dark_gray> - <white>%tier%\n<dark_gray> - <gray>Chance: <white>%chance%\n<dark_gray> - <gray>Strength: <white>%strength%\n<yellow>Next Level\n<dark_gray> - <white>%next_tier%\n<dark_gray> - <gray>Chance: <white>%next_chance%\n<dark_gray> - <gray>Strength: <white>%next_strength%\n<dark_gray> - <gray>Cost: <white>$%next_cost%"));
	public static final Translation PERK_KILL_STRENGTH_MAX_DESCRIPTION = new Translation("perk.kill.strength.max.description").add(ComponentType.CHAT, text("<!i><gray>Every kill has a chance to grant <white>Strength<gray>.\n\nCurrent Level:\n<dark_gray> - <white>%tier%\n<dark_gray> - <gray>Chance: <white>%chance%\n<dark_gray> - <gray>Strength: <white>%strength%"));
	public static final Translation PERK_KILL_SPEED_TITLE = new Translation("perk.kill.speed.name").add(ComponentType.CHAT, text("<!italic>Adrenaline Rush"));
	public static final Translation PERK_KILL_SPEED_DESCRIPTION = new Translation("perk.kill.speed.description").add(ComponentType.CHAT, text("<!i><gray>Every kill has a chance to grant <white>Speed<gray>."));
	public static final Translation PERK_KILL_SPEED_UPGRADABLE_DESCRIPTION = new Translation("perk.kill.speed.upgradable.description").add(ComponentType.CHAT, text("<!i><gray>Every kill has a chance to grant <white>Speed<gray>.\n\nCurrent Level:\n<dark_gray> - <white>%tier%\n<dark_gray> - <gray>Chance: <white>%chance%\n<dark_gray> - <gray>Speed: <white>%strength%\n<yellow>Next Level\n<dark_gray> - <white>%next_tier%\n<dark_gray> - <gray>Chance: <white>%next_chance%\n<dark_gray> - <gray>Speed: <white>%next_strength%\n<dark_gray> - <gray>Cost: <white>$%next_cost%"));
	public static final Translation PERK_KILL_SPEED_MAX_DESCRIPTION = new Translation("perk.kill.speed.max.description").add(ComponentType.CHAT, text("<!i><gray>Every kill has a chance to grant <white>Speed<gray>.\n\nCurrent Level:\n<dark_gray> - <white>%tier%\n<dark_gray> - <gray>Chance: <white>%chance%\n<dark_gray> - <gray>Speed: <white>%strength%"));
	public static final Translation PERK_KILL_HEAD_COLLECTOR_TITLE = new Translation("perk.kill.head-collector.name").add(ComponentType.CHAT, text("<!italic>Headsman"));
	public static final Translation PERK_KILL_HEAD_COLLECTOR_DESCRIPTION = new Translation("perk.kill.head-collector.description").add(ComponentType.CHAT, text("<!i><gray>Drops the head of the killed player."));
	public static final Translation PERK_KILL_HEAD_COLLECTOR_UPGRADABLE_DESCRIPTION = new Translation("perk.kill.head-collector.upgradable.description").add(ComponentType.CHAT, text("<!i><gray>Drops the head of the killed player.\n\nCurrent Level:\n<dark_gray> - <white>%tier%\n<dark_gray> - <gray>Head Drops: <red>No\n<yellow>Next Level\n<dark_gray> - <gray>Head Drops: <green>Yes"));
	public static final Translation PERK_KILL_HEAD_COLLECTOR_MAX_DESCRIPTION = new Translation("perk.kill.head-collector.max.description").add(ComponentType.CHAT, text("<!i><gray>Drops the head of the killed player.\n\nCurrent Level:\n<dark_gray> - <white>%tier%\n<dark_gray> - <gray>Head Drops: <green>Yes"));
	public static final Translation PERK_KILL_RANDOM_ITEMS_TITLE = new Translation("perk.kill.random-kill-items.name").add(ComponentType.CHAT, text("<!italic>Scavenger"));
	public static final Translation PERK_KILL_RANDOM_ITEMS_DESCRIPTION = new Translation("perk.kill.random-kill-items.description").add(ComponentType.CHAT, text("<!i><gray>Every kill has a chance to drop <white>random items<gray>."));
	public static final Translation PERK_KILL_RANDOM_ITEMS_UPGRADABLE_DESCRIPTION = new Translation("perk.kill.random-kill-items.upgradable.description").add(ComponentType.CHAT, text("<!i><gray>Every kill has a chance to drop <white>random items<gray>.\n\nCurrent Level:\n<dark_gray> - <white>%tier%\n<dark_gray> - <gray>Chance: <white>%chance%\n<dark_gray> - <gray>Items: <white>%strength%-%extravalue%\n<yellow>Next Level\n<dark_gray> - <white>%next_tier%\n<dark_gray> - <gray>Chance: <white>%next_chance%\n<dark_gray> - <gray>Items: <white>%next_strength%-%next_extravalue%\n<dark_gray> - <gray>Cost: <white>$%next_cost%"));
	public static final Translation PERK_KILL_RANDOM_ITEMS_MAX_DESCRIPTION = new Translation("perk.kill.random-kill-items.max.description").add(ComponentType.CHAT, text("<!i><gray>Every kill has a chance to drop <white>random items<gray>.\n\nCurrent Level:\n<dark_gray> - <white>%tier%\n<dark_gray> - <gray>Chance: <white>%chance%\n<dark_gray> - <gray>Items: <white>%strength%-%extravalue%"));
}
