/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.superblaubeere27.clientbase.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.superblaubeere27.clientbase.ClientBase;
import net.superblaubeere27.clientbase.events.Render2DEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.notifications.NotificationManager;
import net.superblaubeere27.clientbase.utils.GLUtil;
import net.superblaubeere27.clientbase.utils.fontRenderer.GlyphPage;
import net.superblaubeere27.clientbase.utils.fontRenderer.GlyphPageFontRenderer;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HUD extends Module {
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    private final GlyphPageFontRenderer renderer;

    public HUD() {
        super("HUD", "The Overlay", ModuleCategory.RENDER);
        setState(true);

        char[] chars = new char[256];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }

        GlyphPage glyphPage = new GlyphPage(new Font("BebasNeueRegular", Font.PLAIN, 17), true, true);

        glyphPage.generateGlyphPage(chars);
        glyphPage.setupTexture();

        renderer = new GlyphPageFontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);

        HashMap<ModuleCategory, java.util.List<Module>> moduleCategoryMap = new HashMap<>();

        for (Module module : ClientBase.INSTANCE.moduleManager.getModules()) {
            if (!moduleCategoryMap.containsKey(module.getCategory())) {
                moduleCategoryMap.put(module.getCategory(), new ArrayList<>());
            }

            moduleCategoryMap.get(module.getCategory()).add(module);
        }

    }

    private static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.8f).getRGB();
    }

    @EventTarget
    public void render2D(Render2DEvent event) {
        if (!getState()) return;
        FontRenderer fontRenderer = mc.fontRenderer;

        ScaledResolution res = new ScaledResolution(mc);

        LocalDateTime now = LocalDateTime.now();
        String time = timeFormat.format(now);

        GlStateManager.pushMatrix();

        renderer.drawString("§6" + ClientBase.CLIENT_INITIALS + "§r" + ClientBase.CLIENT_NAME.substring(1) + " b" + ClientBase.CLIENT_VERSION + " §7(" + time + ")", 2, 2, -1, true);

        if (!(mc.currentScreen instanceof GuiChat)) {
            double currSpeed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ) * 20;

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);

            renderer.drawString("FPS: " + Minecraft.getDebugFPS(), 2, res.getScaledHeight() - renderer.getFontHeight() * 2 - 2, -1, true);
            renderer.drawString(df.format(currSpeed) + " blocks/second", 2, res.getScaledHeight() - renderer.getFontHeight() - 2, -1, true);
        }

        AtomicInteger offset = new AtomicInteger(0);
        AtomicInteger index = new AtomicInteger();

        AtomicReference<String> lastModuleName = new AtomicReference<>("");

        ClientBase.INSTANCE.moduleManager.getModules().stream().filter(mod -> mod.getState() && !mod.isHidden()).sorted(Comparator.comparingInt(mod -> -renderer.getStringWidth(mod.getName()))).forEach(mod -> {
            Gui.drawRect(res.getScaledWidth() - renderer.getStringWidth(mod.getName()) - 4, offset.get(), res.getScaledWidth(), renderer.getFontHeight() + offset.get(), new Color(0, 0, 0, 50).getRGB());
            renderer.drawString(mod.getName(), res.getScaledWidth() - renderer.getStringWidth(mod.getName()) - 3, offset.get(), -1, true);

            if (index.get() == 0) {
                Gui.drawRect(res.getScaledWidth() - renderer.getStringWidth(mod.getName()) - 4, 0, res.getScaledWidth() - renderer.getStringWidth(mod.getName()) - 6, 2 + renderer.getFontHeight() + offset.get(), 0xffffc34d);
            } else {
                Gui.drawRect(res.getScaledWidth() - renderer.getStringWidth(mod.getName()) - 4, offset.get(), res.getScaledWidth() - renderer.getStringWidth(mod.getName()) - 6, 2 + renderer.getFontHeight() + offset.get(), 0xffffc34d);
                Gui.drawRect(res.getScaledWidth() - renderer.getStringWidth(lastModuleName.get()) - 4, offset.get(), res.getScaledWidth() - renderer.getStringWidth(mod.getName()) - 4, 2 + offset.get(), 0xffffc34d);
            }

            lastModuleName.set(mod.getName());

            offset.addAndGet(fontRenderer.FONT_HEIGHT + 2);
            index.getAndIncrement();
        });

        Gui.drawRect(res.getScaledWidth() - renderer.getStringWidth(lastModuleName.get()) - 4, offset.get(), res.getScaledWidth(),  2 + offset.get(), 0xffffc34d);

        NotificationManager.render();

        GlStateManager.popMatrix();
    }
}
