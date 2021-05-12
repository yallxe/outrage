/*
 * Copyright 2019 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pw.yallxe.outrage.gui.clickgui.components;

import pw.yallxe.outrage.gui.clickgui.AbstractComponent;
import pw.yallxe.outrage.gui.clickgui.IRenderer;
import pw.yallxe.outrage.gui.clickgui.Window;

public class CheckBox extends AbstractComponent {
    private static final int PREFERRED_HEIGHT = 22;

    private boolean selected;
    private String title;
    private final int preferredHeight;
    private boolean hovered;
    private ValueChangeListener<Boolean> listener;

    public CheckBox(IRenderer renderer, String title, int preferredHeight) {
        super(renderer);

        this.preferredHeight = preferredHeight;

        setTitle(title);
    }

    public CheckBox(IRenderer renderer, String title) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

        setWidth(renderer.getStringWidth(title) + preferredHeight + preferredHeight / 4);
        setHeight(preferredHeight);
    }

    public void setListener(ValueChangeListener<Boolean> listener) {
        this.listener = listener;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
