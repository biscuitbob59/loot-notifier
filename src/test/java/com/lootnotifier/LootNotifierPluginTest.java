package com.lootnotifier;

import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.plugins.Plugin;

public class LootNotifierPluginTest
{
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin((Class<? extends Plugin>) (Class<?>) LootNotifierPlugin.class);
        net.runelite.client.RuneLite.main(args);
    }
}