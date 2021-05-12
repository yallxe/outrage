package net.superblaubeere27.clientbase.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.superblaubeere27.clientbase.events.GameTickEvent;
import net.superblaubeere27.clientbase.events.Render2DEvent;
import net.superblaubeere27.clientbase.events.Render3DEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.BlockUtils;
import net.superblaubeere27.clientbase.utils.CrystalUtils;
import net.superblaubeere27.clientbase.utils.EntityUtils;
import net.superblaubeere27.clientbase.utils.RendererUtils;
import net.superblaubeere27.clientbase.utils.fontRenderer.GlyphPage;
import net.superblaubeere27.clientbase.utils.fontRenderer.GlyphPageFontRenderer;
import net.superblaubeere27.clientbase.valuesystem.BooleanValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HoleESP extends Module {

    private final NumberValue<Integer> holesval = new NumberValue<>("Holes", 1, 1, 10);
    private final NumberValue<Float> range = new NumberValue<>("Range", 1f, 1f, 50f);
    private final NumberValue<Integer> rVal = new NumberValue<>("R", 0, 0, 255);
    private final NumberValue<Integer> gVal = new NumberValue<>("G", 0, 0, 255);
    private final NumberValue<Integer> bVal = new NumberValue<>("B", 0, 0, 255);
    private final BooleanValue obsidianVal = new BooleanValue("Obsidian", true);
    private final BooleanValue bedrockVal = new BooleanValue("Bedrock", true);

    private final GlyphPageFontRenderer renderer;

    private static final List<BlockPos> midSafety = new ArrayList<>();
    private static List<BlockPos> holes = new ArrayList<>();
    private static final BlockPos[] surroundOffset = BlockUtils.toBlockPos(EntityUtils.getOffsets(0, true));

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
    public void onTick(GameTickEvent event) {
        holes = CrystalUtils.calcHoles(range.getObject());
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
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
    public void onRender3D(Render3DEvent event) {
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
