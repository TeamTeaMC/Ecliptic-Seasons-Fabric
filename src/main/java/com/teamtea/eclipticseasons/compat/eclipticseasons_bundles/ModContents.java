package com.teamtea.eclipticseasons.compat.eclipticseasons_bundles;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.compat.Platform;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.fabric.impl.resource.ResourceLoaderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Locale;
import java.util.Optional;

public class ModContents {

    public static void registerBuiltinResourcePacks() {
        var modContainer = Platform.getModFile(EclipticSeasonsBundles.MODID);
        if (modContainer.isPresent()) {
            CommentedFileConfig oldConfig = CommentedFileConfig.builder(FabricLoader.getInstance().getConfigDir().resolve(EclipticSeasons.defaultConfigName(ModConfig.Type.COMMON, EclipticSeasonsBundles.MODID)))
                    .preserveInsertionOrder().build();
            oldConfig.load();

            for (var stringBooleanValueEntry : General.enableList.entrySet()) {
                String name = stringBooleanValueEntry.getKey();
                var packController = stringBooleanValueEntry.getValue();
                Optional<Boolean> serverOnly = packController.config().getServerOnly();
                if (isShouldLoad(oldConfig, packController.enable())) {
                    // FakeResourceManagerHelperUtil.addPackForExtra(
                    //         event, modFile,
                    //         EclipticSeasonsBundles.MODID,
                    //         name, packController.config().getId(),
                    //         isShouldLoad(oldConfig, packController.priorityLoading())
                    // );
                    ResourceLoaderImpl.registerBuiltinPack(
                            EclipticSeasonsBundles.rl(packController.config().getId()),
                            "resourcepacks/"+name,
                            modContainer.get(),
                            Component.translatable(EclipticSeasonsBundles.rl(packController.config().getId()).toLanguageKey("pack")),
                            PackActivationType.ALWAYS_ENABLED);
                }
            }
            oldConfig.close();
        }
    }

    private static boolean isShouldLoad(CommentedFileConfig oldConfig, ModConfigSpec.BooleanValue booleanValue) {
        boolean shouldLoad;
        try {
            shouldLoad = booleanValue.get();
        } catch (IllegalStateException illegalStateException) {
            shouldLoad = oldConfig.getOrElse(booleanValue.getPath(), false);
        }
        return shouldLoad;
    }

    public static String normalizeId(String raw) {
        return raw
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }


}
