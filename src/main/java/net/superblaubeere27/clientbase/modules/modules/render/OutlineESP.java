package net.superblaubeere27.clientbase.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.superblaubeere27.clientbase.events.Render3DEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.EntityUtils;
import net.superblaubeere27.clientbase.valuesystem.BooleanValue;

import java.util.List;
import java.util.stream.Collectors;

public class OutlineESP extends Module {
    public OutlineESP() {
        super("OutlineESP", "Glowing outline esp", ModuleCategory.RENDER);
    }

    private final BooleanValue playersBool = new BooleanValue("Players", true);
    private final BooleanValue mobsBool = new BooleanValue("Mobs", true);

    private boolean doRenderBoolean(Entity e) {
        if (e instanceof EntityPlayer && playersBool.getObject()) {
            return true;
        } else return !(e instanceof EntityPlayer) && mobsBool.getObject();
    }

    @EventTarget
    public void onRender(Render3DEvent event) {
        List<EntityLivingBase> targets = EntityUtils.getEntityLivingBases();
        targets = targets.stream().filter(entity -> entity != mc.player && !entity.isDead).collect(Collectors.toList());

        for (Entity entity : targets) {
            if (!doRenderBoolean(entity)) {
                if (entity.isGlowing()) {
                    entity.setGlowing(false);
                }
                continue;
            }
            entity.setGlowing(true);
        }
    }
}
