package wtf.jukclient.bot;

import net.minecraft.util.MovementInput;

public class BotMovementInput extends MovementInput {
    private final BotSettings keys;
    
    public BotMovementInput(BotSettings settings) {
        this.keys = settings;
    }
    
    @Override
    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (keys.keyBindForward)
        {
            ++this.moveForward;
        }

        if (keys.keyBindBack)
        {
            --this.moveForward;
        }

        if (keys.keyBindLeft)
        {
            ++this.moveStrafe;
        }

        if (keys.keyBindRight)
        {
            --this.moveStrafe;
        }

        this.jump = keys.keyBindJump;
        this.sneak = keys.keyBindSneak;

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}
