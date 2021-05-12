package pw.yallxe.outrage.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.Render3DEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.utils.EntityUtils;
import pw.yallxe.outrage.valuesystem.BooleanValue;

import java.util.List;
import java.util.stream.Collectors;

public class OutlineESP extends Module {
    public OutlineESP() {
        super("OutlineESP", "Glowing outline esp", ModuleCategory.RENDER);
    }

    private final @NotNull BooleanValue playersBool = new BooleanValue("Players", true);
    private final @NotNull BooleanValue mobsBool = new BooleanValue("Mobs", true);

    private boolean doRenderBoolean(@NotNull Entity e) {
        if (e instanceof EntityPlayer && playersBool.getObject()) {
            return true;
        } else return !(e instanceof EntityPlayer) && mobsBool.getObject();
    }

    @EventTarget
    public void onRender(@NotNull Render3DEvent event) {
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
