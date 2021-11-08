package pw.yallxe.outrage.utils;

import net.minecraft.client.Minecraft;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPage;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPageFontRenderer;

import java.awt.*;

public class UIUtils {
    public Minecraft mc = Minecraft.getMinecraft();
    private final GlyphPageFontRenderer renderer;

    public UIUtils(String font_name, int size) {
        char[] chars = new char[256];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }

        GlyphPage glyphPage = new GlyphPage(new Font(font_name, Font.PLAIN, size), true, true);

        glyphPage.generateGlyphPage(chars);
        glyphPage.setupTexture();

        renderer = new GlyphPageFontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);
    }

    public void drawText(String text, int x, int y, int color) {
        renderer.drawString(text, x, y, color, false);
    }

    public void drawText(String text, int x, int y, int color, boolean shadow) {
        renderer.drawString(text, x, y, color, shadow);
    }

    public void drawText(String text, int x, int y, Color color, boolean shadow) {
        renderer.drawString(text, x, y, color.getRGB(), shadow);
    }

    public int getFontHeight() {
        return renderer.getFontHeight();
    }

    public int getStringWidth(String text) {
        return renderer.getStringWidth(text);
    }
}
