package wtf.jukclient.module.modules.movement;

import me.zero.alpine.listener.Subscribe;
import wtf.jukclient.event.AttackEvent;
import wtf.jukclient.event.TickEvent;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;

public class Sprint extends Module {
    boolean noSprintNextTick = false;

    public Sprint() {
        super("Sprint", EnumCategory.Movement);
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (mc.thePlayer.moveForward <= 0) return;
        if (mc.thePlayer.isCollidedHorizontally) return;
        if (noSprintNextTick) {
            noSprintNextTick = false;
            return;
        }

        mc.thePlayer.setSprinting(true);
    }

    @Subscribe
    private void onAttack(AttackEvent event) {
        noSprintNextTick = true;
    }
}
