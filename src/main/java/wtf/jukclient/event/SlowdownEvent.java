package wtf.jukclient.event;

import lombok.AllArgsConstructor;
import me.zero.alpine.event.CancellableEvent;
import net.minecraft.item.ItemStack;

@AllArgsConstructor
public class SlowdownEvent extends CancellableEvent {
    public float moveStrafeFactor;
    public float moveForwardFactor;
    public final ItemStack item;
}
