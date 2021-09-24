package examples.proxy.command.impl;

import examples.proxy.command.Command;
import examples.proxy.player.Player;
import examples.proxy.util.ChatUtil;
import examples.proxy.util.WorldUtil;

public class CommandQuit extends Command {
    public CommandQuit() {
        super("q");
    }

    @Override
    public void execute(Player player, String[] args) {
        player.getRemoteSession().disconnect();
        player.setRemoteSession(null);
        player.setConnectedType(Player.ConnectedType.DISCONNECTED);
        ChatUtil.sendChatMessage("disconnected", player.getSession(), true);
        WorldUtil.emptyWorld(player.getSession());
    }
}