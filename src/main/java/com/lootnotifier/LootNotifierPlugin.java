package com.lootnotifier;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.Color;
import java.util.Collection;

@Slf4j
@PluginDescriptor(
        name = "Loot Notifier",
        description = "Shows a collection log style popup for drops above your GP thresholds",
        tags = {"loot", "drops", "notification", "collection", "log"}
)
public class LootNotifierPlugin extends Plugin
{
    @Inject private Client client;
    @Inject private ItemManager itemManager;
    @Inject private OverlayManager overlayManager;
    @Inject private LootNotifierConfig config;
    @Inject private LootNotifierOverlay overlay;

    @Override
    protected void startUp()
    {
        overlayManager.add(overlay);
        log.info("Loot Notifier started");
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        log.info("Loot Notifier stopped");
    }

    @Provides
    LootNotifierConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(LootNotifierConfig.class);
    }

    @Subscribe
    public void onNpcLootReceived(NpcLootReceived event)
    {
        processLoot(event.getItems());
    }

    @Subscribe
    public void onPlayerLootReceived(PlayerLootReceived event)
    {
        processLoot(event.getItems());
    }

    private void processLoot(Collection<ItemStack> items)
    {
        long bestPrice    = 0;
        String bestName   = null;
        TierMatch bestMatch = null;

        for (ItemStack stack : items)
        {
            int itemId   = stack.getId();
            int quantity = stack.getQuantity();
            long gePrice = (long) itemManager.getItemPrice(itemId) * quantity;

            TierMatch match = getTier(gePrice);
            if (match == null) continue;

            if (gePrice > bestPrice)
            {
                bestPrice = gePrice;
                bestMatch = match;
                ItemComposition comp = itemManager.getItemComposition(itemId);
                bestName = comp.getName() + (quantity > 1 ? " x" + quantity : "");
            }
        }

        if (bestMatch != null)
        {
            overlay.showNotification(bestName, bestMatch.title, bestMatch.accent);
        }
    }

    private TierMatch getTier(long price)
    {
        if (config.insaneEnabled() && price >= config.insaneThreshold())
            return new TierMatch(config.insaneTitle(), config.insaneColour());

        if (config.highEnabled() && price >= config.highThreshold())
            return new TierMatch(config.highTitle(), config.highColour());

        if (config.medEnabled() && price >= config.medThreshold())
            return new TierMatch(config.medTitle(), config.medColour());

        if (config.lowEnabled() && price >= config.lowThreshold())
            return new TierMatch(config.lowTitle(), config.lowColour());

        return null;
    }

    private static class TierMatch
    {
        final String title;
        final Color  accent;
        TierMatch(String title, Color accent)
        { this.title = title; this.accent = accent; }
    }
}