// Pasted from Rusherhack

package pw.yallxe.outrage.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReflectionFields {
    
    public static @NotNull
    final Minecraft mc = Minecraft.getMinecraft();
    
    public static @Nullable Field renderPosX;
    public static @Nullable Field renderPosY;
    public static @Nullable Field renderPosZ;
    public static @Nullable Field playerViewX;
    public static @Nullable Field playerViewY;
    public static @Nullable Field timer;
    public static @Nullable Field modelManager;
    public static @Nullable Field pressed;

    public static @Nullable Field cpacketPlayerYaw;
    public static @Nullable Field cpacketPlayerPitch;

    public static @Nullable Field spacketPlayerPosLookYaw;
    public static @Nullable Field spacketPlayerPosLookPitch;

    public static @Nullable Field mapTextureObjects;

    public static @Nullable Field cpacketPlayerOnGround;

    public static @Nullable Field rightClickDelayTimer;

    public static @Nullable Field horseJumpPower;

    private static @Nullable Field modifiersField;
    public static @NotNull Method rightClickMouse;

    public static @Nullable Field curBlockDamageMP;
    public static @Nullable Field blockHitDelay;

    public static @Nullable Field debugFps;

    public static @Nullable Field lowerChestInventory;
    public static @Nullable Field shulkerInventory;

    public static @Nullable Field spacketExplosionMotionX;
    public static @Nullable Field spacketExplosionMotionY;
    public static @Nullable Field spacketExplosionMotionZ;

    public static @Nullable Field cpacketPlayerY;
    public static @Nullable Field cpacketVehicleMoveY;
    public static @Nullable Field session;


    public static @Nullable Field PLAYER_MODEL_FLAG;
    public static @Nullable Field speedInAir;

    public static @Nullable Field guiButtonHovered;

    public static @Nullable Field ridingEntity;

    public static @Nullable Field foodExhaustionLevel;

    public static @Nullable Field cPacketUpdateSignLines;

    public static @Nullable Field hopperInventory;

    public static @Nullable Field cPacketChatMessage;

    public static @Nullable Field guiSceenServerListServerData;
    public static @Nullable Field guiDisconnectedParentScreen;
    public static @Nullable Field sPacketChatChatComponent;

    public static @Nullable Field boundingBox;

    public static @Nullable Field y_vec3d;

    public static @Nullable Field sleeping;

    public static @Nullable Field sleepTimer;

    static {
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
        } catch (Exception ignored)   {
        }
    }

    public static void init() {
        try {
            renderPosX = getField(RenderManager.class, "renderPosX", "field_78725_b");
            renderPosY = getField(RenderManager.class, "renderPosY", "field_78726_c");
            renderPosZ = getField(RenderManager.class, "renderPosZ", "field_78723_d");
            playerViewX = getField(RenderManager.class, "playerViewX", "field_78732_j");
            playerViewY = getField(RenderManager.class, "playerViewY", "field_78735_i");
            timer = getField(Minecraft.class, "timer", "field_71428_T");
            modelManager = getField(Minecraft.class, "modelManager", "field_175617_aL");
            rightClickMouse = getMethod(Minecraft.class, new String[]{"rightClickMouse", "func_147121_ag"});
            pressed = getField(KeyBinding.class, "pressed", "field_74513_e");

            cpacketPlayerYaw = getField(CPacketPlayer.class, "yaw", "field_149476_e");
            cpacketPlayerPitch = getField(CPacketPlayer.class, "pitch", "field_149473_f");

            spacketPlayerPosLookYaw = getField(SPacketPlayerPosLook.class, "yaw", "field_148936_d");
            spacketPlayerPosLookPitch = getField(SPacketPlayerPosLook.class, "pitch", "field_148937_e");

            mapTextureObjects = getField(TextureManager.class, "mapTextureObjects", "field_110585_a");

            cpacketPlayerOnGround = getField(CPacketPlayer.class, "onGround", "field_149474_g");

            rightClickDelayTimer = getField(Minecraft.class, "rightClickDelayTimer", "field_71467_ac");

            horseJumpPower = getField(EntityPlayerSP.class, "horseJumpPower", "field_110321_bQ");

            curBlockDamageMP = getField(PlayerControllerMP.class, "curBlockDamageMP", "field_78770_f");

            blockHitDelay = getField(PlayerControllerMP.class, "blockHitDelay", "field_78781_i");

            debugFps = getField(Minecraft.class, "debugFPS", "field_71470_ab");

            lowerChestInventory = getField(GuiChest.class, "lowerChestInventory", "field_147015_w");
            shulkerInventory = getField(GuiShulkerBox.class, "inventory", "field_190779_v");

            spacketExplosionMotionX = getField(SPacketExplosion.class, "motionX", "field_149152_f");
            spacketExplosionMotionY = getField(SPacketExplosion.class, "motionY", "field_149153_g");
            spacketExplosionMotionZ = getField(SPacketExplosion.class, "motionZ", "field_149159_h");

            cpacketPlayerY = getField(CPacketPlayer.class, "y", "field_149477_b");
            cpacketVehicleMoveY = getField(CPacketVehicleMove.class, "y", "field_187008_b");
            session = getField(Minecraft.class, "session", "field_71449_j");

            PLAYER_MODEL_FLAG = getField(EntityPlayer.class, "PLAYER_MODEL_FLAG", "field_184827_bp");
            speedInAir = getField(EntityPlayer.class, "speedInAir", "field_71102_ce");

            guiButtonHovered = getField(GuiButton.class, "hovered", "field_146123_n");

            ridingEntity = getField(Entity.class, "ridingEntity", "field_184239_as");

            foodExhaustionLevel = getField(FoodStats.class, "foodExhaustionLevel", "field_75126_c");

            cPacketUpdateSignLines = getField(CPacketUpdateSign.class, "lines", "field_149590_d");

            hopperInventory = getField(GuiHopper.class, "hopperInventory", "field_147083_w");

            cPacketChatMessage = getField(CPacketChatMessage.class, "message", "field_149440_a");

            guiSceenServerListServerData = getField(GuiScreenServerList.class, "serverData", "field_146301_f");
            guiDisconnectedParentScreen = getField(GuiDisconnected.class, "parentScreen", "field_146307_h");
            sPacketChatChatComponent = getField(SPacketChat.class, "chatComponent", "field_148919_a");

            boundingBox = getField(Entity.class, "boundingBox", "field_148919_a");

            y_vec3d = getField(Vec3d.class, "y", "field_72448_b", "c");

            sleeping = getField(EntityPlayer.class, "sleeping", "field_71083_bS", "bK");

            sleepTimer = getField(EntityPlayer.class, "sleepTimer", "field_71076_b");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static @NotNull Field getField(@NotNull Class c, String... names)   {
        for (String s : names)  {
            try {
                Field f = c.getDeclaredField(s);
                f.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                return f;
            } catch (NoSuchFieldException e)    {
                FMLLog.log.info("unable to find field: " + s);
            } catch (IllegalAccessException e)  {
                FMLLog.log.info("unable to make field changeable!");
            }
        }

        throw new IllegalStateException("Field with names: " + Arrays.toString(names) + " not found!");
    }

    public static Method getMethod(@NotNull Class c, String @NotNull [] names, Class<?>... args) {
        for (String s : names) {
            try {
                Method m = c.getDeclaredMethod(s, args);
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException e) {
                FMLLog.log.info("unable to find method: " + s);
            }
        }

        throw new IllegalStateException("Method with names: " + Arrays.toString(names) + " not found!");
    }

    public static double getRenderPosX() {
        try {
            return (double) renderPosX.get(mc.getRenderManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getRenderPosY() {
        try {
            return (double) renderPosY.get(mc.getRenderManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getRenderPosZ() {
        try {
            return (double) renderPosZ.get(mc.getRenderManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static float getPlayerViewY() {
        try {
            return (float) playerViewY.get(mc.getRenderManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static float getPlayerViewX() {
        try {
            return (float) playerViewX.get(mc.getRenderManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static Timer getTimer() {
        try {
            return (Timer) timer.get(mc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static ModelManager getModelManager() {
        try {
            return (ModelManager) modelManager.get(mc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void rightClickMouse() {
        try {
            rightClickMouse.invoke(mc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static boolean getPressed(KeyBinding binding) {
        try {
            return (boolean) pressed.get(binding);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setPressed(KeyBinding keyBinding, boolean state) {
        try {
            pressed.set(keyBinding, state);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketPlayerYaw(CPacketPlayer packet, float value) {
        try {
            cpacketPlayerYaw.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketPlayerPitch(CPacketPlayer packet, float value) {
        try {
            cpacketPlayerPitch.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setSPacketPlayerPosLookYaw(float value, SPacketPlayerPosLook packet) {
        try {
            spacketPlayerPosLookYaw.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setSPacketPlayerPosLookPitch(float value, SPacketPlayerPosLook packet) {
        try {
            spacketPlayerPosLookPitch.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static Map<ResourceLocation, ITextureObject> getMapTextureObjects() {
        try {
            return (Map<ResourceLocation, ITextureObject>) mapTextureObjects.get(mc.getTextureManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketPlayerOnGround(CPacketPlayer packet, boolean onGround) {
        try {
            cpacketPlayerOnGround.set(packet, onGround);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setRightClickDelayTimer(int value) {
        try {
            rightClickDelayTimer.set(mc, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setHorseJumpPower(float value) {
        try {
            horseJumpPower.set(mc.player, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static float getCurBlockDamageMP() {
        try {
            return (float) curBlockDamageMP.get(mc.playerController);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCurBlockDamageMP(float value) {
        try {
            curBlockDamageMP.set(mc.playerController, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static int getBlockHitDelay() {
        try {
            return (int) blockHitDelay.get(mc.playerController);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setBlockHitDelay(float value) {
        try {
            blockHitDelay.set(mc.playerController, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static int getDebugFps()   {
        try {
            return (int) debugFps.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static IInventory getLowerChestInventory(GuiChest chest) {
        try {
            return (IInventory) lowerChestInventory.get(chest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static IInventory getShulkerInventory(GuiShulkerBox chest) {
        try {
            return (IInventory) shulkerInventory.get(chest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setSPacketExplosionMotionX(SPacketExplosion packet, float value) {
        try {
            spacketExplosionMotionX.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setSPacketExplosionMotionY(SPacketExplosion packet, float value) {
        try {
            spacketExplosionMotionY.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setSPacketExplosionMotionZ(SPacketExplosion packet, float value) {
        try {
            spacketExplosionMotionZ.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getCPacketPlayerY(CPacketPlayer packet) {
        try {
            return (double)cpacketPlayerY.get(packet);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketPlayerY(CPacketPlayer packet, double value) {
        try {
            cpacketPlayerY.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getCPacketVehicleMoveY(CPacketVehicleMove packet) {
        try {
            return (double)cpacketVehicleMoveY.get(packet);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketVehicleMoveY(CPacketVehicleMove packet, double value) {
        try {
            cpacketVehicleMoveY.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setSession(Session newSession) {
        try {
            session.set(mc, newSession);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }


    public static DataParameter<Byte> getPLAYER_MODEL_FLAG() {
        try {
            return (DataParameter<Byte>) PLAYER_MODEL_FLAG.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);


        }
    }

    public static void setSpeedInAir(EntityPlayer entityPlayer, float newValue) {
        try {
            speedInAir.set(entityPlayer, newValue);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static float getSpeedInAir(EntityPlayer entityPlayer) {
        try {
            return (float) speedInAir.get(entityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);


        }
    }

    public static boolean getGuiButtonHovered(GuiButton button) {
        try {
            return (boolean) guiButtonHovered.get(button);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setGuiButtonHovered(GuiButton button, boolean value) {
        try {
            guiButtonHovered.set(button, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static Entity getRidingEntity(Entity toGetFrom) {
        try {
            return (Entity) ridingEntity.get(toGetFrom);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static float getFoodExhaustionLevel() {
        try {
            return (float) foodExhaustionLevel.get(mc.player.getFoodStats());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketUpdateSignLines(CPacketUpdateSign packet, String[] value) {
        try {
            cPacketUpdateSignLines.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static IInventory getHopperInventory(GuiHopper chest) {
        try {
            return (IInventory) hopperInventory.get(chest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketChatMessage(CPacketChatMessage packet, String value) {
        try {
            cPacketChatMessage.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }


    public static ServerData getServerData(GuiScreenServerList data) {
        try {
            return (ServerData) guiSceenServerListServerData.get(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static GuiScreen getGuiDisconnectedParentScreen(GuiDisconnected toGetFrom) {
        try {
            return (GuiScreen) guiDisconnectedParentScreen.get(toGetFrom);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setSPacketChatChatComponent (SPacketChat packet, TextComponentString value) {
        try {
            sPacketChatChatComponent.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setY_vec3d(Vec3d vec, double val) {
        try {
            y_vec3d.set(vec, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static boolean getSleeping(EntityPlayer mgr) {
        try {
            return (boolean) sleeping.get(mgr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setSleeping(EntityPlayer entityPlayer, boolean value) {
        try {
            sleeping.set(entityPlayer, Boolean.valueOf(value));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

    }

    public static void sleepTimer(EntityPlayer entityPlayer, int value) {
        try {
            sleeping.set(entityPlayer, Integer.valueOf(value));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

    }

}

