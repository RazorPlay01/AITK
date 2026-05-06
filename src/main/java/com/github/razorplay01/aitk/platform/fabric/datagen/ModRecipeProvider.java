package com.github.razorplay01.aitk.platform.fabric.datagen;

//? fabric && != 1.19.2 {

import com.github.razorplay01.aitk.ModTemplate;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

//? if 1.21.1
//import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

	private final CompletableFuture<HolderLookup.Provider> registriesFuture;

	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
		this.registriesFuture = registriesFuture;
	}


	//? if 1.21.1 {
	/*@Override
	public void buildRecipes(RecipeOutput recipeOutput) {
		IntRecipeProvider provider = new IntRecipeProvider(this.output, this.registriesFuture);
		provider.buildRecipes(recipeOutput);
	}
	*///? } else {
	@Override
	protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		return new IntRecipeProvider(provider, recipeOutput);
	}
	//? }


	@Override
	public @NotNull String getName() {
		return ModTemplate.MOD_ID + ":recipe_provider";
	}

	static class IntRecipeProvider extends RecipeProvider {

		//? if 1.21.1 {
		/*public IntRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(packOutput, completableFuture);
		}

		@Override
		public void buildRecipes(RecipeOutput recipeOutput) {

		}
		*///? } else {
		protected IntRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			super(provider, recipeOutput);
		}

		@Override
		public void buildRecipes() {

		}
		//? }
	}
}
//?}
