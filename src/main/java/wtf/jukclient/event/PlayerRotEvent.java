package wtf.jukclient.event;

import lombok.AllArgsConstructor;
import me.zero.alpine.event.CancellableEvent;

@AllArgsConstructor
public class PlayerRotEvent extends CancellableEvent {
    public float yaw;
    public float pitch;
}
