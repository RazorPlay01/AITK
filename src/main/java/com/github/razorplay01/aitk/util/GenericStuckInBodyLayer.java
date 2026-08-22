package com.github.razorplay01.aitk.util;

import com.github.razorplay01.aitk.mixin.ModelPartAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
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
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Util;
import org.jspecify.annotations.NonNull;
//? }
import static com.github.razorplay01.aitk.util.Util.bestFromListMutable;

//? if >=1.19.2 && <=1.21.1 {
/*public abstract class GenericStuckInBodyLayer<T extends LivingEntity, M extends EntityModel<T>>
		extends RenderLayer<T, M> {

	protected GenericStuckInBodyLayer(LivingEntityRenderer<T, M> renderer) {
		super(renderer);
	}

	protected abstract int numStuck(T entity);

	protected abstract void renderStuckItem(PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource buffer,
	                                        int packedLight, Entity entity, float x, float y, float z, float partialTick);

	@Override
	public void render(PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight, T livingEntity,
	                   float limbSwing, float limbSwingAmount, float partialTick,
	                   float ageInTicks, float netHeadYaw, float headPitch) {

		int count = numStuck(livingEntity);
		if (count <= 0) return;

		if (!(this.getParentModel() instanceof net.minecraft.client.model.AgeableListModel)) return;

		com.github.razorplay01.aitk.mixin.AgeableListModelAccessor ageableListModelAccessor =
				(com.github.razorplay01.aitk.mixin.AgeableListModelAccessor) this.getParentModel();

		// Tops de la jerarquía (equivalente a partir del root en 1.21.2+)
		List<ModelPart> topParts = new ArrayList<>();
		topParts.addAll(makeCollection(ageableListModelAccessor.aitk$headParts()));
		topParts.addAll(makeCollection(ageableListModelAccessor.aitk$bodyParts()));

		if (topParts.isEmpty()) return;

		RandomSource random = RandomSource.create(livingEntity.getId());

		for (int i = 0; i < count; i++) {
			poseStack.pushPose();

			java.util.Random partRand = new java.util.Random(i);
			com.mojang.datafixers.util.Pair<ModelPart, Runnable> pair = Util.bestFromListMutable(
					new ArrayList<>(topParts),
					partRand,
					poseStack,
					true
			);

			if (pair == null) {
				poseStack.popPose();
				continue;
			}

			ModelPart modelPart = pair.getFirst();
			pair.getSecond().run(); // cadena completa padres → hijo

			if (((ModelPartAccessor) (Object) modelPart).aitk$getCubes().isEmpty()) {
				poseStack.popPose();
				continue;
			}

			ModelPart.Cube cube = modelPart.getRandomCube(random);

			float f = random.nextFloat();
			float f1 = random.nextFloat();
			float f2 = random.nextFloat();

			float x = Mth.lerp(f, cube.minX, cube.maxX) / 16.0F;
			float y = Mth.lerp(f1, cube.minY, cube.maxY) / 16.0F;
			float z = Mth.lerp(f2, cube.minZ, cube.maxZ) / 16.0F;

			poseStack.translate(x, y, z);

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
*///? }


//? if >=1.21.2 {

public abstract class GenericStuckInBodyLayer<S extends LivingEntityRenderState, M extends EntityModel<S>, R>
		extends RenderLayer<S, M> {

	private final net.minecraft.client.model.Model<R> model;
	private final R modelState;
	private final net.minecraft.resources.Identifier texture;

	protected GenericStuckInBodyLayer(
			LivingEntityRenderer<?, S, M> renderer,
			net.minecraft.client.model.Model<R> model, R modelState, net.minecraft.resources.Identifier texture) {
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

		RandomSource random = RandomSource.create(
				((StuckArrowsAccess) state).aitk$getEntityId()
		);

		ModelPart root = this.getParentModel().root();

		for (int i = 0; i < count; i++) {
			poseStack.pushPose();

			// Semilla por flecha para repartir partes de forma estable
			java.util.Random partRand = new java.util.Random(i);
			com.mojang.datafixers.util.Pair<ModelPart, Runnable> pair = bestFromListMutable(
					new ArrayList<>(List.of(root)),
					partRand,
					poseStack,
					true
			);

			if (pair == null) {
				poseStack.popPose();
				continue;
			}

			ModelPart modelPart = pair.getFirst();
			pair.getSecond().run();

			if (modelPart.isEmpty()) {
				poseStack.popPose();
				continue;
			}

			ModelPart.Cube cube = modelPart.getRandomCube(random);

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
					this.model,
					this.modelState,
					poseStack,
					this.model.renderType(this.texture),
					lightCoords,
					net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
					state.outlineColor,
					null
			);

			poseStack.popPose();
		}
	}
}

//? }
