package net.superblaubeere27.clientbase.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static List<Block> unSolidBlocks = Arrays.asList(
            Blocks.FLOWING_LAVA,
            Blocks.FLOWER_POT,
            Blocks.SNOW,
            Blocks.CARPET,
            Blocks.END_ROD,
            Blocks.SKULL,
            Blocks.FLOWER_POT,
            Blocks.TRIPWIRE,
            Blocks.TRIPWIRE_HOOK,
            Blocks.WOODEN_BUTTON,
            Blocks.LEVER,
            Blocks.STONE_BUTTON,
            Blocks.LADDER,
            Blocks.UNPOWERED_COMPARATOR,
            Blocks.POWERED_COMPARATOR,
            Blocks.UNPOWERED_REPEATER,
            Blocks.POWERED_REPEATER,
            Blocks.UNLIT_REDSTONE_TORCH,
            Blocks.REDSTONE_TORCH,
            Blocks.REDSTONE_WIRE,
            Blocks.AIR,
            Blocks.PORTAL,
            Blocks.END_PORTAL,
            Blocks.WATER,
            Blocks.FLOWING_WATER,
            Blocks.LAVA,
            Blocks.FLOWING_LAVA,
            Blocks.SAPLING,
            Blocks.RED_FLOWER,
            Blocks.YELLOW_FLOWER,
            Blocks.BROWN_MUSHROOM,
            Blocks.RED_MUSHROOM,
            Blocks.WHEAT,
            Blocks.CARROTS,
            Blocks.POTATOES,
            Blocks.BEETROOTS,
            Blocks.REEDS,
            Blocks.PUMPKIN_STEM,
            Blocks.MELON_STEM,
            Blocks.WATERLILY,
            Blocks.NETHER_WART,
            Blocks.COCOA,
            Blocks.CHORUS_FLOWER,
            Blocks.CHORUS_PLANT,
            Blocks.TALLGRASS,
            Blocks.DEADBUSH,
            Blocks.VINE,
            Blocks.FIRE,
            Blocks.RAIL,
            Blocks.ACTIVATOR_RAIL,
            Blocks.DETECTOR_RAIL,
            Blocks.GOLDEN_RAIL,
            Blocks.TORCH
    );

    public static boolean isBlockSolid(BlockPos pos){
        return !isBlockUnSolid(pos);
    }

    public static boolean isBlockUnSolid(BlockPos pos){
        return isBlockUnSolid(mc.world.getBlockState(pos).getBlock());
    }

    public static boolean isBlockUnSolid(Block block) {
        return unSolidBlocks.contains(block);
    }

    public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static BlockPos[] toBlockPos(Vec3d[] vec3ds) {
        BlockPos[] list = new BlockPos[vec3ds.length];
        for(int i = 0; i < vec3ds.length; i++) {
            list[i] = new BlockPos(vec3ds[i]);
        }
        return list;
    }

    public static Boolean isPosInFov(BlockPos pos) {
        int dirnumber = RotationUtils.getDirection4D();

        if(dirnumber == 0 && pos.getZ() - mc.player.getPositionVector().z < 0) {
            return false;
        }

        if(dirnumber == 1 && pos.getX() - mc.player.getPositionVector().x > 0) {
            return false;
        }

        if(dirnumber == 2 && pos.getZ() - mc.player.getPositionVector().z > 0) {
            return false;
        }

        return !(dirnumber == 3 && pos.getX() - mc.player.getPositionVector().x < 0);
    }
}
