package net.minecraft.entity.player;

import lombok.Getter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public enum EnumPlayerModelParts
{
    CAPE(0, "cape"),
    JACKET(1, "jacket"),
    LEFT_SLEEVE(2, "left_sleeve"),
    RIGHT_SLEEVE(3, "right_sleeve"),
    LEFT_PANTS_LEG(4, "left_pants_leg"),
    RIGHT_PANTS_LEG(5, "right_pants_leg"),
    HAT(6, "hat");

    @Getter
    private final int partId;
    @Getter
    private final int partMask;
    @Getter
    private final String partName;
    @Getter
    private final IChatComponent partNameComponent;

    EnumPlayerModelParts(int partIdIn, String partNameIn)
    {
        this.partId = partIdIn;
        this.partMask = 1 << partIdIn;
        this.partName = partNameIn;
        this.partNameComponent = new ChatComponentTranslation("options.modelPart." + partNameIn);
    }
}
