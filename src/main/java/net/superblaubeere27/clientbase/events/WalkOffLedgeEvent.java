package net.superblaubeere27.clientbase.events;

import com.darkmagician6.eventapi.events.Event;

public class WalkOffLedgeEvent implements Event {
    public boolean isSneaking;

    public WalkOffLedgeEvent(boolean isSneaking) {
        this.isSneaking = isSneaking;
    }
}
