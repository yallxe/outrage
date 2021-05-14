/*
 * Copyright (c) 2018 superblaubeere27
 */

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.modules;

import org.jetbrains.annotations.NotNull;

public enum ModuleCategory {
    RENDER("Render"), MOVEMENT("Movement"), COMBAT("Combat"), MISC("Misc"), PLAYER("Player"), WORLD("World"), FUN("Fun");

    private @NotNull String name;

    ModuleCategory(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull String toString() {
        return name;
    }
}
