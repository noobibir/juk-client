package wtf.jukclient.event;

import lombok.AllArgsConstructor;
import me.zero.alpine.event.CancellableEvent;
import net.minecraft.entity.Entity;

@AllArgsConstructor
public class AttackEvent extends CancellableEvent {
    public Entity target;
}
