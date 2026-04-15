package com.teamtea.eclipticseasons.compat.voxy.helper;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.lang.ref.WeakReference;

public interface IVoxyLevelProvider {

    default LevelChunk getLevelBind() {
        return getLevelReference() != null ?
                getLevelReference().get() : null;
    }

    default Level getLevelBind1() {
        LevelChunk levelChunk = getLevelBind();
        return levelChunk != null ?
                levelChunk.getLevel() : null;
    }

    default void release() {
        setLevelReference(null);
    }

    WeakReference<LevelChunk> getLevelReference();

    void setLevelReference(LevelChunk levelReference);

}
