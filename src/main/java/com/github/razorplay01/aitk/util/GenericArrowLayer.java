package com.github.razorplay01.aitk.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
//? if >=1.21.2 {
/*import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.model.object.projectile.ArrowModel;
*///? }

//? if >=1.19.2 && <=1.21.1 {
public class GenericArrowLayer<T extends LivingEntity, M extends EntityModel<T>>
		extends GenericStuckInBodyLayer<T, M> {

	private final EntityRenderDispatcher dispatcher;

	public GenericArrowLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> renderer) {
		super(renderer);
		this.dispatcher = context.getEntityRenderDispatcher();
	}

	@Override
	protected int numStuck(T entity) {
		return entity.getArrowCount();
	}

	@Override
	protected void renderStuckItem(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
	                               Entity entity, float x, float y, float z, float partialTick) {

		float f = Mth.sqrt(x * x + z * z);

		//? if >=1.19.2 && <1.20.1 {
		/*net.minecraft.world.entity.projectile.Arrow arrow = new net.minecraft.world.entity.projectile.Arrow(entity.level, entity.getX(), entity.getY(), entity.getZ());
		*///? }
		//? if >=1.20.1 && <1.21.1 {
		/*net.minecraft.world.entity.projectile.Arrow arrow = new net.minecraft.world.entity.projectile.Arrow(entity.level(), entity.getX(), entity.getY(), entity.getZ());
		*///? }
		//? if >=1.21.1 {
		net.minecraft.world.entity.projectile.Arrow arrow = new net.minecraft.world.entity.projectile.Arrow(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ARROW), null);
		//? }

		arrow.setYRot((float) (Math.atan2(x, z) * (180F / Math.PI)));
		arrow.setXRot((float) (Math.atan2(y, f) * (180F / Math.PI)));

		// Sincronizamos las rotaciones anteriores (importante para el render)
		arrow.yRotO = arrow.getYRot();
		arrow.xRotO = arrow.getXRot();

		// Renderizamos la flecha usando el dispatcher vanilla
		this.dispatcher.render(arrow, 0.0, 0.0, 0.0, 0.0F, partialTick, poseStack, buffer, packedLight);
	}
}
//? }

//? if >=1.21.2 {
/*public class GenericArrowLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GenericStuckInBodyLayer<S, M, ArrowRenderState> {

    public GenericArrowLayer(LivingEntityRenderer<?, S, M> renderer, EntityRendererProvider.Context context) {
        super(renderer, new ArrowModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.ARROW)), new ArrowRenderState(), net.minecraft.client.renderer.entity.TippableArrowRenderer.NORMAL_ARROW_LOCATION);
    }

    @Override
    protected int numStuck(S state) {
        return ((StuckArrowsAccess) state).arrowsForAll$getArrowCount();
    }
}
*///? }
