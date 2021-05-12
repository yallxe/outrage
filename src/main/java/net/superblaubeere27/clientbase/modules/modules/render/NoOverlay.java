package net.superblaubeere27.clientbase.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;
import net.superblaubeere27.clientbase.events.Render2DEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;

public class NoOverlay extends Module {
    /*private final Potion[] badPotions = new Potion[]{
            Potion.blindness,
            Potion.confusion
    };*/

    public NoOverlay() {
        super("NoOverlay", "Hide annoying overlays", ModuleCategory.RENDER);
    }

    /*@EventTarget
    public void render(Render2DEvent event) {
        for (Potion potion : badPotions)
            mc.player.removePotionEffect(potion.id);
    }*/
}
