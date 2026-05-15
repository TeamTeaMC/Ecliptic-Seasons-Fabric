package warp.net.neoforged.neoforge.server;

import net.minecraft.server.MinecraftServer;

public final class ServerLifecycleHooks {
    public static MinecraftServer SERVER;

    public static MinecraftServer getCurrentServer() {
        return SERVER;
    }
}
