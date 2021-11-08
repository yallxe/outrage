package pw.yallxe.outrage.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.GameTickEvent;
import pw.yallxe.outrage.events.Render3DEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.utils.BlockUtils;
import pw.yallxe.outrage.utils.RendererUtils;
import pw.yallxe.outrage.valuesystem.BooleanValue;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class HypixelPP extends Module {

    private static final int hpp_size = 35;  // const 35 is a rounded size of Hypixel Pixel Party map

    private final @NotNull BooleanValue nearestOnly = new BooleanValue("Nearest only", false);
    private final @NotNull BooleanValue traceNearest = new BooleanValue("Trace Nearest", false);
    private final @NotNull BooleanValue safetyIndicator = new BooleanValue("Safety Indicator", false);

    public HypixelPP() {
        super("HypixelPP", "Hypixel Pixel Party ESP", ModuleCategory.RENDER);
    }

    private List<BlockPos> getSortedPoses(ItemStack iStack) {
        List<BlockPos> poses = BlockUtils.getPixelPartyBlockPoses(iStack,
                (int)mc.player.posX - hpp_size,
                (int)mc.player.posX + hpp_size,
                (int)mc.player.posY - 2,
                (int) mc.player.posY,
                (int)mc.player.posZ - hpp_size,
                (int)mc.player.posZ + hpp_size);
        poses.sort(Comparator.comparingDouble(pos -> mc.player.getDistanceSq(pos)));
        return poses;
    }

    @EventTarget
    public void onTick(@NotNull GameTickEvent event) {
        if (safetyIndicator.getObject()) {
            BlockPos player_bp = new BlockPos((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ);
            BlockPos nearest_pos = getSortedPoses(mc.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND)).get(0);
            if (player_bp.getX() == nearest_pos.getX() && player_bp.getZ() == nearest_pos.getZ()) {
                this.setHudStatus("Safe");
            } else this.setHudStatus("Unsafe");
        }
    }

    @EventTarget
    public void onRender3D(@NotNull Render3DEvent event) {
        ItemStack iStack = mc.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        if (iStack.getItem() == Items.AIR) return;

        List<BlockPos> poses = getSortedPoses(iStack);

        if (traceNearest.getObject()) RendererUtils.drawTracer(
                new Vec3d(poses.get(0).getX(), poses.get(0).getY(), poses.get(0).getZ()),
                new Color(255, 255, 255), event.ticks
        );

        for (BlockPos pos : poses) {
            RendererUtils.drawBlockESP(pos, new Color(255, 0, 0, 30));
            if (nearestOnly.getObject()) break;
        }
    }
}
