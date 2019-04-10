package com.humangamer.helpfuladditions.compat;

import com.humangamer.helpfuladditions.registry.HARegistry;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class WailaProvider
{

    public static void register(IWailaRegistrar registry)
    {
        GenericProvider gpInstance = new GenericProvider();

        for (int i = 0; i < HARegistry.getBlockList().size(); i++)
        {
            Block block = HARegistry.getBlockList().get(i).getBlock();
            if (block instanceof IWailaInfoProvider)
            {
                registry.registerStackProvider(gpInstance, block.getClass());
                registry.registerHeadProvider(gpInstance, block.getClass());
                registry.registerBodyProvider(gpInstance, block.getClass());
                registry.registerTailProvider(gpInstance, block.getClass());
            }
        }
    }

    public static class GenericProvider implements IWailaDataProvider
    {

        @Nonnull
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            Block block = accessor.getBlock();
            if (block instanceof IWailaInfoProvider)
            {
                IWailaInfoProvider provider = (IWailaInfoProvider) block;
                return provider.getWailaStack(accessor, config);
            }
            return accessor.getStack();
        }

        @Nonnull
        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            Block block = accessor.getBlock();
            if (block instanceof IWailaInfoProvider)
            {
                IWailaInfoProvider provider = (IWailaInfoProvider) block;
                return provider.getWailaHead(itemStack, currenttip, accessor, config);
            }
            return currenttip;
        }

        @Nonnull
        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            Block block = accessor.getBlock();
            if (block instanceof IWailaInfoProvider)
            {
                IWailaInfoProvider provider = (IWailaInfoProvider) block;
                return provider.getWailaBody(itemStack, currenttip, accessor, config);
            }
            return currenttip;
        }

        @Nonnull
        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            Block block = accessor.getBlock();
            if (block instanceof IWailaInfoProvider)
            {
                IWailaInfoProvider provider = (IWailaInfoProvider) block;
                return provider.getWailaTail(itemStack, currenttip, accessor, config);
            }
            return currenttip;
        }

        @Nonnull
        @Override
        public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
        {
            IBlockState state = world.getBlockState(pos);
            if (state == null)
                return tag;
            Block block = state.getBlock();
            if (block instanceof IWailaInfoProvider)
            {
                IWailaInfoProvider provider = (IWailaInfoProvider) block;
                return provider.getWailaNBTData(player, te, tag, world, pos);
            }
            return tag;
        }

    }

}
