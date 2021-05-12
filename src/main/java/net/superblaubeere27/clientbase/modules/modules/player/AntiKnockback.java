package net.superblaubeere27.clientbase.modules.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.superblaubeere27.clientbase.events.PacketEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;

public class AntiKnockback extends Module {
    public AntiKnockback() {
        super("AntiKnockback", "Prevents you from taking knockback", ModuleCategory.PLAYER);
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
        if (!getState()) return;

        if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion)
            event.setCancelled(true);
    }
}
