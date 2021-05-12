package pw.yallxe.outrage.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import pw.yallxe.outrage.events.Render3DEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.utils.EntityUtils;
import pw.yallxe.outrage.utils.RendererUtils;
import pw.yallxe.outrage.valuesystem.BooleanValue;
import pw.yallxe.outrage.valuesystem.ModeValue;
import pw.yallxe.outrage.valuesystem.NumberValue;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ESP extends Module {
    public ESP() {
        super("ESP", "Highlights players", ModuleCategory.RENDER);
    }

    private @NotNull final ModeValue espmode = new ModeValue("Mode", "GishCode", "GishCode", "Box");
    private @NotNull final NumberValue<Integer> colorR = new NumberValue<>("Red", 0, 0, 255);
    private @NotNull final NumberValue<Integer> colorG = new NumberValue<>("Green", 0, 0, 255);
    private @NotNull final NumberValue<Integer> colorB = new NumberValue<>("Blue", 0, 0, 255);
    private @NotNull final BooleanValue playersBool = new BooleanValue("Players", true);
    private @NotNull final BooleanValue mobsBool = new BooleanValue("Mobs", true);

    private boolean doRenderBoolean(@NotNull Entity e) {
        if (e instanceof EntityPlayer && playersBool.getObject()) {
            return true;
        } else return !(e instanceof EntityPlayer) && mobsBool.getObject();
    }

    @EventTarget
    public void render(@NotNull Render3DEvent event) {
        List<EntityLivingBase> targets = EntityUtils.getEntityLivingBases();

        targets = targets.stream().filter(entity -> entity != mc.player && !entity.isDead).collect(Collectors.toList());

        for (EntityLivingBase entity : targets) {
            if (!doRenderBoolean(entity)) {
                continue;
            }
            if (espmode.getModes()[espmode.getObject()].equals("GishCode")) {
                RendererUtils.drawRombESP(entity, colorR.getObject(), colorG.getObject(), colorB.getObject(), 60f, event.ticks);
            } else if (espmode.getModes()[espmode.getObject()].equals("Box")) {
                RendererUtils.drawEntityESP(entity, new Color(colorR.getObject(), colorG.getObject(), colorB.getObject()));
            }
        }
    }
}

