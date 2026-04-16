package com.teamtea.eclipticseasons.compat.eclipticseasons_bundles;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.compat.Platform;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class General {
    public record PackController(
            ModConfigSpec.BooleanValue enable,
            ModConfigSpec.BooleanValue priorityLoading,
            BundleConfig config
    ) {
        public static PackController of(ModConfigSpec.BooleanValue enable,
                                        ModConfigSpec.BooleanValue priorityLoading,
                                        BundleConfig config) {
            return new PackController(enable, priorityLoading, config);
        }
    }

    public static ModConfigSpec COMMON_CONFIG;

    // public static ModConfigSpec.ConfigValue<String> order;
    public static Map<String, PackController> enableList = new HashMap<>();

    static {
        DynamicOps<JsonElement> dynamicops = JsonOps.INSTANCE;
        Version minecraft = Platform.getModFile("minecraft")
                .get()
                .getMetadata()
                .getVersion();
        LangUtil.tryLoadLang(EclipticSeasonsBundles.MODID, false);

        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

        // COMMON_BUILDER.comment("Compat settings");

        ModContainer container = Platform.getModFile(EclipticSeasonsBundles.MODID)
                .orElseThrow();
        Path modRoot = container.getRootPaths().get(0);
        Path resourcePacksPath = modRoot.resolve("resourcepacks");

        Set<String> topDirs = new HashSet<>();

        if (Files.exists(resourcePacksPath)) {
            try (Stream<Path> walk = Files.walk(resourcePacksPath)) {
                walk.filter(Files::isRegularFile)
                        .forEach(path -> {
                            String relativePath = modRoot.relativize(path).toString().replace("\\", "/");

                            if (relativePath.endsWith("/bundle.cfg")) {
                                String subPath = relativePath.substring("resourcepacks/".length());
                                String[] parts = subPath.split("/");

                                if (parts.length >= 2) {
                                    topDirs.add(parts[0]);
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        {
            bsp:
            for (String packageName : topDirs) {
                Path configPath = modRoot.resolve("resourcepacks/" + packageName + "/bundle.cfg");
                if (!Files.exists(configPath)) continue;
                try {
                    InputStream inputStream = Files.newInputStream(configPath);
                    if (inputStream == null) continue;
                    String json = new String(inputStream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                    inputStream.close();
                    if (json.isEmpty()) continue;

                    JsonElement jsonElement = GsonHelper.parse(json);
                    BundleConfig config = BundleConfig.CODEC
                            .parse(dynamicops, jsonElement)
                            .resultOrPartial(x ->
                                    {
                                        String formatted = ("Invalid JSON in " + configPath);
                                        EclipticSeasons.LOGGER.warn(formatted);
                                    }
                            ).orElse(null);
                    if (config == null) continue;

                    System.out.println("Loaded config for " + config.getId());


                    if (!config.getRequire().isEmpty()) {
                        boolean anyLoaded = false;

                        for (String require : config.getRequire()) {
                            if (Platform.isModLoaded(require)) {
                                anyLoaded = true;
                                if (!config.isRequireAll()) break;
                            } else {
                                if (config.isRequireAll()) {
                                    continue bsp;
                                }
                            }
                        }

                        if (!config.isRequireAll() && !anyLoaded) {
                            continue;
                        }
                    }


                    if (!config.getMcVersion().isEmpty()) {
                        boolean matched = config.getMcVersion().stream().anyMatch(spec -> {
                            try {
                                if (!spec.contains(")") && !spec.contains("]") && !spec.contains("[")) {
                                    spec = "[%s,%s]".formatted(spec, spec);
                                }
                                return VersionPredicate.parse(spec).test(minecraft);
                            } catch (Exception ignored) {
                                return false;
                            }
                        });
                        if (!matched) continue;
                    }


                    COMMON_BUILDER.comment(LangUtil.parseI18n(config.getDescription().isEmpty() ?
                            EclipticSeasons.erl(EclipticSeasonsBundles.MODID, config.getId()).toLanguageKey("pack_description") :
                            config.getDescription()
                    ));
                    COMMON_BUILDER.translation(
                            EclipticSeasons.erl(EclipticSeasonsBundles.MODID, config.getId()).toLanguageKey("pack")
                    ).push(config.getId());
                    var enable = COMMON_BUILDER
                            .comment(String.format("Enable compat package %s", config.getId()))
                            .translation(packageName)
                            .define("Enable", config.isEnable());

                    var priorityLoading = COMMON_BUILDER
                            .comment("This package will be loaded first [Not use in Fabric].")
                            //.translation(packageName)
                            .define("PriorityLoading", config.isTop());

                    enableList.put(packageName, PackController.of(enable, priorityLoading, config));
                    COMMON_BUILDER.pop();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (com.google.gson.JsonParseException e) {
                    System.err.println("Invalid JSON in " + configPath + ": " + e.getMessage());
                }
            }
        }


        // If any skip, then null
        try {
            COMMON_CONFIG = COMMON_BUILDER.build();
        } catch (IllegalStateException e) {

        }


    }

}
