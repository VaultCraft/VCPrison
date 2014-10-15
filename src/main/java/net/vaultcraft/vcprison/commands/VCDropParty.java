package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.event.DropParty;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.events.ServerEventHandler;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 10/14/14
 */
public class VCDropParty extends ICommand {

    public VCDropParty(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        DropParty dp = DropParty.getInstance();

        if (args.length == 0) {
            int left = (int) ServerEventHandler.getTimeRemaining(dp.getDropEvent());

            Form.at(player, Prefix.VAULT_CRAFT, "The drop party will begin in " + MMSS(left) + "!");
        }

        switch (args[0].toLowerCase()) {
            case "force":
            case "forcestart":
            case "begin": {
                ServerEventHandler.setTimeRemaining(dp.getDropEvent(), 5);
                Form.at(player, Prefix.WARNING, "Drop party will begin in 5 seconds!");
                break;
            }
        }
    }

    private static String MMSS(int in) {
        if (in >= 60) {
            int mins = (int)(in/60);
            int left = in % 60;

            return mins + " minutes, " + left + " seconds";
        }

        return in + " seconds";
    }
}
