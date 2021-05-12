/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

        addModule(new ClickGUIModule());
        addModule(new HUD());
    }

    private void addModule(@NotNull Module module) {
        modules.add(module);
        if (module instanceof HUD) {
            MinecraftForge.EVENT_BUS.register(module);
        }
        ClientBase.INSTANCE.valueManager.registerObject(module.getName(), module);
    }

    @NotNull
    public List<Module> getModules() {
        return modules;
    }

    @NotNull
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) Objects.requireNonNull(modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null));
    }

    public Module getModule(@NotNull String name, boolean caseSensitive) {
        return modules.stream().filter(mod -> !caseSensitive && name.equalsIgnoreCase(mod.getName()) || name.equals(mod.getName())).findFirst().orElse(null);
    }

    public void dispatchKeyPress(int keyCode) {
        for (Module m : modules) {
            if (m.getKeybind() == keyCode) {
                m.setState(!m.getState());
            }
        }
    }

    @SubscribeEvent
    public void keyPress(InputEvent.KeyInputEvent e) {
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
