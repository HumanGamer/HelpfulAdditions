package com.humangamer.helpfuladditions.items;

import com.humangamer.helpfuladditions.Reference;
import com.humangamer.helpfuladditions.registry.HARegistry;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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

        RayTraceResult ray = rayTrace(world, player, false);
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            this.spawnWaterParticles(world, ray.getBlockPos());
            this.waterCrops(world, ray.getBlockPos());
        }
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
                double d0 = pos.add(i, 0, j).getX() + world.rand.nextFloat();
                double d1 = pos.add(i, 0, j).getY() + 1.0D;
                double d2 = pos.add(i, 0, j).getZ() + world.rand.nextFloat();

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
                    Block target = world.getBlockState(pos.add(i, j, k)).getBlock();

                    if (target instanceof IGrowable || target == Blocks.MYCELIUM || target == Blocks.CACTUS || target == Blocks.REEDS || target == Blocks.CHORUS_FLOWER)
                    {
                        world.scheduleBlockUpdate(pos.add(i, j, k), target, 0, 1);
                    }
                }
            }
        }
    }
}