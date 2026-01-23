package xyz.goldendupe.perks.commands;

import org.bukkit.entity.Player;
import xyz.goldendupe.perks.gui.ActionsMenu;

public class ActionCommand {
    private final ActionsMenu actionsMenu;
    public ActionCommand(ActionsMenu actionsMenu) {
        this.actionsMenu = actionsMenu;
    }

    public void run(Player player){
        actionsMenu.open(player);
    }
}
