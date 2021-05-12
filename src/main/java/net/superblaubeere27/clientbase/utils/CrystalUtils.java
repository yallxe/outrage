package net.superblaubeere27.clientbase.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CrystalUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    private static final List<BlockPos> midSafety = new ArrayList<>();
    private static List<BlockPos> holes = new ArrayList<>();
    private static final BlockPos[] surroundOffset = BlockUtils.toBlockPos(EntityUtils.getOffsets(0, true));

    public static List<BlockPos> calcHoles(float range) {
        List<BlockPos> safeSpots = new ArrayList<>();
        midSafety.clear();
        List<BlockPos> positions = BlockUtils.getSphere(EntityUtils.getPlayerPos(mc.player), range, (int)range, false, true, 0);
        for (BlockPos pos : positions) {
            if(!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if(!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if(!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            boolean isSafe = true;
            boolean midSafe = true;
            for(BlockPos offset : surroundOffset) {
                Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
                if(BlockUtils.isBlockUnSolid(block)) {
                    midSafe = false;
                }

                if(block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    isSafe = false;
                }
            }

            if (isSafe) {
                safeSpots.add(pos);
            }

            if(midSafe) {
                midSafety.add(pos);
            }
        }
        return safeSpots;
    }

    public static boolean isSafe(BlockPos pos) {
        boolean isSafe = true;
        for(BlockPos offset : surroundOffset) {
            Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
            if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN) {
                isSafe = false;
                break;
            }
        }
        return isSafe;
    }

    public static List<BlockPos> getSortedHoles() {
        holes.sort(Comparator.comparingDouble(hole -> mc.player.getDistanceSq(hole)));
        return holes;
    }
}
