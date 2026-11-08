package wtf.jukclient.module.modules.misc;

import me.zero.alpine.listener.Subscribe;
import net.minecraft.util.ChatComponentText;
import wtf.jukclient.bot.BotHelper;
import wtf.jukclient.bot.BotData;
import wtf.jukclient.event.TickEvent;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;

public class Bot extends Module {
    private BotData bot;

    public Bot() {
        super("Bot", EnumCategory.Misc);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        bot = BotHelper.createBot("localhost", 25565, "juk");
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
