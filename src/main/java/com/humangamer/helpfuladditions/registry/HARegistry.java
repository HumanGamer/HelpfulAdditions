package com.humangamer.helpfuladditions.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public final class HARegistry {
    private static List<BlockContainer> blocks = new ArrayList<BlockContainer>();
    private static List<Item> items = new ArrayList<Item>();

    public static void registerBlock(BlockContainer blockContainer) {
        HARegistry.blocks.add(blockContainer);
    }

    public static void registerBlock(Block block, ItemBlock item) {
        HARegistry.registerBlock(new BlockContainer(block, item));
    }

    public static void registerBlock(Block block) {
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(item.getBlock().getRegistryName());
        HARegistry.registerBlock(new BlockContainer(block, item));
    }

    public static void registerItem(Item item) {
        HARegistry.items.add(item);
    }

    public static List<BlockContainer> getBlockList() {
        return HARegistry.blocks;
    }

    public static List<Item> getItemList() {
        return HARegistry.items;
    }

    public static class BlockContainer {
        private Block block;
        private ItemBlock item;

        public BlockContainer(Block block, ItemBlock item) {
            this.block = block;
            this.item = item;
        }

        public Block getBlock() {
            return this.block;
        }

        public ItemBlock getItem() {
            return this.item;
        }
    }
}
