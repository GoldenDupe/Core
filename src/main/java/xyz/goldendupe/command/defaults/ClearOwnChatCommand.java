package xyz.goldendupe.command.defaults;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class ClearOwnChatCommand extends GDCloudCommand {

	public ClearOwnChatCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"clearmychat",
								Description.of("Clears the player's chat."),
								"bleach"
						)
						.permission(MemberType.DEFAULT.permissionOf("clear-my-chat"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							Component component = Component.empty();
							for (int i = 0; i < 275; i++) {
								sender.sendMessage(component);
							}
							commandMessenger.message(sender, Translations.COMMAND_CLEAR_MY_CHAT);
						})
		);
	}
}