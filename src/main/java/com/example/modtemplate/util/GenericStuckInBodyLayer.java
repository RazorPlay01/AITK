package com.example.modtemplate.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import org.jspecify.annotations.NonNull;

import java.util.List;

public abstract class GenericStuckInBodyLayer<S extends LivingEntityRenderState, M extends EntityModel<S>, R>
		extends RenderLayer<S, M> {

	private final Model<R> model;
	private final R modelState;
	private final Identifier texture;

	protected GenericStuckInBodyLayer(
			LivingEntityRenderer<?, S, M> renderer,
			Model<R> model, R modelState, Identifier texture) {
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

		// ══════════════════════════════════════════════════════════
		// FILTRAR: solo partes que realmente tienen cubos
		// ══════════════════════════════════════════════════════════
		List<ModelPart> partsWithCubes = this.getParentModel().allParts().stream()
				.filter(part -> !part.cubes.isEmpty())
				.toList();

		// Si el modelo no tiene partes con cubos, no hacer nada
		if (partsWithCubes.isEmpty()) return;
		// ══════════════════════════════════════════════════════════

		RandomSource random = RandomSource.create(
				((StuckArrowsAccess) state).arrowsForAll$getEntityId()
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
					lightCoords, OverlayTexture.NO_OVERLAY,
					state.outlineColor, null
			);

			poseStack.popPose();
		}
	}
}
