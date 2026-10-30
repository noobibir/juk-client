package wtf.jukclient;

import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import me.zero.alpine.listener.Subscribe;
import me.zero.alpine.listener.Subscriber;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import wtf.jukclient.command.CommandManager;
import wtf.jukclient.event.KeyEvent;
import wtf.jukclient.module.ModuleManager;
import wtf.jukclient.ui.ClickGUI;

public class Juk implements Subscriber {
    private static Juk instance = null;

    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public ClickGUI clickGUI;
    public final EventBus eventManager = EventManager.builder()
            .setName("root/juk")
            .setSuperListeners()
            .build();

    public void init() {
        System.out.println("[juk] client init");

        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        clickGUI = new ClickGUI();
        eventManager.subscribe(this);
    }

    public static Juk getInstance() {
        if (instance == null) {
            instance = new Juk();
        }

        return instance;
    }

    @Subscribe
    private void onKey(KeyEvent event) {
        if (event.key == Keyboard.KEY_RSHIFT) {
            Minecraft.getMinecraft().displayGuiScreen(clickGUI);
        }
    }
}
