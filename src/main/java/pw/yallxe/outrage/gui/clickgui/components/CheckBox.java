/*
 * Copyright (c) 2018 superblaubeere27
 */

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.gui.clickgui.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.yallxe.outrage.gui.clickgui.AbstractComponent;
import pw.yallxe.outrage.gui.clickgui.IRenderer;
import pw.yallxe.outrage.gui.clickgui.Window;

public class CheckBox extends AbstractComponent {
    private static final int PREFERRED_HEIGHT = 22;

    private boolean selected;
    private @Nullable String title;
    private final int preferredHeight;
    private boolean hovered;
    private @Nullable ValueChangeListener<Boolean> listener;

    public CheckBox(@NotNull IRenderer renderer, @NotNull String title, int preferredHeight) {
        super(renderer);

        this.preferredHeight = preferredHeight;

        setTitle(title);
    }

    public CheckBox(@NotNull IRenderer renderer, @NotNull String title) {
        this(renderer, title, PREFERRED_HEIGHT);
    }

    @Override
    public void render() {
        renderer.drawRect(x, y, preferredHeight, preferredHeight, hovered ? pw.yallxe.outrage.gui.clickgui.Window.SECONDARY_FOREGROUND_HEX : pw.yallxe.outrage.gui.clickgui.Window.TERTIARY_FOREGROUND.getRGB());

        if (selected) {
            int color = hovered ? pw.yallxe.outrage.gui.clickgui.Window.TERTIARY_FOREGROUND.getRGB() : pw.yallxe.outrage.gui.clickgui.Window.SECONDARY_FOREGROUND_HEX;

            renderer.drawRect(x + 2, y + 3, preferredHeight - 5, preferredHeight - 5, color);
        }

        renderer.drawOutline(x, y, preferredHeight, preferredHeight, 1.0f, hovered ? pw.yallxe.outrage.gui.clickgui.Window.SECONDARY_OUTLINE.getRGB() : pw.yallxe.outrage.gui.clickgui.Window.SECONDARY_FOREGROUND_HEX);

        renderer.drawString(x + preferredHeight + preferredHeight / 4, y + renderer.getStringHeight(title) / 4 - 4, title, Window.FOREGROUND);
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);

        return false;
    }

    private void updateHovered(int x, int y, boolean offscreen) {
        hovered = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + getHeight();
    }

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        if (button == 0) {
            updateHovered(x, y, offscreen);

            if (hovered) {

                boolean newVal = !selected;
                boolean change = true;

                if (listener != null) {
                    change = listener.onValueChange(newVal);
                }

                if (change) selected = newVal;

                return true;
            }
        }

        return false;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;

        setWidth(renderer.getStringWidth(title) + preferredHeight + preferredHeight / 4);
        setHeight(preferredHeight);
    }

    public void setListener(@NotNull ValueChangeListener<Boolean> listener) {
        this.listener = listener;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
