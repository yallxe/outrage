/*
 * Copyright (c) 2021 yallxe
 */
package pw.yallxe.outrage.modules.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.yallxe.outrage.events.GameTickEvent;
import pw.yallxe.outrage.events.PacketEvent;
import pw.yallxe.outrage.events.Render2DEvent;
import pw.yallxe.outrage.events.Render3DEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.utils.RendererUtils;
import pw.yallxe.outrage.utils.RotationUtils;
import pw.yallxe.outrage.utils.Timer;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPage;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPageFontRenderer;
import pw.yallxe.outrage.valuesystem.BooleanValue;
import pw.yallxe.outrage.valuesystem.ModeValue;
import pw.yallxe.outrage.valuesystem.NumberValue;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAura extends Module {
    private final @NotNull Timer timer;

    private static @Nullable EntityLivingBase target = null;

    private final @NotNull NumberValue<Integer> apsValue = new NumberValue<>("APS", 12, 1, 20);
    private final @NotNull NumberValue<Float> rangeValue = new NumberValue<>("Range", 6f, 0f, 8f);
    private final @NotNull NumberValue<Float> fovValue = new NumberValue<>("FOV", 180f, 1f, 360f);
    private final @NotNull ModeValue rotationMode = new ModeValue("Rotation", "Spoof", "Client", "Spoof");
    private final @NotNull ModeValue rotateOn = new ModeValue("Rotate on", "Target", "Target", "Hit");
    private final @NotNull ModeValue cooldownMode = new ModeValue("Cooldown", "Default", "Default", "APS");
    private final @NotNull BooleanValue targetESP = new BooleanValue("TargetESP", true);
    private final @NotNull BooleanValue targetHud = new BooleanValue("TargetHUD", true);
    private final @NotNull NumberValue<Integer> targetHudX = new NumberValue<>("TargetHUD X", 10, -250, 250);
    private final @NotNull NumberValue<Integer> targetHudY = new NumberValue<>("TargetHUD Y", 10, -250, 250);

    private final @NotNull GlyphPageFontRenderer renderer, rendererBold;

    private final @NotNull BooleanValue playersBool = new BooleanValue("Players", true);
    private final @NotNull BooleanValue mobsBool = new BooleanValue("Mobs", true);

    private long last_player_packet = -1;
    private float iYaw = -1;
    private float iPitch = -1;
    private boolean setNormalAngle = false;
    float @Nullable [] rotations;

    boolean canHit;

    private boolean doInteractBoolean(@NotNull Entity e) {
        if (RotationUtils.getRotationDifference(e) > fovValue.getObject()) return false;
        if (e instanceof EntityPlayer && playersBool.getObject()) {
            return true;
        } else return !(e instanceof EntityPlayer) && mobsBool.getObject();
    }

    public KillAura() {
        super("KillAura", "$$$", ModuleCategory.COMBAT);

        timer = new Timer();

        char[] chars = new char[256];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }

        GlyphPage glyphPage = new GlyphPage(new Font("BebasNeueRegular", Font.PLAIN, 17), true, true);

        glyphPage.generateGlyphPage(chars);
        glyphPage.setupTexture();

        renderer = new GlyphPageFontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);

        GlyphPage glyphPageBold = new GlyphPage(new Font("BebasNeueRegular", Font.BOLD, 17), true, true);

        glyphPageBold.generateGlyphPage(chars);
        glyphPageBold.setupTexture();

        rendererBold = new GlyphPageFontRenderer(glyphPageBold, glyphPageBold, glyphPageBold, glyphPageBold);
    }

    private void updateTarget() {
        List<EntityLivingBase> targets = mc.world.loadedEntityList.stream()
                .filter(EntityLivingBase.class::isInstance)
                .map(EntityLivingBase.class::cast)
                .collect(Collectors.toList());

        targets = targets.stream().filter(entity -> entity.getDistance(mc.player) < rangeValue.getObject() && entity != mc.player && !entity.isDead && entity.getHealth() > 0).collect(Collectors.toList());
        targets.sort(Comparator.comparingDouble(entity -> entity.getDistance(mc.player)));
        // targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());

        if (!targets.isEmpty() && doInteractBoolean(targets.get(0))) {
            target = targets.get(0);
        } else target = null;
    }

    @EventTarget
    private void tick(@NotNull GameTickEvent event) {
        if (!getState()) return;

        if (setNormalAngle) {
            mc.player.rotationYaw = iYaw;
            mc.player.rotationPitch = iPitch;
            setNormalAngle = false;
        } else {
            iYaw = mc.player.rotationYaw;
            iPitch = mc.player.rotationPitch;
        }

        updateTarget();

        if (target == null) return;


        if (cooldownMode.getModes()[cooldownMode.getObject()].equals("APS")) {
            canHit = timer.hasTimeElapsed(1000 / apsValue.getObject(), true);
        } else {
            canHit = (int)mc.player.getCooledAttackStrength(1) == 1;
        }

        if (rotateOn.getModes()[rotateOn.getObject()].equals("Target")) {
            handle_rotation();
        }

        if (canHit) {
            if (rotateOn.getModes()[rotateOn.getObject()].equals("Hit")) {
                handle_rotation();
            }
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    private void handle_rotation() {
        if (rotationMode.getModes()[rotationMode.getObject()].equals("Spoof")) {
            final float[] rotations = RotationUtils.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), target.getPositionEyes(mc.getRenderPartialTicks()));
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
        }
    }

    @EventTarget
    private void render2d(@NotNull Render2DEvent event) {
        if (target != null) {
            if (rotationMode.getModes()[rotationMode.getObject()].equals("Client")) {
                rotations = RotationUtils.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), target.getPositionEyes(mc.getRenderPartialTicks()));
                iYaw = mc.player.rotationYaw;
                iPitch = mc.player.rotationPitch;
                mc.player.rotationYaw = rotations[0];
                mc.player.rotationPitch = rotations[1];
                setNormalAngle = true;
            }
        }

        if (target != null && targetHud.getObject()) {
            GL11.glPushMatrix();
            ScaledResolution sr = new ScaledResolution(mc);

            Gui.drawRect(sr.getScaledWidth() / 2 + 5 + targetHudX.getObject(), sr.getScaledHeight() / 2 + 5 + targetHudY.getObject(), sr.getScaledWidth() / 2 + 150 + targetHudX.getObject(), sr.getScaledHeight() / 2 + 65 + targetHudY.getObject(), new Color(0, 0, 0, 135).getRGB());

            Gui.drawRect(sr.getScaledWidth() / 2 + 5 + targetHudX.getObject(), sr.getScaledHeight() / 2 + 5 + targetHudY.getObject(), sr.getScaledWidth() / 2 + 150 + targetHudX.getObject(), sr.getScaledHeight() / 2 + 8 + targetHudY.getObject(), new Color(255, 196, 77).getRGB());

            rendererBold.drawString(target.getName() + " - " + (target.getHealth() > mc.player.getHealth() ? "??cLosing??r" : "??aWinning??r"), (sr.getScaledWidth() / 2f) + 10 + targetHudX.getObject(), (sr.getScaledHeight() / 2f) + 10 + targetHudY.getObject(), -1, true);
            renderer.drawString(target.onGround ? "On ground" : "Off ground", (sr.getScaledWidth() / 2f) + 10 + targetHudX.getObject(), (sr.getScaledHeight() / 2f) + 12 + renderer.getFontHeight() + targetHudY.getObject(), -1, true);

            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);

            renderer.drawString(df.format(target.getHealth()) + "/" + df.format(target.getMaxHealth()) + " HP", (sr.getScaledWidth() / 2f) + 10 + targetHudX.getObject(), (sr.getScaledHeight() / 2f) + 25 + targetHudY.getObject() + renderer.getFontHeight(), -1, true);
            renderer.drawString("Hurt time: " + target.hurtTime, (sr.getScaledWidth() / 2f) + 10 + targetHudX.getObject(), (sr.getScaledHeight() / 2f) + 38 + renderer.getFontHeight() + targetHudY.getObject(), -1, true);
            GL11.glPopMatrix();
        }
    }

    private boolean handle_delta(@NotNull PacketEvent event) {
        long delta = System.currentTimeMillis() - last_player_packet;
        if (last_player_packet == -1) {
            last_player_packet = System.currentTimeMillis();
        } else if (delta < 50) {
            System.out.println(delta);
            event.setCancelled(true);
            return true;
        }
        last_player_packet = System.currentTimeMillis();
        return false;
    }

    @EventTarget
    private void render3d(@NotNull Render3DEvent event) {
        if (target != null && targetESP.getObject()) {
            RendererUtils.drawEntityESP(target, new Color(255, 0, 0, 30));
        }
    }
}
