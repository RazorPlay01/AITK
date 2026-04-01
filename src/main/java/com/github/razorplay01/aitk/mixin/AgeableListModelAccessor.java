package com.github.razorplay01.aitk.mixin;
//? if <1.21.2 {

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@IfMinecraftVersion(maxVersion = "1.21.1")
@Mixin(AgeableListModel.class)
public interface AgeableListModelAccessor {
	@Invoker("headParts")
	Iterable<ModelPart> aitkHeadParts();

	@Invoker("bodyParts")
	Iterable<ModelPart> aitkBodyParts();
}
//? }
