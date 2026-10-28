package wtf.jukclient.event;

import lombok.AllArgsConstructor;
import me.zero.alpine.event.CancellableEvent;

@AllArgsConstructor
public class Render2dEvent extends CancellableEvent {
    public float partialTicks;
}
