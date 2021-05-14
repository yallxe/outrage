/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUtils {
    private static final @NotNull Minecraft mc = Minecraft.getMinecraft();

    public static @NotNull List<EntityLivingBase> getEntityLivingBases() {
        return mc.world.loadedEntityList.stream()
                .filter(EntityLivingBase.class::isInstance)
                .map(EntityLivingBase.class::cast)
                .collect(Collectors.toList());
    }


    public static Vec3d @NotNull [] getOffsets(int y, boolean floor) {
        List<Vec3d> offsets = getOffsetList(y, floor);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static List<Vec3d> getOffsetList(int y, boolean floor) {
        List<Vec3d> offsets = new ArrayList<>();
        offsets.add(new Vec3d(-1, y, 0));
        offsets.add(new Vec3d(1, y, 0));
        offsets.add(new Vec3d(0, y, -1));
        offsets.add(new Vec3d(0, y, 1));

        if(floor) {
            offsets.add(new Vec3d(0, y - 1, 0));
        }

        return offsets;
    }
}
