package com.github.razorplay01.aitk.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//? >= 1.21.11 {
/*import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.gen.Invoker;
*///?} else {
import net.minecraft.world.entity.projectile.AbstractArrow;
//?}
@Mixin(AbstractArrow.class)
public interface AbstractArrowAccessor {
	//? >= 1.21.11 {
	/*@Invoker("isInGround")
	boolean aitk$inGround();
	*///?} else {
	@Accessor("inGround")
	boolean aitk$inGround();
	//?}

	@Accessor("inGroundTime")
	int aitk$inGroundTime();
}
