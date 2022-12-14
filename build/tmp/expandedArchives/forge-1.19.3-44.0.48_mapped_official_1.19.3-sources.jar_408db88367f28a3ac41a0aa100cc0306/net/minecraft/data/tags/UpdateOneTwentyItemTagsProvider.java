package net.minecraft.data.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class UpdateOneTwentyItemTagsProvider extends ItemTagsProvider {
   public UpdateOneTwentyItemTagsProvider(PackOutput p_256307_, CompletableFuture<HolderLookup.Provider> p_256395_, TagsProvider<Block> p_256357_) {
      super(p_256307_, p_256395_, p_256357_);
   }

   protected void addTags(HolderLookup.Provider p_256409_) {
      this.copy(BlockTags.PLANKS, ItemTags.PLANKS);
      this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
      this.copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
      this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
      this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
      this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
      this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
      this.copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
      this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
      this.copy(BlockTags.DOORS, ItemTags.DOORS);
      this.copy(BlockTags.SLABS, ItemTags.SLABS);
      this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
      this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
      this.copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
      this.copy(BlockTags.FENCES, ItemTags.FENCES);
      this.copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
      this.copy(BlockTags.BAMBOO_BLOCKS, ItemTags.BAMBOO_BLOCKS);
      this.copy(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS);
      this.tag(ItemTags.CHEST_BOATS).add(Items.BAMBOO_CHEST_RAFT);
      this.tag(ItemTags.BOATS).add(Items.BAMBOO_RAFT);
      this.tag(ItemTags.BOOKSHELF_BOOKS).add(Items.BOOK, Items.WRITTEN_BOOK, Items.ENCHANTED_BOOK, Items.WRITABLE_BOOK);
      this.tag(ItemTags.NON_FLAMMABLE_WOOD).add(Items.WARPED_HANGING_SIGN, Items.CRIMSON_HANGING_SIGN);
   }
}