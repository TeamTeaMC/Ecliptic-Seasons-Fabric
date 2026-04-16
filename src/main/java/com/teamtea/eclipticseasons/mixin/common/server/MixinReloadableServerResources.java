package com.teamtea.eclipticseasons.mixin.common.server;


import net.minecraft.server.ReloadableServerResources;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ReloadableServerResources.class)
public abstract class MixinReloadableServerResources {

    // @Shadow
    // @Final
    // private List<Registry.PendingTags<?>> postponedTags;
    //
    // @Inject(at = {@At(value = "INVOKE",
    //         ordinal = 1,
    //         target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V")},
    //         method = {"updateComponentsAndStaticRegistryTags"})
    // public void eclipticseasons$updateComponentsAndStaticRegistryTags(
    //         CallbackInfo ci) {
    //
    //     TagsUpdatedEvent tagsUpdatedEvent = TagsUpdatedEvent
    //             .builder()
    //             .updateCause(TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD)
    //             .lookupProvider(new HolderLookup.Provider() {
    //                 @Override
    //                 public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
    //                     return postponedTags.stream().map(Registry.PendingTags::key);
    //                 }
    //
    //                 @Override
    //                 public <T> Optional<? extends HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
    //                     return (Optional) postponedTags
    //                             .stream()
    //                             .filter(pendingTags -> pendingTags.key()==key)
    //                             .map(pendingTags -> pendingTags.lookup())
    //                             .findFirst();
    //                 }
    //             })
    //             .build();
    //
    //     ESEventHook.TAG_UPDATED.invoker().onEvent(tagsUpdatedEvent);
    // }
}
