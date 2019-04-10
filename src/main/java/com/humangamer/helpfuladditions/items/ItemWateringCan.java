package com.humangamer.helpfuladditions.items;

import com.humangamer.helpfuladditions.Reference;
import com.humangamer.helpfuladditions.registry.HARegistry;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWateringCan extends Item {

    private int range;
    private int chance;
    private EnumRarity rarity;

    public ItemWateringCan(String name, EnumRarity rarity, int range, int chance)
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);

        this.rarity = rarity;
        this.range = range;
        this.chance = chance;

        this.setTranslationKey(name);

        this.setRegistryName(Reference.PREFIX + name);
        HARegistry.registerItem(this);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!isActive(stack) || !isSelected || world.getTotalWorldTime() % 4 != 0 || !(entity instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer)entity;
        EnumFacing facing = player.getHorizontalFacing();
        BlockPos pos = new BlockPos(player.posX, player.posY - 1, player.posZ).add(facing.getXOffset(), facing.getYOffset(), facing.getZOffset());
        this.spawnWaterParticles(world, pos);
        this.waterCrops(world, pos);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return this.rarity;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.isActive(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        if (!world.isRemote && player.isSneaking())
        {
            ItemStack can = player.getHeldItem(hand);
            setActive(can, !isActive(can));
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(isActive(stack) ? ChatFormatting.GREEN + "Active" + ChatFormatting.RESET : ChatFormatting.RED + "Inactive" + ChatFormatting.RESET);
        int rangeDisplay = (range + 2);
        tooltip.add("Range: " + rangeDisplay + "x" + rangeDisplay);
        tooltip.add("Effectiveness: " + chance + "%");
    }

    private boolean isActive(ItemStack stack)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound("watering_can");
        return compound.getBoolean("active");
    }

    private void setActive(ItemStack stack, boolean active)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound("watering_can");
        compound.setBoolean("active", active);
    }

    private void spawnWaterParticles(World world, BlockPos pos)
    {
        for (int i = -range; i <= range; i++)
        {
            for (int j = -range; j<= range; j++)
            {
                double d0 = pos.add(i, 0, j).getX() + itemRand.nextFloat();
                double d1 = pos.add(i, 0, j).getY() + 1.0D;
                double d2 = pos.add(i, 0, j).getZ() + itemRand.nextFloat();

                IBlockState state = world.getBlockState(pos.add(i, 0, j));
                if (state.getBlock() instanceof BlockFarmland || state.isFullBlock())
                    d1 += 1.0D;

                world.spawnParticle(EnumParticleTypes.WATER_DROP, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[5]);
            }
        }
    }

    private void waterCrops(World world, BlockPos pos)
    {
        if (world.isRemote || chance < itemRand.nextInt(100) + 1)
            return;

        for (int i = -range; i <= range; i++)
        {
            for (int j = -range; j <= range; j++)
            {
                for (int k = -range; k <= range; k++)
                {
                    IBlockState state = world.getBlockState(pos.add(i, j, k));
                    Block block = state.getBlock();

                    if (block instanceof IGrowable || block == Blocks.MYCELIUM || block == Blocks.CACTUS || block == Blocks.REEDS || block == Blocks.CHORUS_FLOWER)
                    {
                        world.scheduleBlockUpdate(pos.add(i, j, k), block, 0, 1);
                    } else if (block instanceof BlockFarmland)
                    {
                        // Moisturize the soil
                        int moisture = state.getValue(BlockFarmland.MOISTURE);
                        if (moisture < 7)
                            world.setBlockState(pos.add(i, j, k), state.withProperty(BlockFarmland.MOISTURE, Integer.valueOf(7)), 2);
                    }
                }
            }
        }
    }
}
