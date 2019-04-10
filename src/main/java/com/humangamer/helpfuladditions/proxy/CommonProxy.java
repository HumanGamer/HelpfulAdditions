package com.humangamer.helpfuladditions.proxy;

import com.humangamer.helpfuladditions.init.HABlocks;
import com.humangamer.helpfuladditions.init.HAItems;
import com.humangamer.helpfuladditions.init.HATiles;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event)
    {
        HABlocks.initBlocks();
        HAItems.initItems();
        HATiles.initTiles();
    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }

}
