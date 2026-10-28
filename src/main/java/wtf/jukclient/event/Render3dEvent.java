package wtf.jukclient.event;

import lombok.AllArgsConstructor;
import me.zero.alpine.event.CancellableEvent;

@AllArgsConstructor
public class Render3dEvent extends CancellableEvent {
    public float partialTicks;
}
