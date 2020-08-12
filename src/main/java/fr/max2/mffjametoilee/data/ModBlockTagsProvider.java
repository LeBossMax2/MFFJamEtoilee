package fr.max2.mffjametoilee.data;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;

public class ModBlockTagsProvider extends BlockTagsProvider
{
    public ModBlockTagsProvider(DataGenerator gen)
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
}