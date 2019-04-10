package com.humangamer.helpfuladditions.events;

import com.humangamer.helpfuladditions.registry.HARegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryEventHandler {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (HARegistry.BlockContainer blockContainer : HARegistry.getBlockList()) {
            Block block = blockContainer.getBlock();
            event.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (HARegistry.BlockContainer blockContainer : HARegistry.getBlockList()) {
            ItemBlock item = blockContainer.getItem();
            event.getRegistry().register(item);
        }

        for (Item item : HARegistry.getItemList()) {
            event.getRegistry().register(item);
        }
    }
}
