package wtf.jukclient.event;

import me.zero.alpine.event.CancellableEvent;
import net.minecraft.network.Packet;

public class PacketEvent extends CancellableEvent {
    public Packet<?> packet;
    public EventFlow flow;

    public PacketEvent (Packet<?> packet) {this.packet = packet;}

    public boolean isInbound () {return flow != null && flow == EventFlow.INBOUND;}
    public boolean isOutbound () {return flow != null && flow == EventFlow.OUTBOUND;}

    public enum EventFlow {
        INBOUND, OUTBOUND;
    }
}
