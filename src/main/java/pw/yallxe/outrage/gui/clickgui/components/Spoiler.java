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
import pw.yallxe.outrage.modules.Module;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Spoiler extends AbstractComponent {
    private static final int PREFERRED_HEIGHT = 28;
    public final int preferredWidth;
    private @Nullable String title;
    private final int preferredHeight;
    private boolean hovered;
    private final @NotNull Pane contentPane;
    private boolean opened = false;

    // XXX This kills off any use case of the spoiler except for modules
    private final @NotNull Module attachedModule;
    private boolean isBinding;
    public boolean showKeyBinds;

    public Spoiler(@NotNull IRenderer renderer, @NotNull String title, int preferredWidth, int preferredHeight, @NotNull Pane contentPane, @NotNull Module attachedModule) {
        super(renderer);

        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
        this.contentPane = contentPane;
        this.attachedModule = attachedModule;

        setTitle(title);
    }

    public Spoiler(@NotNull IRenderer renderer, @NotNull String title, int preferredWidth, @NotNull Pane contentPane, @NotNull Module attachedModule) {
        this(renderer, title, preferredWidth, PREFERRED_HEIGHT, contentPane, attachedModule);
    }

    @Override
    public void render() {
        boolean moduleEnabled = attachedModule.getState();

        if (moduleEnabled)  {
            renderer.drawRect(x, y, getWidth(), preferredHeight, pw.yallxe.outrage.gui.clickgui.Window.SECONDARY_FOREGROUND_HEX);
        } else if (hovered) {
            renderer.drawRect(x, y, getWidth(), preferredHeight, pw.yallxe.outrage.gui.clickgui.Window.TERTIARY_FOREGROUND);
        }

        renderer.drawString(x + 3, y + 3, title, pw.yallxe.outrage.gui.clickgui.Window.FOREGROUND);

        int windowWidth = 230;

        if (opened) {
            updateBounds();

            contentPane.setX(getX());
            contentPane.setY(getY() + preferredHeight);

            contentPane.render();

            renderer.drawString(x + windowWidth - 20, y, "˄", pw.yallxe.outrage.gui.clickgui.Window.FOREGROUND);
        } else {
            if (!contentPane.components.isEmpty()) {
                renderer.drawString(x + windowWidth - 20, y, "˅", pw.yallxe.outrage.gui.clickgui.Window.FOREGROUND);
            }
        }

        if (isBinding) {
            renderer.drawRect(x, y, getWidth(), preferredHeight, Window.BACKGROUND);
            renderer.drawString(x + 3, y + 3, "Listening...", new Color(255, 255, 255));
        }

        if (showKeyBinds) {
            String keyName = Keyboard.getKeyName(attachedModule.getKeybind());

            int keyBindOffset = contentPane.components.isEmpty() ? windowWidth - 20 - renderer.getStringWidth(keyName) : windowWidth - 35 - renderer.getStringWidth(keyName);

            if (!keyName.equals("NONE")) {
                renderer.drawString(x + keyBindOffset, y, (attachedModule.getState() ? "" : "§7") + "[" + keyName + "]", new Color(255, 255, 255));
            }
        }
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);

        return opened && contentPane.mouseMove(x, y, offscreen);
    }

    private void updateHovered(int x, int y, boolean offscreen) {
        hovered = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + preferredHeight;
    }

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);

        if (button == 1 && !isBinding) {
            if (hovered && !contentPane.components.isEmpty()) {
                opened = !opened;

                contentPane.updateLayout();

                updateBounds();
                return true;
            }
        } else if (button == 0 && hovered && !isBinding) {
            attachedModule.setState(!attachedModule.getState());
            return true;
        } else if (button == 2 && hovered) {
            isBinding = !isBinding;
        }

        return opened && contentPane.mousePressed(button, x, y, offscreen);
    }

    public @Nullable String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;

        updateBounds();
    }

    private void updateBounds() {
        setWidth(preferredWidth);
        setHeight(Math.max(renderer.getStringHeight(getTitle()) * 5 / 4, preferredHeight) + (opened ? contentPane.getHeight() : 0));
    }

    @Override
    public boolean mouseReleased(int button, int x, int y, boolean offscreen) {
        return opened && contentPane.mouseReleased(button, x, y, offscreen);
    }

    @Override
    public boolean mouseWheel(int change) {
        return opened && contentPane.mouseWheel(change);
    }

    @Override
    public boolean keyPressed(int key, char c) {
        if (hovered && isBinding) {

            if (key == Keyboard.KEY_SPACE) {
                attachedModule.setKeybind(Keyboard.KEY_NONE);
            } else {
                attachedModule.setKeybind(key);
            }

            isBinding = false;
        }

        return opened && contentPane.keyPressed(key, c);
    }
}
