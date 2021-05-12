package pw.yallxe.outrage.modules.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.MotionUpdateEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", "auto totem but bugs if your inventory is full ://", ModuleCategory.COMBAT);
    }

    @EventTarget
    public void handler(@NotNull MotionUpdateEvent event) {
        ItemStack offhand_item = mc.player.getHeldItemOffhand();
        if (offhand_item.getItem() != Items.TOTEM_OF_UNDYING && mc.player.getHealth() <= 5) {
            for (ItemStack itemStack : mc.player.inventory.mainInventory) {
                if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                    mc.player.setHeldItem(EnumHand.OFF_HAND, itemStack);
                }
            }
        }
    }
}
