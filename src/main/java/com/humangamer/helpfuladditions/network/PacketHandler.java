package com.humangamer.helpfuladditions.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public final class PacketHandler
{
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public static int nextID()
    {
        return packetId++;
    }

    public static void registerMessages(String channelName)
    {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
    }
}
