package com.teamtea.eclipticseasons.compat.eclipticseasons_bundles;


import com.teamtea.eclipticseasons.compat.Platform;
import com.teamtea.eclipticseasons.config.sync.ESConfigSync;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.config.ModConfig;

public class EclipticSeasonsBundles {
    public static final String MODID = "eclipticseasons_bundles";

    public static void init() {
        if (!Platform.isModLoaded(MODID)) return;

        if (General.COMMON_CONFIG != null) {
            fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, General.COMMON_CONFIG);
            LangUtil.tryLoadLang(MODID, true);
            ModContents.registerBuiltinResourcePacks();
            ESConfigSync.specShouldSync.add(General.COMMON_CONFIG);
        }
    }

    public static Identifier rl(String id) {
        return Identifier.fromNamespaceAndPath(MODID, id);
    }
}