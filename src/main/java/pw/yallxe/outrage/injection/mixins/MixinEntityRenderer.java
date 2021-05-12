package pw.yallxe.outrage.injection.mixins;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pw.yallxe.outrage.events.Render3DEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinEntityRenderer {
    @Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE), remap = false)
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, @NotNull CallbackInfo callbackInfo) {
        EventManager.call(new Render3DEvent(partialTicks));
    }
}
