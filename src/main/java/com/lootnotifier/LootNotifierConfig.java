package com.lootnotifier;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Alpha;

import java.awt.Color;

@ConfigGroup("lootnotifier")
public interface LootNotifierConfig extends Config
{
    @ConfigSection(
            name = "Low tier",
            description = "Settings for low value drops",
            position = 0
    )
    String lowSection = "low";

    @ConfigSection(
            name = "Medium tier",
            description = "Settings for medium value drops",
            position = 1
    )
    String medSection = "med";

    @ConfigSection(
            name = "High tier",
            description = "Settings for high value drops",
            position = 2
    )
    String highSection = "high";

    @ConfigSection(
            name = "Insane tier",
            description = "Settings for insane value drops",
            position = 3
    )
    String insaneSection = "insane";

    // ── LOW ──────────────────────────────────────────────────────────────────

    @ConfigItem(
            keyName = "lowEnabled",
            name = "Enable Low tier",
            description = "Toggle Low tier popups on or off",
            section = lowSection,
            position = 0
    )
    default boolean lowEnabled() { return true; }

    @ConfigItem(
            keyName = "lowThreshold",
            name = "GP threshold",
            description = "Minimum item value in GP to trigger a Low notification",
            section = lowSection,
            position = 1
    )
    default int lowThreshold() { return 10_000; }

    @ConfigItem(
            keyName = "lowTitle",
            name = "Header text",
            description = "Text shown in the header bar of the Low tier popup",
            section = lowSection,
            position = 2
    )
    default String lowTitle() { return "Collection log"; }

    @Alpha
    @ConfigItem(
            keyName = "lowColour",
            name = "Border colour",
            description = "Colour of the popup border for Low tier",
            section = lowSection,
            position = 3
    )
    default Color lowColour() { return new Color(140, 140, 140); }

    // ── MEDIUM ────────────────────────────────────────────────────────────────

    @ConfigItem(
            keyName = "medEnabled",
            name = "Enable Medium tier",
            description = "Toggle Medium tier popups on or off",
            section = medSection,
            position = 0
    )
    default boolean medEnabled() { return true; }

    @ConfigItem(
            keyName = "medThreshold",
            name = "GP threshold",
            description = "Minimum item value in GP to trigger a Medium notification",
            section = medSection,
            position = 1
    )
    default int medThreshold() { return 100_000; }

    @ConfigItem(
            keyName = "medTitle",
            name = "Header text",
            description = "Text shown in the header bar of the Medium tier popup",
            section = medSection,
            position = 2
    )
    default String medTitle() { return "Collection log"; }

    @Alpha
    @ConfigItem(
            keyName = "medColour",
            name = "Border colour",
            description = "Colour of the popup border for Medium tier",
            section = medSection,
            position = 3
    )
    default Color medColour() { return new Color(40, 180, 40); }

    // ── HIGH ──────────────────────────────────────────────────────────────────

    @ConfigItem(
            keyName = "highEnabled",
            name = "Enable High tier",
            description = "Toggle High tier popups on or off",
            section = highSection,
            position = 0
    )
    default boolean highEnabled() { return true; }

    @ConfigItem(
            keyName = "highThreshold",
            name = "GP threshold",
            description = "Minimum item value in GP to trigger a High notification",
            section = highSection,
            position = 1
    )
    default int highThreshold() { return 1_000_000; }

    @ConfigItem(
            keyName = "highTitle",
            name = "Header text",
            description = "Text shown in the header bar of the High tier popup",
            section = highSection,
            position = 2
    )
    default String highTitle() { return "Collection log"; }

    @Alpha
    @ConfigItem(
            keyName = "highColour",
            name = "Border colour",
            description = "Colour of the popup border for High tier",
            section = highSection,
            position = 3
    )
    default Color highColour() { return new Color(80, 120, 220); }

    // ── INSANE ────────────────────────────────────────────────────────────────

    @ConfigItem(
            keyName = "insaneEnabled",
            name = "Enable Insane tier",
            description = "Toggle Insane tier popups on or off",
            section = insaneSection,
            position = 0
    )
    default boolean insaneEnabled() { return true; }

    @ConfigItem(
            keyName = "insaneThreshold",
            name = "GP threshold",
            description = "Minimum item value in GP to trigger an Insane notification",
            section = insaneSection,
            position = 1
    )
    default int insaneThreshold() { return 10_000_000; }

    @ConfigItem(
            keyName = "insaneTitle",
            name = "Header text",
            description = "Text shown in the header bar of the Insane tier popup",
            section = insaneSection,
            position = 2
    )
    default String insaneTitle() { return "Collection log"; }

    @Alpha
    @ConfigItem(
            keyName = "insaneColour",
            name = "Border colour",
            description = "Colour of the popup border for Insane tier",
            section = insaneSection,
            position = 3
    )
    default Color insaneColour() { return new Color(180, 80, 180); }
}