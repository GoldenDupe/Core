package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.command.CloudPPCommand;
import xyz.goldendupe.command.cloud.GDCloudCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Cloud
public class DupableCommand extends GDCloudCommand {

	private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();
	private static final Component LINE = MiniMessage.miniMessage().deserialize("<red>This item is undupable!");

	public DupableCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"dupable",
								Description.of("Makes your held item dupable/undupable."),
								"makedupable"
						)
						.senderType(Player.class)
						.permission("goldendupe.admin.dupable")
						.handler(context -> {
							Player sender = context.sender();

							ItemStack itemStack = sender.getInventory().getItemInMainHand();
							if (itemStack.isEmpty()){
								commandMessenger.message(sender, "dupable.message-air");
								return;
							}

							ItemMeta meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() :
									Bukkit.getItemFactory().getItemMeta(itemStack.getType());

							PersistentDataContainer container = meta.getPersistentDataContainer();

							boolean isDupable = !container.has(goldenDupe.KEY_UNDUPABLE);

							if (!isDupable)
								container.remove(goldenDupe.KEY_UNDUPABLE);

							else
								container.set(goldenDupe.KEY_UNDUPABLE, PersistentDataType.BOOLEAN, true);

							List<Component> lore = meta.lore();
							if (lore != null) {
								lore = new LinkedList<>();
								for (Component component : Objects.requireNonNull(meta.lore())) {
									String line = PLAIN.serialize(component);
									if (line.equalsIgnoreCase("This item is undupable!")) {
										continue;
									}

									lore.add(!isDupable ? component : LINE);
								}
							}

							meta.lore(lore);

							itemStack.setItemMeta(meta);
							commandMessenger.message(sender, "dupable.message-success");
						})
		);
	}

}
