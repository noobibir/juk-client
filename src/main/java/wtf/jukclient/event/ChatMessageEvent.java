package wtf.jukclient.event;

import lombok.AllArgsConstructor;
import me.zero.alpine.event.CancellableEvent;

@AllArgsConstructor
public class ChatMessageEvent extends CancellableEvent {
    public String message;
}
