package examples.proxy.command;

import examples.proxy.player.Player;

public abstract class Command {
    private final String prefix;

    public Command(String prefix) {
        this.prefix = prefix;
    }

    public abstract void execute(Player player, String[] args);

    public String getPrefix() {
        return prefix;
    }
}