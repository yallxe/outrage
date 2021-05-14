package pw.yallxe.outrage.gui.clickgui;

import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.utils.GLUtil;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPageFontRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL41.glClearDepthf;

public class ClientBaseRendererImpl implements IRenderer {
    private final @NotNull GlyphPageFontRenderer renderer;

    public ClientBaseRendererImpl(@NotNull GlyphPageFontRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void drawRect(double x, double y, double w, double h, @NotNull Color c) {
        GLUtil.drawRect(GL11.GL_QUADS, x / 2.0, y / 2.0, x / 2.0 + w / 2.0, y / 2.0 + h / 2.0, GLUtil.toRGBA(c));
    }

    @Override
    public void drawRect(double x, double y, double w, double h, int c) {
        GLUtil.drawRect(GL11.GL_QUADS, x / 2.0, y / 2.0, x / 2.0 + w / 2.0, y / 2.0 + h / 2.0, c);
    }

    @Override
    public void drawOutline(double x, double y, double w, double h, float lineWidth, int c) {
        GL11.glLineWidth(lineWidth);
        GLUtil.drawRect(GL11.GL_LINE_LOOP, x / 2.0, y / 2.0, x / 2.0 + w / 2.0, y / 2.0 + h / 2.0, c);
    }

    @Override
    public void setColor(@NotNull Color c) {
        GLUtil.setColor(c);
    }

    @Override
    public void drawString(int x, int y, @NotNull String text, @NotNull Color color) {
        renderer.drawString(text, x / 2f, y / 2f, GLUtil.toRGBA(color), false);
    }

    @Override
    public int getStringWidth(@NotNull String str) {
        return renderer.getStringWidth(str) * 2;
    }

    @Override
    public int getStringHeight(@NotNull String str) {
        return renderer.getFontHeight() * 2;
    }
}
