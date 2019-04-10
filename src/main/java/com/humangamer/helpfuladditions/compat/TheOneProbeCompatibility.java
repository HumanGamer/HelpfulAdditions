package com.humangamer.helpfuladditions.compat;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import javax.annotation.Nullable;
import java.util.function.Function;

public class TheOneProbeCompatibility
{
    private static boolean registered;

    public static void register()
    {
        if (registered)
            return;

        registered = true;

        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.humangamer.helpfuladditions.compat.TheOneProbeCompatibility$GetTheOneProbe");
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void>
    {
        public static ITheOneProbe probe;

        @Nullable
        @Override
        public Void apply(ITheOneProbe probe)
        {
            this.probe = probe;

            this.probe.registerProvider(new IProbeInfoProvider()
            {
                @Override
                public String getID()
                {
                    return "helpfuladditions:default";
                }

                @Override
                public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
                {
                    if (blockState.getBlock() instanceof ITopInfoProvider)
                    {
                        ITopInfoProvider provider = (ITopInfoProvider) blockState.getBlock();
                        provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
                    }
                }
            });

            return null;
        }
    }
}
