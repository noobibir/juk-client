package wtf.jukclient.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class MathUtil {
    public static float gcd(float angle, float mouseSensitivity) {
        float f = mouseSensitivity * 0.6F + 0.2F;
        float gcd = f * f * f * 8.0F;
        return (float) (Math.floor(angle / gcd) * gcd);
    }

    public static float getDistanceBetween(Entity from, Entity to) {
        Vec3 vec1 = from.getPositionEyes(1);
        AxisAlignedBB aabb = to.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(
                MathHelper.clamp_double(vec1.xCoord, aabb.minX, aabb.maxX),
                MathHelper.clamp_double(vec1.yCoord, aabb.minY, aabb.maxY),
                MathHelper.clamp_double(vec1.zCoord, aabb.minZ, aabb.maxZ)
        );
        return (float) vec1.distanceTo(vec2);
    }

    public static float[] vec2angles(Vec3 vec) {
        double dx = vec.xCoord;
        double dy = vec.yCoord;
        double dz = vec.zCoord;

        float yaw = (float) ((0.5 * Math.PI + Math.atan2(dz, dx)) * 180 / Math.PI);
        float pitch = (float) (Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 180 / Math.PI);

        return new float[] { yaw, pitch };
    }

    public static Vec3 angles2vec (float yaw, float pitch) {
        return new Vec3(
                Math.sin((float) Math.toRadians(-yaw) - (float)Math.PI) *
                        -Math.cos(Math.toRadians(-pitch)),
                Math.sin(Math.toRadians(-pitch)),
                Math.cos(Math.toRadians(-yaw) - (float)Math.PI) *
                        -Math.cos(Math.toRadians(-pitch))
        );
    }
}
