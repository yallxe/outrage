/*
 * Copyright (c) 2018 superblaubeere27
 */

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.gui.clickgui.layout;

import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.gui.clickgui.AbstractComponent;

import java.util.Map;

public class Layout {
    private @NotNull Map<AbstractComponent, int[]> componentLocations;
    private int maxHeight;
    private int maxWidth;

    Layout(@NotNull Map<AbstractComponent, int[]> componentLocations, int maxHeight, int maxWidth) {
        this.componentLocations = componentLocations;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }

    public @NotNull Map<AbstractComponent, int[]> getComponentLocations() {
        return componentLocations;
    }

    public void setComponentLocations(@NotNull Map<AbstractComponent, int[]> componentLocations) {
        this.componentLocations = componentLocations;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }
}
