/*
 * Copyright (c) 2018 superblaubeere27
 */

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.gui.clickgui.components;

import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.gui.clickgui.AbstractComponent;
import pw.yallxe.outrage.gui.clickgui.IRenderer;
import pw.yallxe.outrage.gui.clickgui.Window;

public class Label extends AbstractComponent {
    private @NotNull String text;

    public Label(@NotNull IRenderer renderer, @NotNull String text) {
        super(renderer);
        setText(text);
    }

    @Override
    public void render() {
        renderer.drawString(x, y, text, Window.FOREGROUND);
    }

    public @NotNull String getText() {
        return text;
    }

    public void setText(@NotNull String text) {
        setWidth(renderer.getStringWidth(text));
        setHeight(renderer.getStringHeight(text));

        this.text = text;
    }
}
