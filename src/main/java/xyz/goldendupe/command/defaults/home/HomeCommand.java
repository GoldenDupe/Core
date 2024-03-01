package xyz.goldendupe.command.defaults.home;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDHome;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.TimedTeleport;

@Cloud
public class HomeCommand extends GDCloudCommand {

    public HomeCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);

        commandManager.command(
                commandManager.commandBuilder(
                                "home",
                                Description.of("Teleports the player to the home provided."),
                                "h"
                        )
                        .senderType(Player.class)
                        .argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("home-name"))
                        .handler(context -> {

                            Player sender = context.sender();
                            GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
                            String homeName = context.get("home-name").toString().toLowerCase();

                            if (!goldenDupe.getHomes(player).containsKey(homeName)){
                                commandMessenger.message(sender, "home.message-doesnt-exist",
                                        new Placeholder("home", homeName));
                                return;
                            }

                            GDHome home = goldenDupe.getHomes(player).get(homeName);

                            commandMessenger.message(sender, "home.message-teleporting",
                                    new Placeholder("home", homeName));
                            new TimedTeleport(commandMessenger, "home",
                                    sender, home.asLocation(), false, 100).accept();

                        })
        );

    }

}
