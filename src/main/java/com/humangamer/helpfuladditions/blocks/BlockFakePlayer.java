package com.humangamer.helpfuladditions.blocks;

import com.humangamer.helpfuladditions.Reference;
import com.humangamer.helpfuladditions.compat.ITopInfoProvider;
import com.humangamer.helpfuladditions.compat.IWailaInfoProvider;
import com.humangamer.helpfuladditions.registry.HARegistry;
import com.humangamer.helpfuladditions.tiles.TileFakePlayer;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ITextStyle;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.TextStyle;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockFakePlayer extends Block implements ITileEntityProvider, ITopInfoProvider, IWailaInfoProvider
{

    public BlockFakePlayer(String name, Material material)
    {
        super(material);

        this.setCreativeTab(CreativeTabs.DECORATIONS);

        this.setTranslationKey(name);
        this.setRegistryName(Reference.PREFIX + name);
        HARegistry.registerBlock(this);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
            return true;

        // TODO: Replace this with a GUI
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileFakePlayer)
        {
            TileFakePlayer tile = (TileFakePlayer) tileEntity;

            tile.setStoredItem(player.getHeldItem(hand).copy());
        }

        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isTopSolid(IBlockState state)
    {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileFakePlayer();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
    {
        TileEntity tileEntity = world.getTileEntity(data.getPos());
        if (tileEntity instanceof TileFakePlayer)
        {
            TileFakePlayer te = (TileFakePlayer) tileEntity;

            ItemStack stack = te.getStoredItem();
            float pitch = te.getPitch();
            float yaw = te.getYaw();

            if (stack == null || stack.getCount() <= 0)
            {
                probeInfo.text("No Item");
            } else
            {
                probeInfo.item(stack);
                probeInfo.itemLabel(stack);
            }
            probeInfo.text("Pitch: " + pitch);
            probeInfo.text("Yaw: " + yaw);
        }
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        TileEntity tileEntity = accessor.getTileEntity();
        if (tileEntity instanceof TileFakePlayer)
        {
            TileFakePlayer te = (TileFakePlayer) tileEntity;

            ItemStack stack = te.getStoredItem();
            float pitch = te.getPitch();
            float yaw = te.getYaw();

            String itemName;
            if (stack == null || stack.getCount() <= 0)
                itemName = "None";
            else
                itemName = stack.getDisplayName() + " x" + stack.getCount();

            currenttip.add("Item: " + itemName);
            currenttip.add("Pitch: " + pitch);
            currenttip.add("Yaw: " + yaw);
        }

        return currenttip;
    }

}
