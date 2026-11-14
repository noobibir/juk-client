package wtf.jukclient.module.modules.movement;

import me.zero.alpine.listener.Subscribe;
import wtf.jukclient.event.SlowdownEvent;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;
import wtf.jukclient.module.setting.BoolSetting;
import wtf.jukclient.module.setting.FloatSetting;

public class NoSlow extends Module {
    private final BoolSetting hit = addSetting(new BoolSetting("hit").set(true));
    private final FloatSetting hitSlowdown = addSetting(new FloatSetting("hitSlowdown").bound(0, 2).set(1));
    private final BoolSetting sprintAfterHit = addSetting(new BoolSetting("sprintAfterHit").set(true));
    private final BoolSetting sword = addSetting(new BoolSetting("sword").set(true));
    private final FloatSetting swordSlowdown = addSetting(new FloatSetting("swordSlowdown").bound(0, 1).set(1));
    private final BoolSetting bow = addSetting(new BoolSetting("bow").set(true));
    private final FloatSetting bowSlowdown = addSetting(new FloatSetting("bowSlowdown").bound(0, 1).set(1));
    private final BoolSetting food = addSetting(new BoolSetting("food").set(true));
    private final FloatSetting foodSlowdown = addSetting(new FloatSetting("foodSlowdown").bound(0, 1).set(1));
    private final BoolSetting web = addSetting(new BoolSetting("web").set(true));
    private final FloatSetting webSlowdown = addSetting(new FloatSetting("webSlowdown").bound(0, 1).set(1));
    private final BoolSetting webMotionReset = addSetting(new BoolSetting("webMotionReset").set(false));

    public NoSlow() {
        super("NoSlow", EnumCategory.Movement);
    }

    @Subscribe
    private void onSlowdown(SlowdownEvent event) {
        if (event.type == SlowdownEvent.SlowDownType.HIT && hit.get()) {
            event.x = hitSlowdown.get();
            event.sprintState = sprintAfterHit.get();
        }
        else if (event.type == SlowdownEvent.SlowDownType.SWORD && sword.get()) {
            event.x = event.y = swordSlowdown.get();
        }
        else if (event.type == SlowdownEvent.SlowDownType.BOW && bow.get()) {
            event.x = event.y = bowSlowdown.get();
        }
        else if (event.type == SlowdownEvent.SlowDownType.FOOD && food.get()) {
            event.x = event.y = foodSlowdown.get();
        }
        else if (event.type == SlowdownEvent.SlowDownType.WEB && web.get()) {
            event.x = event.y = webSlowdown.get();
            if (!webMotionReset.get()) {
                event.cancel();
            }
        }
    }
}
