package wtf.jukclient.module.modules.movement;

import me.zero.alpine.listener.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import wtf.jukclient.Juk;
import wtf.jukclient.event.TickEvent;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;
import wtf.jukclient.module.modules.combat.KillAura;
import wtf.jukclient.module.setting.FloatSetting;

public class TargetStrafe extends Module {
    private float angle = 0;
    private final FloatSetting speed = addSetting(new FloatSetting("speed").bound(0, 10).set(3));
    private final FloatSetting angularSpeed = addSetting(new FloatSetting("angularSpeed").bound(0, 360).set(5));
    private final FloatSetting radius = addSetting(new FloatSetting("radius").bound(0, 6).set(2));

    public TargetStrafe() {
        super("TargetStrafe", EnumCategory.Movement);
    }

    @Subscribe
    private void onTick(TickEvent event) {
        KillAura aura = Juk.getInstance().moduleManager.getModule(KillAura.class);
        Entity target = aura.getTarget();
        if (target != null && aura.isEnabled()) {
            angle += angularSpeed.get();
            double sin = Math.sin(Math.toRadians(angle));
            double cos = Math.cos(Math.toRadians(angle));

            Vec3 circlePos = new Vec3(
                    target.posX + sin * radius.get(),
                    target.posY,
                    target.posZ + cos * radius.get()
            );
            Vec3 motion = circlePos.subtract(mc.thePlayer.getPositionVector());
            if (motion.lengthVector() > speed.get()) {
                motion = motion.normalize();
                mc.thePlayer.motionX = motion.xCoord * speed.get();
                mc.thePlayer.motionY = motion.yCoord * speed.get();
                mc.thePlayer.motionZ = motion.zCoord * speed.get();
            } else {
                mc.thePlayer.motionX = motion.xCoord;
                mc.thePlayer.motionY = motion.yCoord;
                mc.thePlayer.motionZ = motion.zCoord;
            }
        }
    }
}
