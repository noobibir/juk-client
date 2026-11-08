package wtf.jukclient.module.modules.misc;

import me.zero.alpine.listener.Subscribe;
import net.minecraft.util.ChatComponentText;
import wtf.jukclient.bot.BotHelper;
import wtf.jukclient.bot.BotData;
import wtf.jukclient.event.TickEvent;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;
import wtf.jukclient.module.setting.InputSetting;

public class Bot extends Module {
    private BotData bot;

    private final InputSetting username = addSetting(new InputSetting("username"));
    private final InputSetting host = addSetting(new InputSetting("host"));
    private final InputSetting port = addSetting(new InputSetting("port"));

    public Bot() {
        super("Bot", EnumCategory.Misc);

        for (char c = 'a'; c < 'z'; c++) username.addAllowedChar(c);
        for (char c = 'A'; c < 'Z'; c++) username.addAllowedChar(c);
        for (char c = '0'; c < '9'; c++) username.addAllowedChar(c);
        username.addAllowedChar('_');

        for (char c = 'a'; c < 'z'; c++) host.addAllowedChar(c);
        for (char c = 'A'; c < 'Z'; c++) host.addAllowedChar(c);
        host.addAllowedChar('.');
        for (char c = '0'; c < '9'; c++) port.addAllowedChar(c);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        bot = BotHelper.createBot(host.get(), Integer.parseInt(port.get()), username.get());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        bot.netManager.closeChannel(new ChatComponentText("disconnected"));
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (this.bot.netManager.isChannelOpen())
        {
            this.bot.netManager.processReceivedPackets();
        }
        else
        {
            this.bot.netManager.checkDisconnected();
        }
    }
}
