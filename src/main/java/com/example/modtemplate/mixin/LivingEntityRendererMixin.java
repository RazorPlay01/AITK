package com.example.modtemplate.mixin;

import com.example.modtemplate.util.GenericArrowLayer;
import com.example.modtemplate.util.StuckArrowsAccess;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initLeashLayer(
            EntityRendererProvider.Context context,
            EntityModel<?> model, float shadow, CallbackInfo ci) {

        if ((Object) this instanceof AvatarRenderer<?>) return;

        LivingEntityRenderer<?, ?, ?> self = (LivingEntityRenderer<?, ?, ?>) (Object) this;
        self.addLayer(new GenericArrowLayer(self, context));
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
            at = @At("HEAD"))
    public void extractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        StuckArrowsAccess stuckArrowsAccess = (StuckArrowsAccess) state;
        stuckArrowsAccess.arrowsForAll$setEntityId(entity.getId());
        stuckArrowsAccess.arrowsForAll$setArrowCount(entity.getArrowCount());
    }
}
