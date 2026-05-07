package com.github.razorplay01.aitk.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >= 1.21.11 {
/*import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
*///?} else {
import net.minecraft.world.entity.projectile.AbstractArrow;
//?}

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

	@Inject(method = "tickDespawn", at = @At("HEAD"), cancellable = true)
	private void aitk$longerStuckArrows(CallbackInfo ci) {
		AbstractArrow arrow = (AbstractArrow) (Object) this;
		AbstractArrowAccessor accessor = (AbstractArrowAccessor) arrow;

		if (accessor.aitk$inGround() && arrow.getOwner() != null) {
			if (accessor.aitk$inGroundTime() < 2400) {  // 2 minutos (1200 = 1 minuto)
				ci.cancel();
			}
		}
	}
}
