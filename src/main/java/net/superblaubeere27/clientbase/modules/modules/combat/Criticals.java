package net.superblaubeere27.clientbase.modules.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.superblaubeere27.clientbase.events.PacketEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", "what title says", ModuleCategory.COMBAT);
    }

    private final ModeValue mode = new ModeValue("Mode", "Packet", "Packet", "Jump");
    private final NumberValue<Float> packet_boost = new NumberValue<>("Packet Y", 0.0001f, 0.0001f, 1f);

    @EventTarget
    private void onPacket(PacketEvent event) {
        if (!getState()) return;

        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK) {
            if (!mc.player.isInWater() && !mc.player.isOnLadder() && mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                if (mode.getModes()[mode.getObject()].equals("Jump")) {
                    mc.player.jump();
                } else if (mode.getModes()[mode.getObject()].equals("Packet")) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + packet_boost.getObject(), mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                }
            }
        }
    }
}
