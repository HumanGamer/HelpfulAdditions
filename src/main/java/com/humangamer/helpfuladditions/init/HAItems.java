package com.humangamer.helpfuladditions.init;

import com.humangamer.helpfuladditions.items.ItemWateringCan;
import net.minecraft.item.EnumRarity;

public final class HAItems {

    public static ItemWateringCan basicWateringCan;
    public static ItemWateringCan reinforcedWateringCan;

    public static void initItems()
    {
        basicWateringCan = new ItemWateringCan("basic_watering_can", EnumRarity.UNCOMMON, 1, 50);
        reinforcedWateringCan = new ItemWateringCan("reinforced_watering_can", EnumRarity.RARE, 2, 100);
    }
}
