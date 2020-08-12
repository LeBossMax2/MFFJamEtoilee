package fr.max2.mffjametoilee.data;

import java.util.stream.Stream;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;

public class ModItemTagsProvider extends ItemTagsProvider
{
    public ModItemTagsProvider(DataGenerator gen)
    {
        super(gen);
    }

    @Override
    public void registerTags()
    {
    	
    }

    @Override
    public String getName()
    {
        return MFFJamEtoileeMod.MOD_ID + " Block Tags";
    }
    
    protected static Tag.Builder<Item> add(Tag.Builder<Item> builder, Block... blocks)
    {
    	Stream.of(blocks).map(Block::asItem).forEach(builder::add);
    	return builder;
    }
}