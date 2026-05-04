package com.teamtea.eclipticseasons.compat.configured;

import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.api.ModContext;
import com.mrcrayfish.configured.api.util.ConfigScreenHelper;
import com.mrcrayfish.configured.client.ClientHandler;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.compat.CompatModule;
import com.teamtea.eclipticseasons.compat.Platform;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ConfiguredUtil {

    public static Screen getSafe(Screen p) {
        Function<Screen, Screen> screenConsumer = ConfiguredUtil::get;
        return CompatModule.isConfigured() ?
                screenConsumer.apply(p) : null;
    }

    public static Screen get(Screen p) {
        Optional<ModContainer> modFile = Platform.getModFile(EclipticSeasonsApi.MODID);
        return modFile.map(modContainer -> newConfigScreen(p, modContainer)).orElse(null);
    }

    static Screen newConfigScreen(Screen currentScreen, ModContainer container) {
        String modId = container.getMetadata().getId();
        Map<ConfigType, Set<IModConfig>> modConfigMap = ClientHandler.createConfigMap(new ModContext(modId));
        return modConfigMap.isEmpty() ? null : ConfigScreenHelper.createSelectionScreen(currentScreen, Component.literal(container.getMetadata().getName()), modConfigMap);
    }
}
