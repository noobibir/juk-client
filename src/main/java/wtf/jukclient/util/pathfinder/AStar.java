package wtf.jukclient.util.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class AStar {
    private static float h(PathNode from, PathNode to) {
        float dx = from.x - to.x;
        float dy = from.y - to.y;
        float dz = from.z - to.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private static float getBlockCost(Block b) {
        if (b == Blocks.air) {
            return 1;
        }
        if (b == Blocks.web || b == Blocks.water || b == Blocks.flowing_water) {
            return 10;
        }
        if (b == Blocks.lava || b == Blocks.flowing_lava) {
            return 50;
        }
        return b.getBlockHardness(null, null);
    }

    private static float d(PathNode from, PathNode to, World world) {
        Block block = world.getBlockState(to.blockPos()).getBlock();
        Block block2 = world.getBlockState(to.blockPos().up()).getBlock();
        return getBlockCost(block) + getBlockCost(block2);
    }

    private static List<PathNode> recoverPath(PathNode node) {
        List<PathNode> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.prevNode;
        }
        Collections.reverse(path);
        return path;
    }

    private static List<PathNode> getAdjacentNodes(PathNode node) {
        List<PathNode> nodes = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    nodes.add(new PathNode(
                            node.x + dx,
                            node.y + dy,
                            node.z + dz,
                            node
                    ));
                }
            }
        }
        return nodes;
    }

    private static boolean contains(List<PathNode> list, PathNode element) {
        return list.stream().anyMatch((node) -> node.equals(element));
    }

    public static List<PathNode> pathfind(PathNode start, PathNode goal, World world) {
        List<PathNode> u = new ArrayList<>();
        List<PathNode> q = new ArrayList<>();
        Map<PathNode, Float> g = new HashMap<>();
        Map<PathNode, Float> f = new HashMap<>();
        q.add(start);
        g.put(start, 0f);
        f.put(start, h(start, goal));

        while (!q.isEmpty()) {
            PathNode current = q.stream().min((node1, node2) -> Float.compare(f.get(node1), f.get(node2))).get();
            if (current.equals(goal)) {
                return recoverPath(current);
            }
            q.remove(current);
            u.add(current);
            for (PathNode next : getAdjacentNodes(current)) {
                float newScore = g.get(current) + d(current, next, world);
                if (contains(u, next) && newScore >= g.get(next)) {
                    continue;
                }
                if (!contains(u, next) || newScore < g.get(next)) {
                    next.prevNode = current;
                    g.put(next, newScore);
                    f.put(next, newScore + h(next, goal));
                    if (!q.contains(next)) {
                        q.add(next);
                    }
                }
            }
        }
        return null;
    }
}
