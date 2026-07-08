package com.github.razorplay01.aitk.mixin;
//? if >=1.21.2 {

import com.github.razorplay01.aitk.util.StuckArrowsAccess;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements StuckArrowsAccess {

	@Unique
	private int aitk$arrowCount = 0;

	@Unique
	private int aitk$entityId = 0;

	@Override
	public int aitk$getArrowCount() {
		return this.aitk$arrowCount;
	}

	@Override
	public void aitk$setArrowCount(int count) {
		this.aitk$arrowCount = count;
	}

	@Override
	public int aitk$getEntityId() {
		return this.aitk$entityId;
	}

	@Override
	public void aitk$setEntityId(int id) {
		this.aitk$entityId = id;
	}
}
//? }
