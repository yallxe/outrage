// so actually this shit is not skid (excepting safe places detection) but things kind of like resolvers of breaking n placing are custom and not skidded at all looool
package pw.yallxe.outrage.modules.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import pw.yallxe.outrage.events.GameTickEvent;
import pw.yallxe.outrage.events.Render3DEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.utils.CrystalUtils;
import pw.yallxe.outrage.utils.RendererUtils;
import pw.yallxe.outrage.valuesystem.BooleanValue;
import pw.yallxe.outrage.valuesystem.NumberValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CrystalAura extends Module {
    private final NumberValue<Integer> explodeRange = new NumberValue<>("ExplodeRange", 5, 1, 8);
    private final NumberValue<Integer> placeRange = new NumberValue<>("PlaceRange", 5, 1, 20);

    private final NumberValue<Integer> xBruteForce = new NumberValue<>("X Bruteforce", 2, 1, 5);
    private final NumberValue<Integer> yBruteForce = new NumberValue<>("Y Bruteforce", 2, 1, 5);
    private final NumberValue<Integer> zBruteForce = new NumberValue<>("Z Bruteforce", 2, 1, 5);

    private final BooleanValue checkSafeBreak = new BooleanValue("Check Safe on Break", true);
    private final BooleanValue checkSafePlace = new BooleanValue("Check Safe on Place", true);

    private final BooleanValue playersBool = new BooleanValue("Players", true);
    private final BooleanValue mobsBool = new BooleanValue("Mobs", true);

    private boolean doInteractBoolean(Entity e) {
        if (e instanceof EntityPlayer && playersBool.getObject()) {
            return true;
        } else return !(e instanceof EntityPlayer) && mobsBool.getObject();
    }

    public CrystalAura() {
        super("CrystalAura", "auto suicide", ModuleCategory.COMBAT);
    }

    public static boolean canPlaceCrystal(final BlockPos pos)
    {
        final Block block = mc.world.getBlockState(pos).getBlock();

        return (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) && mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.AIR
                && mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() == Blocks.AIR;
    }

    private void destroy_able_crystal() {
        for (Entity entity : mc.world.getLoadedEntityList()) { // FIXME: do modern entities iteration
            if (!(entity instanceof EntityEnderCrystal) || mc.player.getDistance(entity) >= explodeRange.getObject() || !doInteractBoolean(entity)) continue;
            List<EntityLivingBase> targets = mc.world.loadedEntityList.stream()
                    .filter(EntityLivingBase.class::isInstance)
                    .map(EntityLivingBase.class::cast).sorted(Comparator.comparingDouble(t -> t.getDistance(entity)))
                    .collect(Collectors.toList());
            if (can_apply_crystal(targets, entity)) {
                // final float[] rotations = RotationUtils.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionEyes(mc.getRenderPartialTicks()));
                // mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    private boolean can_apply_crystal(List<EntityLivingBase> targets, Entity crystal) {
        return targets.get(0) != null && targets.get(0).getHealth() > 0 && !targets.get(0).isDead && targets.get(0) != mc.player && targets.get(0).getDistance(crystal) < mc.player.getDistance(crystal);
    }

    private boolean check_crystal_placing(Entity p) {
        if (!doInteractBoolean(p)) return false;
        boolean haveEndCrystal = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        return !(mc.player.getDistance(p) >= placeRange.getObject() || p == mc.player || !haveEndCrystal || !(p instanceof EntityLivingBase));
    }

    private List<BlockPos> get_bruteforce_poses(BlockPos bp) {
        List<BlockPos> poses = new ArrayList<>();
        for (int x = -xBruteForce.getObject(); x < xBruteForce.getObject(); x++) {
            for (int y = -yBruteForce.getObject(); y < yBruteForce.getObject(); y++) {
                for (int z = -zBruteForce.getObject(); z < zBruteForce.getObject(); z++) {
                    if (!(y == 0 && z == 0 && x == 0)) poses.add(bp.add(x, y, z));
                }
            }
        }
        return poses;
    }

    private void place_crystal() {
        for (Entity p : mc.world.getLoadedEntityList()) {
            if (!check_crystal_placing(p))
                continue;
            List<BlockPos> poses = get_bruteforce_poses(new BlockPos(p.posX, p.posY, p.posZ).down());
            BlockPos bestPos = getPlaceBestPos(p, poses);

            if (bestPos == null) return;

            place(bestPos);

            poses.clear();
        }
    }

    private BlockPos getPlaceBestPos(Entity p, List<BlockPos> poses) {
        BlockPos bestPos = null;

        for (BlockPos nigg : poses) {
            if (!canPlaceCrystal(nigg) || !mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(nigg)).isEmpty() || nigg.getDistance((int)p.posX, (int)p.posY, (int)p.posZ) > nigg.getDistance((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ)) continue;
            if (bestPos == null) {
                bestPos = nigg;
            } else if (nigg.getY() == (int)p.posY && nigg.getDistance((int)p.posX, (int)p.posY, (int)p.posZ) <= 2) {
                bestPos = nigg;
            } else if (bestPos.getDistance((int)p.posX, (int)p.posY, (int)p.posZ) > nigg.getDistance((int)p.posX, (int)p.posY, (int)p.posZ)) {
                bestPos = nigg;
            }
        }
        return bestPos;
    }

    @EventTarget
    public void render(Render3DEvent event) {
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (!(entity instanceof EntityEnderCrystal) || mc.player.getDistance(entity) >= explodeRange.getObject() || !doInteractBoolean(entity)) continue;
            RendererUtils.drawBlockESP(new BlockPos(entity.posX, entity.posY, entity.posZ), 255, 0, 0);
        }

        for (Entity p : mc.world.getLoadedEntityList()) {
            if (!check_crystal_placing(p))
                continue;
            List<BlockPos> poses = get_bruteforce_poses(new BlockPos(p.posX, p.posY, p.posZ).down());

            BlockPos bestPos = getPlaceBestPos(p, poses);

            if (bestPos == null) return;

            RendererUtils.drawBlockESP(bestPos, 0, 255, 0);

            poses.clear();
        }
    }

    @EventTarget
    public void handler(GameTickEvent event) {
        if (!checkSafeBreak.getObject() || CrystalUtils.isSafe(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) destroy_able_crystal();
        if (!checkSafePlace.getObject() || CrystalUtils.isSafe(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) place_crystal();
    }

    private void place(BlockPos pos) {
        if (pos.getDistance((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ) >= placeRange.getObject() * 3) return;
        if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            /*final float[] rotations = RotationUtils.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));*/
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
            mc.player.swingArm(EnumHand.MAIN_HAND);
        } else if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            /*final float[] rotations = RotationUtils.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));*/
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.OFF_HAND, 0, 0, 0));
            mc.player.swingArm(EnumHand.OFF_HAND);
        }
    }
}
