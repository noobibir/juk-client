package wtf.jukclient.util.pathfinder;

import lombok.AllArgsConstructor;
import net.minecraft.util.BlockPos;

@AllArgsConstructor
public class PathNode {
    public int x;
    public int y;
    public int z;

    public PathNode prevNode;

    public BlockPos blockPos() {
        return new BlockPos(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PathNode)) return false;
        PathNode node = (PathNode) o;
        return node.x == this.x && node.y == this.y && node.z == this.z;
    }

    int cantor(int a, int b) {
        return (a + b + 1) * (a + b) / 2 + b;
    }

    @Override
    public int hashCode() {
        return cantor(x, cantor(y, z));
    }
}
