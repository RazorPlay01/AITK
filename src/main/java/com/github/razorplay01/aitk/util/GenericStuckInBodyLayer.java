package com.github.razorplay01.aitk.util;

import com.github.razorplay01.aitk.mixin.ModelPartAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//? if >=1.21.2 {
/*import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Util;
import org.jspecify.annotations.NonNull;
*///? }

//? if >=1.19.2 && <=1.21.1 {
public abstract class GenericStuckInBodyLayer<T extends LivingEntity, M extends EntityModel<T>>
		extends RenderLayer<T, M> {

	protected GenericStuckInBodyLayer(LivingEntityRenderer<T, M> renderer) {
		super(renderer);
	}

	protected abstract int numStuck(T entity);

	/**
	 * Renderiza un solo ítem clavado (la flecha).
	 * Aquí puedes sobrescribir si quieres flechas de diferentes tipos/texturas.
	 */
	protected abstract void renderStuckItem(PoseStack poseStack, MultiBufferSource buffer,
	                                        int packedLight, Entity entity, float x, float y, float z, float partialTick);

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity,
	                   float limbSwing, float limbSwingAmount, float partialTick,
	                   float ageInTicks, float netHeadYaw, float headPitch) {

		int count = numStuck(livingEntity);
		if (count <= 0) return;

		// Usamos getRandomModelPart como hace vanilla, pero con filtro extra por seguridad
		RandomSource random = RandomSource.create(livingEntity.getId());

		List<ModelPart> parts = new ArrayList<>();
		if (!(this.getParentModel() instanceof net.minecraft.client.model.AgeableListModel)) return;
		com.github.razorplay01.aitk.mixin.AgeableListModelAccessor ageableListModelAccessor = (com.github.razorplay01.aitk.mixin.AgeableListModelAccessor) this.getParentModel();
		parts.addAll(makeCollection(ageableListModelAccessor.aitk$headParts()));
		parts.addAll(makeCollection(ageableListModelAccessor.aitk$bodyParts()));

		List<ModelPart> partsWithCubes = parts.stream()
				.filter(part -> !((ModelPartAccessor) (Object) part).aitk$getCubes().isEmpty())
				.toList();

		if (partsWithCubes.isEmpty()) return;

		for (int i = 0; i < count; i++) {
			poseStack.pushPose();

			ModelPart modelPart = net.minecraft.Util.getRandom(partsWithCubes, random);
			ModelPart.Cube cube = modelPart.getRandomCube(random);

			modelPart.translateAndRotate(poseStack);

			float f = random.nextFloat();
			float f1 = random.nextFloat();
			float f2 = random.nextFloat();

			float x = Mth.lerp(f, cube.minX, cube.maxX) / 16.0F;
			float y = Mth.lerp(f1, cube.minY, cube.maxY) / 16.0F;
			float z = Mth.lerp(f2, cube.minZ, cube.maxZ) / 16.0F;

			poseStack.translate(x, y, z);

			// Dirección para la rotación (tu estilo moderno)
			float dirX = -1.0F * (f * 2.0F - 1.0F);
			float dirY = -1.0F * (f1 * 2.0F - 1.0F);
			float dirZ = -1.0F * (f2 * 2.0F - 1.0F);

			this.renderStuckItem(poseStack, buffer, packedLight, livingEntity, dirX, dirY, dirZ, partialTick);

			poseStack.popPose();
		}
	}

	public static <E> Collection<E> makeCollection(Iterable<E> iter) {
		Collection<E> list = new ArrayList<E>();
		for (E item : iter) {
			list.add(item);
		}
		return list;
	}
}
//? }


//? if >=1.21.2 {

/*public abstract class GenericStuckInBodyLayer<S extends LivingEntityRenderState, M extends EntityModel<S>, R>
		extends RenderLayer<S, M> {

	private final net.minecraft.client.model.Model<R> model;
	private final R modelState;
	private final net.minecraft.resources.ResourceLocation texture;

	protected GenericStuckInBodyLayer(
			LivingEntityRenderer<?, S, M> renderer,
			net.minecraft.client.model.Model<R> model, R modelState, net.minecraft.resources.ResourceLocation texture) {
		super(renderer);
		this.model = model;
		this.modelState = modelState;
		this.texture = texture;
	}

	protected abstract int numStuck(S state);

	@Override
	public void submit(
			@NonNull PoseStack poseStack,
			@NonNull SubmitNodeCollector submitNodeCollector,
			int lightCoords, S state, float yRot, float xRot) {

		int count = this.numStuck(state);
		if (count <= 0) return;

		// FILTRAR: solo partes que realmente tienen cubos
		List<ModelPart> partsWithCubes = this.getParentModel().allParts().stream()
				.filter(part -> !((ModelPartAccessor) (Object) part).aitk$getCubes().isEmpty())
				.toList();

		// Si el modelo no tiene partes con cubos, no hacer nada
		if (partsWithCubes.isEmpty()) return;

		RandomSource random = RandomSource.create(
				((StuckArrowsAccess) state).aitk$getEntityId()
		);

		for (int i = 0; i < count; i++) {
			poseStack.pushPose();

			// Usar la lista filtrada en vez de model.allParts()
			ModelPart modelPart = Util.getRandom(partsWithCubes, random);
			ModelPart.Cube cube = modelPart.getRandomCube(random);
			modelPart.translateAndRotate(poseStack);

			float midX = random.nextFloat();
			float midY = random.nextFloat();
			float midZ = random.nextFloat();

			poseStack.translate(
					Mth.lerp(midX, cube.minX, cube.maxX) / 16.0F,
					Mth.lerp(midY, cube.minY, cube.maxY) / 16.0F,
					Mth.lerp(midZ, cube.minZ, cube.maxZ) / 16.0F
			);

			float dirX = -(midX * 2.0F - 1.0F);
			float dirY = -(midY * 2.0F - 1.0F);
			float dirZ = -(midZ * 2.0F - 1.0F);
			float dirXZ = Mth.sqrt(dirX * dirX + dirZ * dirZ);
			float rotY = (float) (Math.atan2(dirX, dirZ) * 180.0F / Math.PI);
			float rotX = (float) (Math.atan2(dirY, dirXZ) * 180.0F / Math.PI);
			poseStack.mulPose(Axis.YP.rotationDegrees(rotY - 90.0F));
			poseStack.mulPose(Axis.ZP.rotationDegrees(rotX));

			submitNodeCollector.submitModel(
					this.model, this.modelState, poseStack, this.model.renderType(this.texture),
					lightCoords, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
					state.outlineColor, null
			);

			poseStack.popPose();
		}
	}
}

*///? }
