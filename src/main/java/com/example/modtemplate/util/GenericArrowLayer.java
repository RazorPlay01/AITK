package com.example.modtemplate.util;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.ArrowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class GenericArrowLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GenericStuckInBodyLayer<S, M, ArrowRenderState> {

    public GenericArrowLayer(LivingEntityRenderer<?, S, M> renderer, EntityRendererProvider.Context context) {
        super(renderer, new ArrowModel(context.bakeLayer(ModelLayers.ARROW)), new ArrowRenderState(), TippableArrowRenderer.NORMAL_ARROW_LOCATION);
    }

    @Override
    protected int numStuck(S state) {
        return ((StuckArrowsAccess) state).arrowsForAll$getArrowCount();
    }
}
