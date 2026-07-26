package com.github.razorplay01.aitk.util;

import com.github.razorplay01.aitk.mixin.ModelPartAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.geom.ModelPart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class Util {
	private Util() {
		// []
	}

	/**
	 * Recorre la jerarquía de ModelPart y devuelve un part con cubos junto con un
	 * Runnable que aplica translateAndRotate de todos sus ancestros + él mismo.
	 * Basado en la lógica de EMF (MixinStuckArrowsFeatureRenderer).
	 */
	public static Pair<ModelPart, Runnable> bestFromListMutable(
			List<ModelPart> partsMutable,
			Random randomSource,
			PoseStack poseStack,
			boolean firstIteration) {

		Collections.shuffle(partsMutable, randomSource);

		for (ModelPart modelPart : partsMutable) {
			ModelPartAccessor accessor = (ModelPartAccessor) (Object) modelPart;
			if (!modelPart.visible) continue;

			// Preferir parts visibles con cubos y que no estén marcados skipDraw
			if (!accessor.aitk$getCubes().isEmpty() && !modelPart.skipDraw) {
				return Pair.of(modelPart, () -> modelPart.translateAndRotate(poseStack));
			}

			if (accessor.aitk$getChildren().isEmpty()) continue;

			Pair<ModelPart, Runnable> child = bestFromListMutable(
					new ArrayList<>(accessor.aitk$getChildren().values()),
					randomSource,
					poseStack,
					false
			);

			if (child != null) {
				Runnable childRunnable = child.getSecond();
				return Pair.of(child.getFirst(), () -> {
					modelPart.translateAndRotate(poseStack);
					childRunnable.run();
				});
			}
		}

		// Fallback: si no hay nada con cubos, usar el primer part de la lista raíz
		if (firstIteration && !partsMutable.isEmpty()) {
			ModelPart part = getFirst(partsMutable);
			return Pair.of(part, () -> part.translateAndRotate(poseStack));
		}

		return null;
	}

	private static ModelPart getFirst(List<ModelPart> parts) {
		if (parts.isEmpty()) {
			throw new NoSuchElementException();
		} else {
			return parts.get(0);
		}
	}
}
