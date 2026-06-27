package com.lootnotifier;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.awt.*;

public class LootNotifierOverlay extends Overlay
{
    private static final int DISPLAY_DURATION = 5000;
    private static final int PHASE_HORIZ_IN   = 500;
    private static final int PHASE_VERT_IN    = 500;
    private static final int PHASE_VERT_OUT   = 500;
    private static final int PHASE_HORIZ_OUT  = 500;

    private static final int BOX_W   = 260;
    private static final int HDR_H   = 28;
    private static final int BODY_H  = 48;
    private static final int POPUP_H = HDR_H + BODY_H;
    private static final int LINE_H  = 4;

    private static final Color BG_BODY    = new Color(58,  36, 16);
    private static final Color BG_HEADER  = new Color(44,  26,  8);
    private static final Color OUTER_DARK = new Color(26,  14,  4);
    private static final Color TITLE_ORG  = new Color(255, 144, 16);
    private static final Color TEXT_CREAM = new Color(240, 234, 216);

    private final Client client;
    private final LootNotifierConfig config;

    private boolean active    = false;
    private long    shownAt   = 0;
    private String  itemName  = "";
    private String  titleText = "Collection log";
    private Color   accentCol = new Color(200, 168, 96);

    @Inject
    public LootNotifierOverlay(Client client, ItemManager itemManager, LootNotifierConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.TOP_CENTER);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }

    public void showNotification(String itemName, String titleText, Color accentColour)
    {
        this.itemName  = itemName;
        this.titleText = titleText;
        this.accentCol = accentColour;
        this.shownAt   = System.currentTimeMillis();
        this.active    = true;
    }

    private float ease(float t)
    {
        return t * t * (3f - 2f * t);
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        if (!active) return null;

        long elapsed = System.currentTimeMillis() - shownAt;
        if (elapsed > DISPLAY_DURATION)
        {
            active = false;
            return null;
        }

        long holdStart  = PHASE_HORIZ_IN + PHASE_VERT_IN;
        long holdEnd    = DISPLAY_DURATION - PHASE_VERT_OUT - PHASE_HORIZ_OUT;
        long vertOutEnd = DISPLAY_DURATION - PHASE_HORIZ_OUT;

        float horizScale;
        float vertScale;
        boolean showText;

        if (elapsed < PHASE_HORIZ_IN)
        {
            horizScale = ease((float) elapsed / PHASE_HORIZ_IN);
            vertScale  = 0f;
            showText   = false;
        }
        else if (elapsed < holdStart)
        {
            horizScale = 1f;
            vertScale  = ease((float)(elapsed - PHASE_HORIZ_IN) / PHASE_VERT_IN);
            showText   = vertScale > 0.7f;
        }
        else if (elapsed < holdEnd)
        {
            horizScale = 1f;
            vertScale  = 1f;
            showText   = true;
        }
        else if (elapsed < vertOutEnd)
        {
            horizScale = 1f;
            vertScale  = 1f - ease((float)(elapsed - holdEnd) / PHASE_VERT_OUT);
            showText   = vertScale > 0.3f;
        }
        else
        {
            horizScale = 1f - ease((float)(elapsed - vertOutEnd) / PHASE_HORIZ_OUT);
            vertScale  = 0f;
            showText   = false;
        }

        int drawW = Math.max(2, (int)(BOX_W * horizScale));
        int drawH = LINE_H + (int)((POPUP_H - LINE_H) * vertScale);

        int offX = (BOX_W - drawW) / 2;
        int offY = (POPUP_H - drawH) / 2;

        int fx = offX;
        int fy = offY + 8;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setClip(fx, fy, drawW, drawH);

        g2.setColor(OUTER_DARK);
        g2.fillRect(fx, fy, BOX_W, POPUP_H);

        g2.setColor(accentCol);
        g2.fillRect(fx + 4, fy + 4, BOX_W - 8, POPUP_H - 8);

        g2.setColor(BG_BODY);
        g2.fillRect(fx + 5, fy + 5, BOX_W - 10, POPUP_H - 10);

        g2.setColor(BG_HEADER);
        g2.fillRect(fx + 5, fy + 5, BOX_W - 10, HDR_H - 2);

        g2.setColor(OUTER_DARK);
        g2.fillRect(fx + 5, fy + HDR_H - 2, BOX_W - 10, 2);

        g2.setColor(accentCol);
        g2.fillRect(fx + 5, fy + HDR_H, BOX_W - 10, 1);

        if (showText)
        {
            float textAlpha = Math.min(1f, (vertScale - 0.7f) / 0.3f);
            textAlpha = Math.max(0f, textAlpha);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, textAlpha));

            g2.setFont(FontManager.getRunescapeBoldFont());
            FontMetrics fm = g2.getFontMetrics();
            int titleX = fx + (BOX_W - fm.stringWidth(titleText)) / 2;
            int titleY = fy + HDR_H - 6;
            g2.setColor(Color.BLACK);
            g2.drawString(titleText, titleX + 1, titleY + 1);
            g2.setColor(TITLE_ORG);
            g2.drawString(titleText, titleX, titleY);

            g2.setFont(FontManager.getRunescapeBoldFont());
            fm = g2.getFontMetrics();
            int itemX = fx + (BOX_W - fm.stringWidth(itemName)) / 2;
            int itemY = fy + HDR_H + 26;
            g2.setColor(Color.BLACK);
            g2.drawString(itemName, itemX + 1, itemY + 1);
            g2.setColor(TEXT_CREAM);
            g2.drawString(itemName, itemX, itemY);
        }

        g2.dispose();
        return new Dimension(BOX_W, POPUP_H + 16);
    }
}