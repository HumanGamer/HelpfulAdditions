package com.humangamer.helpfuladditions.init;

import com.humangamer.helpfuladditions.blocks.BlockFakePlayer;
import net.minecraft.block.material.Material;

public final class HABlocks
{

    public static BlockFakePlayer fakePlayer;

    public static void initBlocks()
    {
        fakePlayer = new BlockFakePlayer("fake_player", Material.IRON);
    }

}
