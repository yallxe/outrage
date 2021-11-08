/*
 * Copyright (c) 2018 superblaubeere27
 */

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.modules;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

public abstract class Module {
    protected static final @NotNull Minecraft mc = Minecraft.getMinecraft();
    private final @NotNull String name;
    private final @NotNull String description;
    private final @NotNull ModuleCategory category;
    private final boolean canBeEnabled;
    private final boolean hidden;
    private int keybind;
    private boolean state;
    private String hud_status = null;

    protected Module(@NotNull String name, @NotNull String description, @NotNull ModuleCategory moduleCategory) {
        this(name, description, moduleCategory, true, false, Keyboard.KEY_NONE);
    }

    protected Module(@NotNull String name, @NotNull String description, @NotNull ModuleCategory category, boolean canBeEnabled, boolean hidden, int keybind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.canBeEnabled = canBeEnabled;
        this.hidden = hidden;
        this.keybind = keybind;
    }

    public void setHudStatus(String value) {
        hud_status = value;
    }

    public String getHudStatus() {
        return hud_status;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public @NotNull ModuleCategory getCategory() {
        return category;
    }

    public boolean isCanBeEnabled() {
        return canBeEnabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        if (state) {
            this.state = true;
            EventManager.register(this);
            onEnable();

        } else {
            this.state = false;
            onDisable();
            EventManager.unregister(this);
        }
    }

    protected void onEnable() {

    }

    protected void onDisable() {

    }
}
