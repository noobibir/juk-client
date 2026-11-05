package wtf.jukclient.module.modules.movement;

import me.zero.alpine.listener.Subscribe;
import wtf.jukclient.event.SlowdownEvent;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", EnumCategory.Movement);
    }

    @Subscribe
    private void onSlowdown(SlowdownEvent event) {
        event.cancel();
    }
}
