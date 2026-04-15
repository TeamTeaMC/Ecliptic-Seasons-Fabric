package net.neoforged.neoforge.event;


import com.teamtea.eclipticseasons.api.event.IESEvent;
import lombok.Builder;
import lombok.Data;
import net.minecraft.core.HolderLookup;
import net.neoforged.bus.api.Event;

@Data
@Builder
public class TagsUpdatedEvent implements Event, IESEvent {

    private final HolderLookup.Provider lookupProvider;
    private final UpdateCause updateCause;
    private final boolean integratedServer;

    public enum UpdateCause {
        SERVER_DATA_LOAD,
        CLIENT_PACKET_RECEIVED
    }
}
