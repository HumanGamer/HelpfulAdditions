package com.humangamer.helpfuladditions.blocks;

import com.humangamer.helpfuladditions.Reference;
import com.humangamer.helpfuladditions.registry.HARegistry;
import com.humangamer.helpfuladditions.tiles.TileFakePlayer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFakePlayer extends BlockDirectional implements ITileEntityProvider {

    public BlockFakePlayer(String name, Material material) {
        super(material);

        this.setCreativeTab(CreativeTabs.DECORATIONS);

        this.setTranslationKey(name);
        this.setRegistryName(Reference.PREFIX + name);
        HARegistry.registerBlock(this);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFakePlayer();
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        setDefaultDirection(world, pos, state);
    }

    private void setDefaultDirection(World world, BlockPos pos, IBlockState state)
    {
        if (!world.isRemote)
        {
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
            boolean flag = world.getBlockState(pos.north()).isFullBlock();
            boolean flag1 = world.getBlockState(pos.south()).isFullBlock();

            if (enumfacing == EnumFacing.NORTH && flag && !flag1)
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && flag1 && !flag)
            {
                enumfacing = EnumFacing.NORTH;
            }
            else
            {
                boolean flag2 = world.getBlockState(pos.west()).isFullBlock();
                boolean flag3 = world.getBlockState(pos.east()).isFullBlock();

                if (enumfacing == EnumFacing.WEST && flag2 && !flag3)
                {
                    enumfacing = EnumFacing.EAST;
                }
                else if (enumfacing == EnumFacing.EAST && flag3 && !flag2)
                {
                    enumfacing = EnumFacing.WEST;
                }
            }

            world.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (world.isRemote)
            return true;


        // TODO: Replace this with a GUI
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileFakePlayer)
        {
            TileFakePlayer tile = (TileFakePlayer)tileEntity;

            tile.setStoredItem(player.getHeldItem(hand).copy());
        }

        return true;
    }

    public static IPosition getFacingPosition(IBlockSource coords)
    {
        EnumFacing enumfacing = (EnumFacing)coords.getBlockState().getValue(FACING);
        double d0 = coords.getX() + 0.7D * (double)enumfacing.getXOffset();
        double d1 = coords.getY() + 0.7D * (double)enumfacing.getYOffset();
        double d2 = coords.getZ() + 0.7D * (double)enumfacing.getZOffset();
        return new PositionImpl(d0, d1, d2);
    }


    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7));
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }
}
