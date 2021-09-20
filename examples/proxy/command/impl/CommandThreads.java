package examples.proxy.command.impl;

import examples.proxy.command.Command;
import examples.proxy.player.Player;
import examples.proxy.util.ChatUtil;

public class CommandThreads extends Command {
    public CommandThreads() {
        super("threads");
    }

    @Override
    public void execute(Player player, String[] args) {
        ChatUtil.sendChatMessage("threads: " + Thread.activeCount(), player.getSession(), true);
    }
}