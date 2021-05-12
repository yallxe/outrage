package net.superblaubeere27.clientbase.modules.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.superblaubeere27.clientbase.events.GameTickEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AntiBot extends Module {

    public AntiBot() {
        super("AntiBot", "Removes bots from game", ModuleCategory.COMBAT);
    }

    private List<EntityLivingBase> bots = new ArrayList<>();

    private final Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

    private String stripColor(String input) {
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }

    @EventTarget
    private void onTick(GameTickEvent event) {
        for (EntityLivingBase entity : mc.world.loadedEntityList.stream()
                .filter(EntityLivingBase.class::isInstance)
                .map(EntityLivingBase.class::cast)
                .collect(Collectors.toList())) {
            if (entity == mc.player) continue;
            String targetName = stripColor(entity.getDisplayName().getFormattedText());
            int times = 0;
            for (NetworkPlayerInfo networkPlayerInfo : Objects.requireNonNull(mc.getConnection()).getPlayerInfoMap()) {
                if (stripColor(Objects.requireNonNull(networkPlayerInfo.getDisplayName()).getFormattedText()).equals(targetName)) {
                    times += 1;
                }
                if (times > 1 && entity.ticksExisted >= 20 && !bots.contains(entity)) {
                    mc.world.removeEntity(entity);
                    System.out.println("Removed bot ");
                    break;
                }
            }
        }

    }
}
