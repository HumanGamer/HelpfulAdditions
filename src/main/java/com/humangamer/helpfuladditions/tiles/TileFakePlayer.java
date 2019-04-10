package com.humangamer.helpfuladditions.tiles;

import com.humangamer.helpfuladditions.entities.EntityFakePlayer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;

public class TileFakePlayer extends TileEntity implements ITickable {

    private EntityFakePlayer player;

    // TODO: Have an actual inventory and GUI instead of a copy of an item that you right click onto the block.
    private ItemStack storedItem;

    @Override
    public void update() {
        if (world.isRemote)
            return;

        if (player == null) {
            player = new EntityFakePlayer((WorldServer) world);
            player.setPosition(pos.getX(), pos.getY(), pos.getZ());
        }

        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = state.getValue(BlockDirectional.FACING);

        player.rotationYaw = facing.getHorizontalAngle();

        if (facing == EnumFacing.UP)
            player.rotationPitch = -90;
        else if (facing == EnumFacing.DOWN)
            player.rotationPitch = 90;
        else
            player.rotationPitch = 75;

        if (storedItem != null && storedItem.getItem() != null)
            storedItem.getItem().onUpdate(storedItem, world, player, 0, true);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        if (compound.hasKey("item")) {
            NBTTagCompound item = compound.getCompoundTag("item");
            storedItem = new ItemStack(item);
        } else {
            storedItem = null;
        }

        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        if (storedItem != null) {
            NBTTagCompound item = new NBTTagCompound();

            storedItem.writeToNBT(item);
            compound.setTag("item", item);
        } else if (compound.hasKey("item")) {
            compound.removeTag("item");
        }

        return super.writeToNBT(compound);
    }

    public void setStoredItem(ItemStack stack)
    {
        storedItem = stack;
    }

    public ItemStack getStoredItem()
    {
        return storedItem;
    }
}
