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
import pw.yallxe.outrage.utils.UIUtils;
import pw.yallxe.outrage.valuesystem.ModeValue;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HUD extends Module {
    private static final @NotNull DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    private final @NotNull UIUtils ui = new UIUtils("BebasNeueRegular", 17);
    private final @NotNull ModeValue colorMode = new ModeValue("Color", "Outrage", "Outrage", "Rainbow");

    public HUD() {
        super("HUD", "The Overlay", ModuleCategory.RENDER);
        setState(true);

        HashMap<ModuleCategory, java.util.List<Module>> moduleCategoryMap = new HashMap<>();

        for (Module module : ClientBase.INSTANCE.moduleManager.getModules()) {
            if (!moduleCategoryMap.containsKey(module.getCategory())) {
                moduleCategoryMap.put(module.getCategory(), new ArrayList<>());
            }

            moduleCategoryMap.get(module.getCategory()).add(module);
        }

    }

    private static int rainbow() {
        double rainbowState = Math.ceil((System.currentTimeMillis() + 0) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.8f).getRGB();
    }

    private int getHUDColor() {
        if (Objects.equals(colorMode.getMode(), "Rainbow")) {
            return rainbow();
        }
        return 0xffffc34d;
    }

    @EventTarget
    public void render2D(@NotNull Render2DEvent event) {
        if (!getState()) return;
        FontRenderer fontRenderer = mc.fontRenderer;

        ScaledResolution res = new ScaledResolution(mc);

        LocalDateTime now = LocalDateTime.now();
        String time = timeFormat.format(now);

        GlStateManager.pushMatrix();

        ui.drawText("§6" + ClientBase.CLIENT_INITIALS + "§r" + ClientBase.CLIENT_NAME.substring(1) + " b" + ClientBase.CLIENT_VERSION + " §7(" + time + ")", 2, 2, -1, true);

        if (!(mc.currentScreen instanceof GuiChat)) {
            double currSpeed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ) * 20;

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);

            ui.drawText("FPS: " + Minecraft.getDebugFPS(), 2, res.getScaledHeight() - ui.getFontHeight() * 2 - 2, -1);
            ui.drawText(df.format(currSpeed) + " blocks/second", 2, res.getScaledHeight() - ui.getFontHeight() - 2, -1);
        }

        AtomicInteger offset = new AtomicInteger(0);
        AtomicInteger index = new AtomicInteger();

        AtomicReference<String> lastModuleName = new AtomicReference<>("");

        ClientBase.INSTANCE.moduleManager.getModules().stream().filter(mod -> mod.getState() && !mod.isHidden()).sorted(Comparator.comparingInt(mod -> -ui.getStringWidth(mod.getName()))).forEach(mod -> {
            String modName = mod.getName();
            if (mod.getHudStatus() != null && !Objects.equals(mod.getHudStatus(), "")) {
                modName += " §7" + mod.getHudStatus();
            }
            Gui.drawRect(res.getScaledWidth() - ui.getStringWidth(modName) - 4, offset.get(), res.getScaledWidth(), ui.getFontHeight() + offset.get(), new Color(0, 0, 0, 50).getRGB());
            ui.drawText(modName, res.getScaledWidth() - ui.getStringWidth(modName) - 3, offset.get(), -1, true);

            if (index.get() == 0) {
                Gui.drawRect(res.getScaledWidth() - ui.getStringWidth(mod.getName()) - 4, 0, res.getScaledWidth() - ui.getStringWidth(mod.getName()) - 6, 2 + ui.getFontHeight() + offset.get(), getHUDColor());
            } else {
                Gui.drawRect(res.getScaledWidth() - ui.getStringWidth(mod.getName()) - 4, offset.get(), res.getScaledWidth() - ui.getStringWidth(mod.getName()) - 6, 2 + ui.getFontHeight() + offset.get(), getHUDColor());
                Gui.drawRect(res.getScaledWidth() - ui.getStringWidth(lastModuleName.get()) - 4, offset.get(), res.getScaledWidth() - ui.getStringWidth(mod.getName()) - 4, 2 + offset.get(), getHUDColor());
            }

            lastModuleName.set(mod.getName());

            offset.addAndGet(fontRenderer.FONT_HEIGHT + 2);
            index.getAndIncrement();
        });

        Gui.drawRect(res.getScaledWidth() - ui.getStringWidth(lastModuleName.get()) - 4, offset.get(), res.getScaledWidth(),  2 + offset.get(), getHUDColor());

        NotificationManager.render();

        GlStateManager.popMatrix();
    }
}
