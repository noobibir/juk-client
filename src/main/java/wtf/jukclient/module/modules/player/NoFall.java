package wtf.jukclient.module.modules.player;

import me.zero.alpine.listener.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;
import wtf.jukclient.event.PacketEvent;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;
import wtf.jukclient.module.setting.ChoiceSetting;

public class NoFall extends Module {
    private final ChoiceSetting mode = addSetting(new ChoiceSetting("mode").addOptions("forceGround", "noGround"));

    public NoFall() {
        super("NoFall", EnumCategory.Player);
    }

    @Subscribe
    private void onPacket(PacketEvent event) {
        if (event.packet instanceof C03PacketPlayer) {
            if (mode.get().equals("forceGround")) {
                ((C03PacketPlayer) event.packet).setOnGround(true);
            }
            else if (mode.get().equals("noGround")) {
                ((C03PacketPlayer) event.packet).setOnGround(false);
            }
        }
    }
}
