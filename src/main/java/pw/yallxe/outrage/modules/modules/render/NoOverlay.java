package pw.yallxe.outrage.modules.modules.render;

import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;

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
