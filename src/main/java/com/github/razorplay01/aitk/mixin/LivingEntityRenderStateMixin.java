package com.github.razorplay01.aitk.mixin;
//? if >=1.21.2 {

/*import com.github.razorplay01.aitk.util.StuckArrowsAccess;
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@IfMinecraftVersion(minVersion = "1.21.2")
@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements StuckArrowsAccess {

	@Unique
	private int arrowsForAll$arrowCount = 0;

	@Unique
	private int arrowsForAll$entityId = 0;

	@Override
	public int arrowsForAll$getArrowCount() {
		return this.arrowsForAll$arrowCount;
	}

	@Override
	public void arrowsForAll$setArrowCount(int count) {
		this.arrowsForAll$arrowCount = count;
	}

	@Override
	public int arrowsForAll$getEntityId() {
		return this.arrowsForAll$entityId;
	}

	@Override
	public void arrowsForAll$setEntityId(int id) {
		this.arrowsForAll$entityId = id;
	}
}
*///? }
