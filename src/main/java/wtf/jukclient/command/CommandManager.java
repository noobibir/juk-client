package wtf.jukclient.command;

import lombok.Getter;
import me.zero.alpine.listener.Subscribe;
import me.zero.alpine.listener.Subscriber;
import org.reflections.Reflections;
import wtf.jukclient.Juk;
import wtf.jukclient.event.ChatMessageEvent;
import wtf.jukclient.util.ChatUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class CommandManager implements Subscriber {
    @Getter
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandManager() {
        register();
        Juk.getInstance().eventManager.subscribe(this);
    }

    private void register() {
        final Reflections refl = new Reflections("wtf.jukclient.command.commands");
        final Set<Class<? extends Command>> classes = refl.getSubTypesOf(Command.class);

        for (Class<? extends Command> m : classes) {
            try {
                final Command inst = m.newInstance();
                commands.put(inst.getName(), inst);
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("exception occurred while instantiating command " + m.getSimpleName());
            }
        }
    }

    @Subscribe(priority = 1) // more priority than chat bypass
    private void onChat(ChatMessageEvent event) {
        String message = event.message;
        if (!message.startsWith(".")) return;
        event.cancel();
        message = message.substring(1);
        String[] parts = message.split("\\.");
        String commandName = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        Command cmd = commands.getOrDefault(parts[0], null);
        if (cmd == null) {
            ChatUtil.send("Unknown command: %s", commandName);
            return;
        }
        cmd.execute(args);
    }
}
