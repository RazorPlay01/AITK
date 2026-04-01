package com.github.razorplay01.aitk.mixin;

import com.github.razorplay01.aitk.util.GenericArrowLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <=1.21.8 {
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
//?}
//? if >=1.21.2 {
/*import com.github.razorplay01.aitk.util.StuckArrowsAccess;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
*///? }

//? if >=1.19.2 && <=1.21.1 {
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

	protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initLeashLayer(
			EntityRendererProvider.Context context,
			EntityModel<?> model, float shadow, CallbackInfo ci) {

		if ((Object) this instanceof PlayerRenderer) return;

		LivingEntityRenderer<?, ?> self = (LivingEntityRenderer<?, ?>) (Object) this;
		self.addLayer(new GenericArrowLayer(context, self));
	}
//? }


//? if >=1.21.2 {
/*@Mixin(LivingEntityRenderer.class)
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
*///? }
}
