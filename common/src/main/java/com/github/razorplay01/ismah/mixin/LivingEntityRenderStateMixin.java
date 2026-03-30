package com.github.razorplay01.ismah.mixin;

import com.github.razorplay01.ismah.util.StuckArrowsAccess;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
