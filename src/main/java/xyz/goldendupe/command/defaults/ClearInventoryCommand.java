package xyz.goldendupe.command.defaults;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.utils.ChestRows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.flag.CommandFlag;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.bootstrap.InitAfterBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.RunSync;

import java.util.List;

@Cloud
public class ClearInventoryCommand extends GDCloudCommand implements InitAfterBootstrap {

	public static InventoryGUI clearMenu;

	public void init() {

		List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);

		ItemStack itemStackConf = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		itemStackConf.editMeta(meta -> {
			meta.displayName(Component.text("Confirm", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
			meta.lore(List.of(Component.text("Click to clear inventory.", NamedTextColor.RED)
					.appendSpace().append(Component.text("(No going back!)", NamedTextColor.DARK_RED))
					.decoration(TextDecoration.ITALIC, false)));
		});

		ItemStack itemStackDeny = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		itemStackDeny.editMeta(meta -> {
			meta.displayName(Component.text("Cancel", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
			meta.lore(List.of(Component.text("Click to cancel the clearing of inventory.", NamedTextColor.RED)
					.decoration(TextDecoration.ITALIC, false)));
		});

		Clickable conf = Clickable.builder(itemStackConf).actionGeneral((action) -> {
			Player player = action.getWho();
					player.getInventory().clear();
					player.getInventory().setHelmet(null);
					player.getInventory().setChestplate(null);
					player.getInventory().setLeggings(null);
					player.getInventory().setBoots(null);
					player.getInventory().setItemInOffHand(null);
					GoldenDupe.instance().messenger().message(player, Translations.COMMAND_CLEAR_INVENTORY_CLEARED);
					RunSync.runSync(player::closeInventory); // Run on bukkit thread to remove async catcher exception
				}).build();

		Clickable deny = Clickable.builder(itemStackDeny).actionGeneral((action) -> {
			Player player = action.getWho();
			player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
			GoldenDupe.getPlugin(GoldenDupe.class).messenger().message(player, Translations.COMMAND_CLEAR_INVENTORY_CANCEL);
		}).build();

		InventoryGUIBuilder builder = InventoryGUI.builder(ChestRows.THREE).title(Component.text("Clear Inventory Confirmation"));
		List<Integer> slotsConf = List.of(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21);
		List<Integer> slotsDeny = List.of(5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26);

		for (Integer i : slotsConf) {
			builder.addClickable(i, conf);
		}

		for (Integer i : slotsDeny) {
			builder.addClickable(i, deny);
		}

		builder.messenger(goldenDupe().messenger());
		clearMenu = builder.build();
	}

	public ClearInventoryCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"clear",
								Description.of("Clears the player's inventory."),
								"clearinv", "clearinventory", "inventoryclear", "invclear"
						)
						.permission(MemberType.DEFAULT.permissionOf("trash"))
						.senderType(Player.class)
						.flag(CommandFlag.builder("change-auto-confirm"))
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);
							if (context.flags().isPresent("change-auto-confirm")) {
								if (player.isToggleAutoConfirmClearInventory()) {
									messenger.message(sender, Translations.COMMAND_CLEAR_INVENTORY_TOGGLE_TRUE);
								} else {
									messenger.message(sender, Translations.COMMAND_CLEAR_INVENTORY_TOGGLE_FALSE);
								}
								player.setToggleAutoConfirmClearInventory(!player.isToggleAutoConfirmClearInventory());
							} else {
								if (player.isToggleAutoConfirmClearInventory()) {
									sender.getInventory().clear();
									sender.getInventory().setHelmet(null);
									sender.getInventory().setChestplate(null);
									sender.getInventory().setLeggings(null);
									sender.getInventory().setBoots(null);
									sender.getInventory().setItemInOffHand(null);
									messenger.message(sender, Translations.COMMAND_CLEAR_INVENTORY_CLEARED);
									return;
								}

								clearMenu.open(sender);
							}
						})
		);
	}
}
