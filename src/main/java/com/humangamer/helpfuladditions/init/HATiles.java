package com.humangamer.helpfuladditions.init;

import com.humangamer.helpfuladditions.Reference;
import com.humangamer.helpfuladditions.tiles.TileFakePlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class HATiles {

    public static void initTiles()
    {
        GameRegistry.registerTileEntity(TileFakePlayer.class, new ResourceLocation(Reference.PREFIX + "fake_player"));
    }
}
