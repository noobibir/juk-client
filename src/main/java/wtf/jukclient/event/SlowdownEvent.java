package wtf.jukclient.event;

import me.zero.alpine.event.CancellableEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;

public class SlowdownEvent extends CancellableEvent {
    public float x, y;
    public boolean sprintState;
    public SlowDownType type;

    public static SlowdownEvent usingItem(Item item) {
        SlowdownEvent event = new SlowdownEvent();
        event.x = 0.2f;
        event.y = 0.2f;
        event.type = SlowDownType.fromItem(item);
        return event;
    }

    public static SlowdownEvent hit() {
        SlowdownEvent event = new SlowdownEvent();
        event.x = 0.6f;
        event.y = 0f;
        event.type = SlowDownType.HIT;
        event.sprintState = false;
        return event;
    }

    public static SlowdownEvent web() {
        SlowdownEvent event = new SlowdownEvent();
        event.x = 0.25f;
        event.y = 0.05f;
        event.type = SlowDownType.WEB;
        return event;
    }

    public enum SlowDownType {
        SWORD, BOW, FOOD, WEB, HIT;

        public static SlowDownType fromItem(Item item) {
            if (item instanceof ItemSword) {
                return SWORD;
            }
            else if (item instanceof ItemBow) {
                return BOW;
            }
            else if (item instanceof ItemFood) {
                return FOOD;
            }
            else {
                return null;
            }
        }
    }
}
