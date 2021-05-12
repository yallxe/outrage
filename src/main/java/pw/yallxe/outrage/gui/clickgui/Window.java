/*
 * Copyright 2019 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pw.yallxe.outrage.gui.clickgui;


import pw.yallxe.outrage.gui.clickgui.components.Pane;

import java.awt.*;

public class Window {
    /* Color constants */

    public static final int SECONDARY_FOREGROUND_HEX = 0xffffc34d;
    public static final Color TERTIARY_FOREGROUND = new Color(20, 20, 20, 150);
    public static final Color SECONDARY_OUTLINE = new Color(10, 10, 10, 255);
    public static final Color BACKGROUND = new Color(40, 40, 40, 255);
    public static final Color FOREGROUND = Color.white;

    private final String title;
    private int x;
    private int y;
    private int width;
    private int height;

    private int headerHeight;

    private boolean beingDragged;
    private int dragX;
    private int dragY;

    public Pane contentPane;

    public Window(String title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(IRenderer renderer) {
        int fontHeight = renderer.getStringHeight(title);
        int headerFontOffset = fontHeight / 4;

        headerHeight = headerFontOffset * 2 + fontHeight;

        renderer.drawRect(x - 2, y - 2, width + 4, height + 4, SECONDARY_FOREGROUND_HEX);
        renderer.drawRect(x, y, width, height, new Color(40, 40, 40, 255));
        renderer.drawRect(x, y, width, headerHeight, new Color(29, 29, 29, 255));

        renderer.drawString(x + 5, y + 5, title, FOREGROUND);

        if (contentPane != null) {
            if (contentPane.isSizeChanged()) {
                contentPane.setSizeChanged(false);
            }

            contentPane.setX(x);
            contentPane.setY(y + headerHeight);
            contentPane.setWidth(width);
            contentPane.setHeight(height - headerHeight);

            contentPane.render();
        }
    }


    public void mousePressed(int button, int x, int y) {
        if (this.contentPane != null) contentPane.mousePressed(button, x, y, false);

        if (button == 0) {
            if (x >= this.x && y >= this.y && x <= this.x + this.width && y <= this.y + this.headerHeight) {
                beingDragged = true;

                dragX = this.x - x;
                dragY = this.y - y;

//                drag(x, y);
            }
        }
    }

    private void drag(int mouseX, int mouseY) {
        if (beingDragged) {
            this.x = mouseX + dragX;
            this.y = mouseY + dragY;
        }
    }

    public void mouseReleased(int button, int x, int y) {
        if (this.contentPane != null) contentPane.mouseReleased(button, x, y, false);

        if (button == 0) {
            beingDragged = false;
        }
    }

    public void mouseMoved(int x, int y) {
        if (this.contentPane != null) contentPane.mouseMove(x, y, false);

        drag(x, y);
    }

    public Pane getContentPane() {
        return contentPane;
    }

    public void setContentPane(Pane contentPane) {
        this.contentPane = contentPane;
    }

    public void keyPressed(int key, char c) {
        if (this.contentPane != null) contentPane.keyPressed(key, c);
    }

    public void mouseWheel(int change) {
        if (this.contentPane != null) contentPane.mouseWheel(change);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
