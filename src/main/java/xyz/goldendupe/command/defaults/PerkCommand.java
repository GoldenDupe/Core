package xyz.goldendupe.command.defaults;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.cloudplusplus.minecraft.paper.bootstrap.InitAfterBootstrap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.perks.commands.ActionCommand;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class PerkCommand extends GDCloudCommand implements InitAfterBootstrap {
    private ActionCommand actionCommand;

    public PerkCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);

        commandManager.command(
                commandManager.commandBuilder(
                                "perks",
                                Description.of("View player's own perks"),
                                "perk"
                        )
                        .permission(MemberType.DEFAULT.permissionOf("clear-my-chat"))
                        .senderType(Player.class)
                        .handler(context -> {
                            Player sender = context.sender();
                            sender.sendMessage("Hey!");
                            actionCommand.run(sender);
                        })
        );
    }

    @Override
    public void init() {
        actionCommand = new ActionCommand(goldenDupe().getActionManager().getMenu());
    }
}