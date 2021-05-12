package pw.yallxe.outrage.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.GameTickEvent;
import pw.yallxe.outrage.events.Render2DEvent;
import pw.yallxe.outrage.events.Render3DEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.utils.CrystalUtils;
import pw.yallxe.outrage.utils.RendererUtils;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPage;
import pw.yallxe.outrage.utils.fontRenderer.GlyphPageFontRenderer;
import pw.yallxe.outrage.valuesystem.NumberValue;

import java.awt.*;

public class HoleESP extends Module {

    private final @NotNull NumberValue<Integer> holesval = new NumberValue<>("Holes", 1, 1, 10);
    private final @NotNull NumberValue<Float> range = new NumberValue<>("Range", 1f, 1f, 50f);
    private final @NotNull NumberValue<Integer> rVal = new NumberValue<>("R", 0, 0, 255);
    private final @NotNull NumberValue<Integer> gVal = new NumberValue<>("G", 0, 0, 255);
    private final @NotNull NumberValue<Integer> bVal = new NumberValue<>("B", 0, 0, 255);

    private final @NotNull GlyphPageFontRenderer renderer;

    public HoleESP() {
        super("HoleESP", "Shows safe spots.", ModuleCategory.RENDER);

        char[] chars = new char[256];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }

        GlyphPage glyphPage = new GlyphPage(new Font("BebasNeueRegular", Font.PLAIN, 17), true, true);

        glyphPage.generateGlyphPage(chars);
        glyphPage.setupTexture();

        renderer = new GlyphPageFontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);
    }

    @EventTarget
    public void onTick(@NotNull GameTickEvent event) {
        CrystalUtils.holes = CrystalUtils.calcHoles(range.getObject());
    }

    @EventTarget
    public void onRender2D(@NotNull Render2DEvent event) {
        for(BlockPos pos : CrystalUtils.getSortedHoles()) {
            if(pos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
                ScaledResolution sr = new ScaledResolution(mc);
                renderer.drawString("HoleESP: Safe", (float)(sr.getScaledWidth() - renderer.getStringWidth("HoleESP: Safe")) - 4, (float)(sr.getScaledHeight()) - renderer.getFontHeight() - 4, new Color(0, 255, 0).getRGB(), false);
                return;
            }
        }
        ScaledResolution sr = new ScaledResolution(mc);
        renderer.drawString("HoleESP: UnSafe", (float)(sr.getScaledWidth() - renderer.getStringWidth("HoleESP: UnSafe")) - 4, (float)(sr.getScaledHeight()) - renderer.getFontHeight() - 4, new Color(255, 0, 0).getRGB(), false);
    }

    @EventTarget
    public void onRender3D(@NotNull Render3DEvent event) {
        int drawnHoles = 0;
        for(BlockPos pos : CrystalUtils.getSortedHoles()) {

            if(drawnHoles >= holesval.getObject()) {
                break;
            }

            if(pos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
                continue;
            }

            if(CrystalUtils.isSafe(pos)) {
                RendererUtils.drawBlockESP(pos, rVal.getObject(), gVal.getObject(), bVal.getObject());
                drawnHoles++;
            }
        }
    }
}
