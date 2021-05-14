/*
 * Copyright (c) 2018 superblaubeere27
 */

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.gui.clickgui.components;

import org.jetbrains.annotations.NotNull;

public interface ValueChangeListener<T> {

    boolean onValueChange(@NotNull T newValue);

}
