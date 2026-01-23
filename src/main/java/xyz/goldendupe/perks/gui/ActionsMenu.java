package xyz.goldendupe.perks.gui;

import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.perks.PerkTranslations;
import xyz.goldendupe.perks.action.Action;
import xyz.goldendupe.perks.action.ActionManager;
import xyz.goldendupe.perks.action.ActionType;
import xyz.goldendupe.perks.action.PerkTier;

import java.text.DecimalFormat;
import java.util.function.Function;

public class ActionsMenu {
    private final DecimalFormat format = new DecimalFormat("#.#");
    private final ActionManager actionManager;

    public ActionsMenu(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public void open(Player player) {

        InventoryGUI inventoryGUI = InventoryGUI
                .builder(ChestRows.SIX)
                .messenger(actionManager.getMessenger())
                .background(Background.noTooltip(Material.BLACK_STAINED_GLASS_PANE))
                .title(PerkTranslations.GUI_PERKS_TITLE)
                .clickable(10, Clickable.builder(getSlot(player, Material.WOODEN_SWORD, ActionType.KILL, 0)).hideItemFlags().title(PerkTranslations.GUI_KILL_PERK_TITLE_1).description(PerkTranslations.GUI_KILL_PERK_DESCRIPTION_1).actionGeneral(context -> openKillPerksMenu(context.getWho(), 0)))
                .clickable(11, Clickable.builder(getSlot(player, Material.WOODEN_SWORD, ActionType.KILL, 1)).hideItemFlags().title(PerkTranslations.GUI_KILL_PERK_TITLE_2).description(PerkTranslations.GUI_KILL_PERK_DESCRIPTION_2).actionGeneral(context -> openKillPerksMenu(context.getWho(), 1)))
                .clickable(12, Clickable.builder(getSlot(player, Material.WOODEN_SWORD, ActionType.KILL, 2)).hideItemFlags().title(PerkTranslations.GUI_KILL_PERK_TITLE_3).description(PerkTranslations.GUI_KILL_PERK_DESCRIPTION_3).actionGeneral(context -> openKillPerksMenu(context.getWho(), 2)))

                .clickable(14, Clickable.builder(Material.BLUE_STAINED_GLASS_PANE).hideItemFlags().title(PerkTranslations.GUI_DAMAGE_PERK_TITLE_1).description(PerkTranslations.GUI_DAMAGE_PERK_DESCRIPTION_1))
                .clickable(15, Clickable.builder(Material.BLUE_STAINED_GLASS_PANE).hideItemFlags().title(PerkTranslations.GUI_DAMAGE_PERK_TITLE_2).description(PerkTranslations.GUI_DAMAGE_PERK_DESCRIPTION_2))
                .clickable(16, Clickable.builder(Material.BLUE_STAINED_GLASS_PANE).hideItemFlags().title(PerkTranslations.GUI_DAMAGE_PERK_TITLE_3).description(PerkTranslations.GUI_DAMAGE_PERK_DESCRIPTION_3))

                .clickable(19, Clickable.builder(getSlot(player, Material.SKELETON_SKULL, ActionType.DEATH, 0)).hideItemFlags().title(PerkTranslations.GUI_DEATH_PERK_TITLE_1).description(PerkTranslations.GUI_DEATH_PERK_DESCRIPTION_1))
                .clickable(20, Clickable.builder(getSlot(player, Material.SKELETON_SKULL, ActionType.DEATH, 1)).hideItemFlags().title(PerkTranslations.GUI_DEATH_PERK_TITLE_2).description(PerkTranslations.GUI_DEATH_PERK_DESCRIPTION_2))
                .clickable(21, Clickable.builder(getSlot(player, Material.SKELETON_SKULL, ActionType.DEATH, 2)).hideItemFlags().title(PerkTranslations.GUI_DEATH_PERK_TITLE_3).description(PerkTranslations.GUI_DEATH_PERK_DESCRIPTION_3))

                .clickable(23, Clickable.builder(Material.BLUE_STAINED_GLASS_PANE).hideItemFlags().title(PerkTranslations.GUI_DAMAGE_PERK_TITLE_4).description(PerkTranslations.GUI_DAMAGE_PERK_DESCRIPTION_4))
                .clickable(24, Clickable.builder(Material.BLUE_STAINED_GLASS_PANE).hideItemFlags().title(PerkTranslations.GUI_DAMAGE_PERK_TITLE_5).description(PerkTranslations.GUI_DAMAGE_PERK_DESCRIPTION_5))
//                .clickable(25, Clickable.builder(Material.BLUE_STAINED_GLASS_PANE).hideItemFlags().title(PerkTranslations.GUI_DAMAGE_PERK_TITLE_6).description(PerkTranslations.GUI_DAMAGE_PERK_DESCRIPTION_6))

                .clickable(37, Clickable.builder(Material.LEATHER_BOOTS).hideItemFlags().title(PerkTranslations.GUI_TRAIL_WALK_TITLE).description(PerkTranslations.GUI_TRAIL_WALK_DESCRIPTION))
                .clickable(38, Clickable.builder(Material.ELYTRA).hideItemFlags().title(PerkTranslations.GUI_TRAIL_ELYTRA_TITLE).description(PerkTranslations.GUI_TRAIL_ELYTRA_DESCRIPTION))
                .clickable(39, Clickable.builder(Material.FEATHER).title(PerkTranslations.GUI_TRAIL_FLIGHT_TITLE).description(PerkTranslations.GUI_TRAIL_FLIGHT_DESCRIPTION))

                .clickable(41, Clickable.builder(Material.BARRIER).title(PerkTranslations.GUI_TEAM_PERK_TITLE_1).description(PerkTranslations.GUI_TEAM_PERK_DESCRIPTION_1))
                .clickable(42, Clickable.builder(Material.BARRIER).title(PerkTranslations.GUI_TEAM_PERK_TITLE_2).description(PerkTranslations.GUI_TEAM_PERK_DESCRIPTION_2))
                .clickable(43, Clickable.builder(Material.BARRIER).title(PerkTranslations.GUI_TEAM_PERK_TITLE_3).description(PerkTranslations.GUI_TEAM_PERK_DESCRIPTION_3))
                .build();
        inventoryGUI.open(player);
    }

    public ItemStack getSlot(Player player, Material defaultItem, ActionType actionType, int slot) {
        Action action = actionManager.getPlayerPerkSlot(player, actionType, slot);
        if (action != null) {
            return action.getIcon();
        } else {
            return ItemStack.of(defaultItem);
        }
    }

    public void openKillPerksMenu(@NotNull Player player, @Range(from = 0, to = 2) int perkSlot) {
        InventoryGUIBuilder inventoryGUI = InventoryGUI
                .builder(ChestRows.FOUR)
                .messenger(actionManager.getMessenger())
                .background(Background.border(ChestRows.FOUR, Material.BLACK_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE))
                .title(PerkTranslations.GUI_PERK_KILLS_TITLE)
                .placeholderGenerator(p -> {
                    PlaceholderList placeholders = new PlaceholderList();
                    placeholders.add("slot", perkSlot + 1);
                    return placeholders;
                });

        int slot = 10;
        for (Action action : actionManager.getKillActions()) {
            int currentTier = actionManager.getPlayerPerkTier(player, action);
            inventoryGUI.addClickable(
                    slot,
                    Clickable.builder(action.getIcon())
                            .hideItemFlags()
                            .title(action.getName())
                            .description(action.getDescription())
                            .actionGeneral(context -> {
                                openPerkSelect(player, perkSlot, action);
                            })
                            .placeholderGenerator(placeholderGen(player, slot, action, currentTier))

            );
            slot++;
        }

        inventoryGUI.build().open(player);
    }

    public void openPerkSelect(Player player, int perkSlot, Action action) {
        int currentTier = actionManager.getPlayerPerkTier(player, action);
        int nextTier = currentTier + 1;
        boolean isMaxTier = actionManager.isMaxTier(action, currentTier);
        double cost;
        if (isMaxTier) {
            cost = 0;
        } else {
            cost = actionManager.getPerkTier(action, nextTier).cost();
        }
        TranslationKey buttonBuyPerk = switch (actionManager.getType(action)) {
            case KILL -> PerkTranslations.GUI_BUY_PERK_KILL_TITLE;
            case DEATH -> null;
            case MOVEMENT -> null;
            case DAMAGE -> null;
            case PASSIVE -> null;
            case TRAIL_WALK -> null;
            case TRAIL_FLIGHT -> null;
            case TRAIL_ELYTRA -> null;
        };
        TranslationKey buttonSelectPerk = switch (actionManager.getType(action)) {
            case KILL -> PerkTranslations.GUI_SELECT_PERK_KILL_TITLE;
            case DEATH -> null;
            case MOVEMENT -> null;
            case DAMAGE -> null;
            case PASSIVE -> null;
            case TRAIL_WALK -> null;
            case TRAIL_FLIGHT -> null;
            case TRAIL_ELYTRA -> null;
        };
        TranslationKey buttonUnselectPerk = switch (actionManager.getType(action)) {
            case KILL -> PerkTranslations.GUI_UNSELECT_PERK_KILL_TITLE;
            case DEATH -> null;
            case MOVEMENT -> null;
            case DAMAGE -> null;
            case PASSIVE -> null;
            case TRAIL_WALK -> null;
            case TRAIL_FLIGHT -> null;
            case TRAIL_ELYTRA -> null;
        };

        int playerPerkTier = actionManager.getPlayerPerkTier(player, action);
        Function<Player, PlaceholderCollection> placeholderGen = placeholderGen(player, perkSlot, action, playerPerkTier);

        InventoryGUIBuilder inventoryGUI = InventoryGUI
                .builder(ChestRows.THREE)
                .messenger(actionManager.getMessenger())
                .background(Background.border(ChestRows.THREE, Material.BLACK_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE))
                .title(action.getName())
                .placeholderGenerator(placeholderGen);

        if (playerPerkTier <= 0) {
            for (int i = 0; i < 7; i++) {
                inventoryGUI.clickable(10 + i, Clickable.builder(Material.ANVIL)
                        .title(PerkTranslations.GUI_BUY_PERK_KILL_TITLE)
                        .description(
                                action.getDescriptionUpgradable()
                        )
                        .placeholderGenerator(placeholderGen)
                        .actionGeneral(context -> {
                            Economy economy = GoldenDupe.instance().vaultEconomy();
                            double balance = economy.getBalance(player);

                            if (balance < cost) {
                                actionManager.getMessenger().message(player, PerkTranslations.MESSAGE_INSUFFICIENT_FUNDS, placeholderGen.apply(player));
                                return;
                            }

                            economy.withdrawPlayer(player, cost);
                            actionManager.setPlayerPerkTier(player, action, nextTier);
                            openPerkSelect(player, perkSlot, action);
                            actionManager.getMessenger().message(player, PerkTranslations.MESSAGE_PURCHASED, placeholderGen.apply(player));
                        }));
            }
        } else {

            boolean isSlotPerk = actionManager.getPlayerPerkSlot(player, actionManager.getType(action),
                    perkSlot) == action;
            ;
            for (int i = 0; i < 3; i++) {
                if (!isSlotPerk) {
                    inventoryGUI.clickable(10 + i, Clickable.builder(Material.GREEN_WOOL)
                            .actionGeneral(context -> {
                                actionManager.setPerkSlot(player, action, perkSlot);
                                open(player);
                            })
                            .title(buttonSelectPerk)
                            .placeholderGenerator(placeholderGen)
                    );
                } else {
                    inventoryGUI.clickable(10 + i, Clickable.builder(Material.RED_WOOL)
                            .actionGeneral(context -> {
                                actionManager.setPerkSlot(player, action, -1);
                                open(player);
                            })
                            .title(buttonUnselectPerk)
                            .placeholderGenerator(placeholderGen)
                    );
                }
            }

            for (int i = 0; i < 3; i++) {
                if (!isMaxTier) {
                    inventoryGUI.clickable(14 + i, Clickable.builder(Material.ANVIL)
                            .title(PerkTranslations.GUI_UPGRADE_PERK_TITLE)
                            .description(
                                    action.getDescriptionUpgradable()
                            )
                            .placeholderGenerator(placeholderGen)
                            .actionGeneral(context -> {
                                actionManager.setPlayerPerkTier(player, action, nextTier);
                                openPerkSelect(player, perkSlot, action);
                            }));
                } else {
                    inventoryGUI.clickable(14 + i, Clickable.builder(Material.BUNDLE)
                            .hideItemFlags()
                            .title(PerkTranslations.GUI_PERK_SELECT_USED_TIER)
                            .description(
                                    action.getDescriptionMax()
                            )
                            .actionGeneral(context -> {
                                openPerksTierSelectMenu(player, perkSlot, action);
                            })
                            .placeholderGenerator(placeholderGen)
                    );
                }
            }
        }
        inventoryGUI.build().open(player);
    }

    public void openPerksTierSelectMenu(@NotNull Player player, int perkSlot, Action action) {
        InventoryGUIBuilder inventoryGUI = InventoryGUI
                .builder(ChestRows.THREE)
                .messenger(actionManager.getMessenger())
                .background(Background.border(ChestRows.THREE, Material.BLACK_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE))
                .title(PerkTranslations.GUI_PERK_SELECT_USED_TIER)
                .placeholderGenerator(p -> {
                    PlaceholderList placeholders = new PlaceholderList();
                    placeholders.add("slot", perkSlot + 1);
                    return placeholders;
                });

        int slot = 10;
        int maxLevel = actionManager.getMaxTier(action);
        int usedTier = actionManager.getPlayerUsedPerkTier(player, action);

        for (int i = 0; i < maxLevel + 1; i++) {
            ItemStack itemStack = null;
            if (usedTier == i) {
                itemStack = action.getIcon().clone();
                itemStack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
            } else {
                itemStack = action.getIcon().clone();
            }
            ClickableBuilder builder = Clickable.builder(itemStack)
                    .title(action.getName())
                    .description(
                            maxLevel == i ?
                                    action.getDescriptionMax() :
                                    action.getDescriptionUpgradable()
                    )
                    .placeholderGenerator(
                            placeholderGen(player, slot, action, i)
                    );

            inventoryGUI.clickable(10 + i, builder);
        }

        inventoryGUI.build().open(player);
    }

    public Function<Player, PlaceholderCollection> placeholderGen(Player player, int slot, Action action, int playerPerkTier) {
        Component title = actionManager.getMessenger().parseComponent(new MessageInfoBuilder(action.getName()).withReceiver(player).build(), ComponentType.CHAT);
        int currentTier = actionManager.getPlayerPerkTier(player, action);
        return p -> {
            PlaceholderList placeholders = new PlaceholderList();
            placeholders.add("slot", slot + 1);
            assert title != null;
            placeholders.add("perk", title);

            PerkTier currentTierPlGen = actionManager.getPerkTier(action, playerPerkTier);
            PerkTier nextTierPlGen = null;
            if (currentTierPlGen != null) {
                nextTierPlGen = actionManager.getPerkTier(action, currentTier + 1);
                placeholders.add("strength", format(currentTierPlGen.strength()));
                placeholders.add("cost", format(currentTierPlGen.cost()));
                placeholders.add("value", currentTierPlGen.value());
                placeholders.add("extravalue", format(currentTierPlGen.extraValue()));
                placeholders.add("length", format(currentTierPlGen.length()));
                placeholders.add("tier", currentTierPlGen.tier());
                placeholders.add("chance", format(currentTierPlGen.chance() * 100));
            } else {
                nextTierPlGen = actionManager.getPerkTier(action, 1);
                placeholders.add("strength", 0);
                placeholders.add("cost", 0);
                placeholders.add("value", "");
                placeholders.add("extravalue", 0);
                placeholders.add("length", 0);
                placeholders.add("tier", 0);
                placeholders.add("chance", 0);
            }
            if (nextTierPlGen != null) {
                placeholders.add("next_strength", format(nextTierPlGen.strength()));
                placeholders.add("next_cost", format(nextTierPlGen.cost()));
                placeholders.add("next_value", nextTierPlGen.value());
                placeholders.add("next_extravalue", format(nextTierPlGen.extraValue()));
                placeholders.add("next_length", format(nextTierPlGen.length()));
                placeholders.add("next_tier", nextTierPlGen.tier());
                placeholders.add("next_chance", format(nextTierPlGen.chance() * 100));
            }
            return placeholders;
        };
    }

    ;

    private String format(double d) {
        return format.format(d);
    }
}
