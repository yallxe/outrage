/*
 * Copyright (c) 2018 superblaubeere27
*/

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.utils.GLUtil;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPage;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPageFontRenderer;

import java.awt.*;

public class Notification {
    private static final @NotNull Minecraft mc = Minecraft.getMinecraft();
    private final @NotNull NotificationType type;
    private final @NotNull String title;
    private final @NotNull String message;
    private long start;

    private final long fadedIn;
    private final long fadeOut;
    private final long end;

    private final @NotNull GlyphPageFontRenderer renderer, rendererBold;


    public Notification(NotificationType type, String title, String message, int length) {
        this.type = type;
        this.title = title;
        this.message = message;

        fadedIn = 200L * length;
        fadeOut = fadedIn + 500L * length;
        end = fadeOut + fadedIn;

        char[] chars = new char[256];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }

        GlyphPage glyphPage = new GlyphPage(new Font("BebasNeueRegular", Font.PLAIN, 17), true, true);

        glyphPage.generateGlyphPage(chars);
        glyphPage.setupTexture();

        renderer = new GlyphPageFontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);

        GlyphPage glyphPageBold = new GlyphPage(new Font("BebasNeueRegular", Font.BOLD, 17), true, true);

        glyphPageBold.generateGlyphPage(chars);
        glyphPageBold.setupTexture();

        rendererBold = new GlyphPageFontRenderer(glyphPageBold, glyphPageBold, glyphPageBold, glyphPageBold);
    }

    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public void render() {
        ScaledResolution res = new ScaledResolution(mc);
        double offset;
        int width = 120;
        int height = 30;
        long time = getTime();

        if (time < fadedIn) {
            offset = Math.tanh(time / (double) (fadedIn) * 3.0) * width;
        } else if (time > fadeOut) {
            offset = (Math.tanh(3.0 - (time - fadeOut) / (double) (end - fadeOut) * 3.0) * width);
        } else {
            offset = width;
        }

        Color color = new Color(0, 0, 0, 90);
        Color color1;

        if (type == NotificationType.INFO)
            color1 = new Color(120, 183, 187);
        else if (type == NotificationType.WARNING)
            color1 = new Color(255, 196, 77);
        else {
            color1 = new Color(239, 108, 87);
        }

        GLUtil.drawRect(res.getScaledWidth() - offset, res.getScaledHeight() - 5 - height, res.getScaledWidth(), res.getScaledHeight() - 5, color.getRGB());
        GLUtil.drawRect(res.getScaledWidth() - offset, res.getScaledHeight() - 5 - height, res.getScaledWidth() - offset + 4, res.getScaledHeight() - 5, color1.getRGB());

        rendererBold.drawString(title, (int) (res.getScaledWidth() - offset + 8), res.getScaledHeight() - 2 - height, -1, false);
        renderer.drawString(message, (int) (res.getScaledWidth() - offset + 8), res.getScaledHeight() - 20, -1, false);
    }


}
