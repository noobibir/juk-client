package wtf.jukclient.event;

import lombok.AllArgsConstructor;
import me.zero.alpine.event.CancellableEvent;

@AllArgsConstructor
public class KeyEvent extends CancellableEvent {
    public int key;
}
