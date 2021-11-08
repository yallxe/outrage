/*
 * Copyright (c) 2018 superblaubeere27
 */

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.modules;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import pw.yallxe.outrage.ClientBase;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import pw.yallxe.outrage.modules.modules.combat.*;
import pw.yallxe.outrage.modules.modules.player.*;
import pw.yallxe.outrage.modules.modules.movement.*;
import pw.yallxe.outrage.modules.modules.render.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleManager {

    @NotNull
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        EventManager.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }


    public void addModules() {
        addModule(new SetbackDetector());
        addModule(new FullBright());
        addModule(new AntiKnockback());
        addModule(new NoRotate());
        addModule(new AutoTotem());
        addModule(new Sprint());
        addModule(new NoFall());
        addModule(new Speed());
        addModule(new Criticals());
        addModule(new KillAura());
        addModule(new PacketFly());
        addModule(new ElytraFly());
        addModule(new ProjectilesHelper());
        addModule(new CrystalAura());
        addModule(new ESP());
        addModule(new HoleESP());
        addModule(new OutlineESP());
        addModule(new LongJump());
        addModule(new Sneak());
        addModule(new HypixelPP());

        addModule(new ClickGUIModule());
        addModule(new HUD());
    }

    private void addModule(@NotNull Module module) {
        modules.add(module);
        ClientBase.INSTANCE.valueManager.registerObject(module.getName(), module);
    }

    @NotNull
    public List<Module> getModules() {
        return modules;
    }

    public @NotNull Module getModule(@NotNull String name, boolean caseSensitive) {
        return Objects.requireNonNull(modules.stream().filter(mod -> !caseSensitive && name.equalsIgnoreCase(mod.getName()) || name.equals(mod.getName())).findFirst().orElse(null));
    }

    public void dispatchKeyPress(int keyCode) {
        for (Module m : modules) {
            if (m.getKeybind() == keyCode) {
                m.setState(!m.getState());
            }
        }
    }

    @SubscribeEvent
    public void keyPress(InputEvent.@NotNull KeyInputEvent e) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world == null || mc.player == null || !Keyboard.isCreated())
            return;

        int keyCode = Keyboard.getEventKey();
        if (keyCode <= 0 || !Keyboard.getEventKeyState()) {
            return;
        }

        dispatchKeyPress(keyCode);
    }
}
