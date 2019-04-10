package com.humangamer.helpfuladditions.proxy;

import com.humangamer.helpfuladditions.registry.HARegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        this.registerItemModels();
    }

    public void registerItemModels() {
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        for (HARegistry.BlockContainer blockContainer : HARegistry.getBlockList()) {
            Block block = blockContainer.getBlock();
            Item item = Item.getItemFromBlock(block);

            ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
            ModelLoader.registerItemVariants(item, model);
            mesher.register(item, 0, model);
        }

        for (Item item : HARegistry.getItemList()) {
            ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ModelLoader.registerItemVariants(item, model);
            mesher.register(item, 0, model);
        }
    }
}
