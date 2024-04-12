package xyz.goldendupe.command.defaults;

import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.messenger.GoldenPlaceholderManager;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.Ping;

import java.util.LinkedList;
import java.util.List;

@Cloud
public class PingCommand extends GDCloudCommand {
	private final Command.Builder<Player> pingBuilder;
	private PingCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		pingBuilder = commandManager.commandBuilder("ping", Description.of("Shows the ping of a player."), "ms")
				.permission(MemberType.DEFAULT.cloudOf("ping"))
				.senderType(Player.class)
				.handler(context-> {
							Player sender = context.sender();
							List<Placeholder> placeholders = new LinkedList<>(goldenDupe.messenger().getPlaceholderManager().playerPlaceholders("player.", sender));
							int ping = sender.getPing();
							Component coloredPing = Ping.defaultPing.format(ping);

							placeholders.add(new Placeholder("ping", ping));
							placeholders.add(new Placeholder("ping_colored", coloredPing));

							commandMessenger.message(sender, "ping.message-self", true, placeholders);
						}
				);
		commandManager.command(pingBuilder);
		commandManager.command(pingBuilder
				.argument(PlayerParser.playerComponent().name("player"))
				.commandDescription(Description.of("Allows checking ping of other players."))
				.handler(context-> {
							Player argument = context.get("player");
							Player sender = context.sender();
							List<Placeholder> placeholders = new LinkedList<>(goldenDupe.messenger().getPlaceholderManager().playerPlaceholders("player", argument));
							int ping = argument.getPing();
							Component coloredPing = Ping.defaultPing.format(ping);

							placeholders.add(new Placeholder("ping", ping));
							placeholders.add(new Placeholder("ping_colored", coloredPing));

							if (sender.equals(argument)) {
								if (context.flags().isPresent("format")) {
									commandMessenger.message(sender, "ping.message-unformat-self", true, placeholders);
								} else {
									commandMessenger.message(sender, "ping.message-self", true, placeholders);
								}
							} else {
								if (context.flags().isPresent("format")) {
									commandMessenger.message(sender, "ping.message-unformat-other", true, placeholders);
								} else {
									commandMessenger.message(sender, "ping.message-other", true, placeholders);
								}
							}
						}
				)
		);
	}
}
