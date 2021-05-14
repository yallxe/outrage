/*
 * Copyright (c) 2018 superblaubeere27
 */

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.ClientBase;
import pw.yallxe.outrage.events.Render2DEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.notifications.NotificationManager;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPage;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPageFontRenderer;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HUD extends Module {
    private static final @NotNull DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    private final @NotNull GlyphPageFontRenderer renderer;

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
    public void render2D(@NotNull Render2DEvent event) {
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
