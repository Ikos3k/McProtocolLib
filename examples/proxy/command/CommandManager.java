package examples.proxy.command;

import examples.proxy.command.impl.CommandJoin;
import examples.proxy.command.impl.CommandQuit;
import examples.proxy.command.impl.CommandThreads;
import examples.proxy.player.Player;
import examples.proxy.player.PlayerManager;
import examples.proxy.util.ChatUtil;
import me.ANONIMUS.mcprotocol.network.objects.Session;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private static final List<Command> commandList = new ArrayList<>();

    public static void init() {
        commandList.add(new Command("test") {
            @Override
            public void execute(Player player, String[] args) {
                System.out.println("TEST");
            }
        });

        commandList.add(new CommandJoin());
        commandList.add(new CommandQuit());
        commandList.add(new CommandThreads());
    }

    public static void handle(Session session, String message) {
        String[] args = message.split(" ");
        for (Command command : commandList) {
            if (args[0].equals("," + command.getPrefix())) {
                command.execute(PlayerManager.getPlayer(session), args);
                return;
            }
        }

        ChatUtil.sendChatMessage("&4UNKNOWN COMMAND", session, true);
    }
}