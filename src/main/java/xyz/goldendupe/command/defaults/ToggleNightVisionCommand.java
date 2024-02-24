package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class ToggleNightVisionCommand extends GDCloudCommand {
	public ToggleNightVisionCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"togglenightvision",
								Description.of("Allows a player to toggle night vision.")
						)
						.permission(MemberType.DEFAULT.cloudOf("toggle-night-vision"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
							boolean toggle = player.isToggleNightVision();
							player.setToggleNightVision(!toggle);

							if (!toggle) {
								commandMessenger.message(sender, "toggle-night-vision.message-enabled");
							} else {
								commandMessenger.message(sender, "toggle-night-vision.message-disabled");
							}
						})
		);
	}
}
