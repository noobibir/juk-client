package wtf.jukclient.module.modules.combat;

import com.google.common.base.Predicates;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.*;
import wtf.jukclient.JukGlobals;
import wtf.jukclient.event.PlayerRotEvent;
import wtf.jukclient.event.PlayerTickEvent;
import wtf.jukclient.event.TickEvent;
import wtf.jukclient.module.EnumCategory;
import wtf.jukclient.module.Module;
import wtf.jukclient.module.setting.BoolSetting;
import wtf.jukclient.module.setting.ChoiceSetting;
import wtf.jukclient.module.setting.FloatSetting;
import wtf.jukclient.module.setting.IntSetting;
import wtf.jukclient.util.ChatUtil;
import wtf.jukclient.util.Clicker;
import wtf.jukclient.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KillAura extends Module {
    private final Random random = new Random();
    private final Clicker clicker = new Clicker();
    private float yaw, pitch;
    private float targetYaw, targetPitch;
    private final List<Entity> targets = new ArrayList<>();
    private int targetIndex = 0;
    private int ticksUntilSwitch = 50;
    private boolean needRotation = false;

    private final ChoiceSetting mode = addSetting(new ChoiceSetting("mode").addOptions("single", "switch", "multi").set("single"));
    private final IntSetting switchTime = addSetting(new IntSetting("switchTime").bound(0, 100).set(50));
    private final FloatSetting reach = addSetting(new FloatSetting("reach").bound(0, 10).set(3));
    private final FloatSetting minCPS = addSetting(new FloatSetting("minCPS").bound(0, 20).set(5));
    private final FloatSetting maxCPS = addSetting(new FloatSetting("maxCPS").bound(0, 20).set(10));
    private final BoolSetting rotate = addSetting(new BoolSetting("rotate").set(true));
    private final BoolSetting fixGCD = addSetting(new BoolSetting("fixGCD").set(true));
    private final BoolSetting checkRaycast = addSetting(new BoolSetting("checkRaycast").set(true));
    private final BoolSetting raycastThruBlocks = addSetting(new BoolSetting("raycastThroughBlocks"));
    private final FloatSetting rotateDistance = addSetting(new FloatSetting("rotateDistance").bound(0, 10).set(5));
    private final FloatSetting rotateSpeedMin = addSetting(new FloatSetting("rotateSpeedMin").bound(0, 180).set(50));
    private final FloatSetting rotateSpeedMax = addSetting(new FloatSetting("rotateSpeedMax").bound(0, 180).set(100));
    private final FloatSetting jitterYaw = addSetting(new FloatSetting("jitterYaw").bound(0, 5).set(2));
    private final FloatSetting jitterPitch = addSetting(new FloatSetting("jitterPitch").bound(0, 5).set(2));
    private final BoolSetting autoblock = addSetting(new BoolSetting("autoblock"));
    private final BoolSetting unblockOnHit = addSetting(new BoolSetting("unblockOnHit"));

    public KillAura() {
        super("KillAura", EnumCategory.Combat);

        switchTime.addSettingRenderFunc(() -> mode.get().equals("switch"));
        unblockOnHit.addSettingRenderFunc(autoblock::get);
        fixGCD.addSettingRenderFunc(rotate::get);
        rotateDistance.addSettingRenderFunc(rotate::get);
        rotateSpeedMin.addSettingRenderFunc(rotate::get);
        rotateSpeedMax.addSettingRenderFunc(rotate::get);
        jitterYaw.addSettingRenderFunc(rotate::get);
        jitterPitch.addSettingRenderFunc(rotate::get);
        raycastThruBlocks.addSettingRenderFunc(checkRaycast::get);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        yaw = mc.thePlayer.rotationYaw;
        pitch = mc.thePlayer.rotationPitch;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        JukGlobals.doNotStopUsingItem = false;
    }

    @Subscribe
    private void onTick(PlayerTickEvent event) {
        if (event.isCancelled()) return;
        clicker.tick(minCPS.get(), maxCPS.get());
        updateTargets();
        updateTargetRotation();
        tickRotation();
        tryAttack();
    }

    @Subscribe
    private void onRot(PlayerRotEvent event) {
        if (rotate.get() && !targets.isEmpty() && needRotation) {
            event.yaw = this.yaw;
            event.pitch = this.pitch;
        }
    }

    private void tryAttack() {
        if (targets.isEmpty() || !canAttack()) return;
        if (clicker.clicksThisTick != 0) {
            if (mc.thePlayer.isUsingItem() && unblockOnHit.get()) {
                mc.playerController.onStoppedUsingItem(mc.thePlayer);
            }
            for (int i = 0; i < clicker.clicksThisTick; i++) {
                if (mode.get().equals("multi")) {
                    for (Entity e : targets) {
                        mc.thePlayer.swingItem();
                        mc.playerController.attackEntity(mc.thePlayer, e);
                    }
                } else {
                    mc.thePlayer.swingItem();
                    mc.playerController.attackEntity(mc.thePlayer, targets.get(targetIndex));
                }
            }
            ItemStack held = mc.thePlayer.getHeldItem();
            if (autoblock.get() && held != null && held.getItem() instanceof ItemSword) {
                if (!mc.thePlayer.isUsingItem()) {
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, held);
                }
                JukGlobals.doNotStopUsingItem = true;
            } else {
                JukGlobals.doNotStopUsingItem = false;
            }
        }
    }

    private boolean canAttack() {
        if (rotate.get() && checkRaycast.get()) {
            Vec3 eyes = mc.thePlayer.getPositionEyes(1);
            Vec3 dir = MathUtil.angles2vec(yaw, pitch);
            return raycast(eyes, dir, raycastThruBlocks.get(), reach.get());
        }
        else {
            return MathUtil.getDistanceBetween(mc.thePlayer, targets.get(targetIndex)) <= reach.get();
        }
    }

    private void updateTargetRotation() {
        if (!rotate.get() || targets.isEmpty()) {
            needRotation = false;
            return;
        }
        needRotation = true;

        Vec3 vec1 = mc.thePlayer.getPositionEyes(1);
        AxisAlignedBB aabb = targets.get(targetIndex).getEntityBoundingBox();
        Vec3 vec2 = new Vec3(
                MathHelper.clamp_double(vec1.xCoord, aabb.minX, aabb.maxX),
                MathHelper.clamp_double(vec1.yCoord, aabb.minY, aabb.maxY),
                MathHelper.clamp_double(vec1.zCoord, aabb.minZ, aabb.maxZ)
        );
        float[] rot = MathUtil.vec2angles(vec1.subtract(vec2));
        this.targetYaw = rot[0];
        this.targetPitch = rot[1];
    }

    private void updateTargets() {
        float f = rotateDistance.get();
        List<Entity> entities = mc.theWorld.getEntitiesInAABBexcluding(mc.thePlayer, mc.thePlayer.getEntityBoundingBox()
                .expand(1 + f, 1 + f, 1 + f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        for (Entity e : entities) {
            //if (!(e instanceof EntityPlayer)) continue;
            if (e == mc.thePlayer) continue;
            targets.add(e);
        }

        targets.removeIf((e) -> {
            float dist = MathUtil.getDistanceBetween(mc.thePlayer, e);
            return (dist > rotateDistance.get() && rotate.get()) ||
                    (dist > reach.get() && !rotate.get()) ||
                    !mc.theWorld.loadedEntityList.contains(e) ||
                    e.isDead;
        });
        switch (mode.get()) {
            case "switch":
                ticksUntilSwitch--;
                if (ticksUntilSwitch <= 0) {
                    ticksUntilSwitch = switchTime.get();
                    targetIndex++;
                }
                if (targetIndex >= targets.size()) {
                    targetIndex = 0;
                }
                break;
            case "single":
                float minDist = 99;
                for (int i = 0; i < targets.size(); i++) {
                    float dist = MathUtil.getDistanceBetween(mc.thePlayer, targets.get(i));
                    if (dist < minDist) {
                        minDist = dist;
                        targetIndex = i;
                    }
                }
                break;
            case "multi":
                targetIndex = (int) (random.nextFloat() * targets.size());
                break;
        }
    }

    private boolean raycast(Vec3 vecFrom, Vec3 rayVec, boolean thruBlocks, float vecLength) {
        rayVec = rayVec.scale(vecLength);
        Vec3 vecTo = vecFrom.add(rayVec);
        List<MovingObjectPosition> hits = new ArrayList<>();
        List<Entity> entities = mc.theWorld.getEntitiesInAABBexcluding(mc.thePlayer, mc.thePlayer.getEntityBoundingBox()
                .addCoord(rayVec.xCoord, rayVec.yCoord, rayVec.zCoord)
                .expand(1, 1, 1), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        for (Entity e : entities) {
            MovingObjectPosition hit = e.getEntityBoundingBox().calculateIntercept(vecFrom, vecTo);
            if (hit == null || hit.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
                continue;
            }
            hit.typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
            hit.entityHit = e;
            hits.add(hit);
        }
        if (!thruBlocks) {
            MovingObjectPosition hit = mc.theWorld.rayTraceBlocks(vecFrom, vecTo, false, false, false);
            if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                hits.add(hit);
            }
        }
        MovingObjectPosition closestHit = null;
        float minDistance = Float.MAX_VALUE;
        for (MovingObjectPosition h : hits) {
            float dist = (float) vecFrom.distanceTo(h.hitVec);
            if (dist < minDistance) {
                closestHit = h;
                minDistance = dist;
            }
        }
        return closestHit != null &&
                closestHit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY &&
                targets.contains(closestHit.entityHit);
    }

    private void tickRotation() {
        float yawDiff = MathHelper.wrapAngleTo180_float(targetYaw - yaw);
        float yawSpeed = rotateSpeedMin.get() + random.nextFloat() * (rotateSpeedMax.get() - rotateSpeedMin.get());
        yaw += MathHelper.clamp_float(yawDiff, -yawSpeed, yawSpeed);

        float pitchDiff = MathHelper.wrapAngleTo180_float(targetPitch - pitch);
        float pitchSpeed = rotateSpeedMin.get() + random.nextFloat() * (rotateSpeedMax.get() - rotateSpeedMin.get());
        pitch += MathHelper.clamp_float(pitchDiff, -pitchSpeed, pitchSpeed);

        yaw += (2 * random.nextFloat() - 1) * jitterYaw.get();
        pitch += (2 * random.nextFloat() - 2) * jitterPitch.get();
        if (fixGCD.get()) {
            yaw = MathUtil.gcd(yaw, mc.gameSettings.mouseSensitivity);
            pitch = MathUtil.gcd(pitch, mc.gameSettings.mouseSensitivity);
        }
        pitch = MathHelper.clamp_float(pitch, -90, 90);
    }
}
