package xyz.goldendupe.command.donator.basic;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class ToggleBottlesCommand extends GDCloudCommand {
	public ToggleBottlesCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"togglebottles",
								Description.of("Allows a player to toggle between receiving and not receiving potion bottles."),
										"togglebottle"
						)
						.permission(MemberType.DONATOR.cloudOf("toggle-bottles"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);
							boolean toggle = player.isTogglePotionBottles();
							player.setTogglePotionBottles(!toggle);

							if (!toggle){
								messenger.message(sender, Translations.COMMAND_TOGGLE_POTION_BOTTLES_TRUE);
							} else {
								messenger.message(sender, Translations.COMMAND_TOGGLE_POTION_BOTTLES_FALSE);
							}
						})
		);
	}
}
