package fr.max2.mffjametoilee.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.init.ModItemGroups;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguagesProvider implements IDataProvider 
{
	private final List<LanguagePartProvider> languages = new ArrayList<>();
	
	protected ModLanguagesProvider(DataGenerator gen, String modid, String... locales)
	{
		for (String locale : locales)
		{
			this.languages.add(new LanguagePartProvider(gen, modid, locale));
		}
	}
	
	public ModLanguagesProvider(DataGenerator gen, String modid)
	{
		this(gen, modid, "en_us", "fr_fr");
	}
	
	protected void addTranslations()
    {
		// Blocks

    	// Items
    	
    	// ItemGroups
    	add(ModItemGroups.MAIN, "MFFJamEtoilée", "MFFJamEtoilée");
    }
	
	@Override
	public void act(DirectoryCache cache) throws IOException
	{
		this.addTranslations();
		for (LanguageProvider language : this.languages)
		{
			language.act(cache);
		}
	}
	
	protected void add(Block key, String... names)
	{
		add(key.getTranslationKey(), names);
	}
	
	protected void add(Item key, String... names)
	{
		add(key.getTranslationKey(), names);
	}
	
	protected void add(ItemGroup key, String... names)
	{
		add(key.getTranslationKey(), names);
	}
	
	protected void add(Enchantment key, String... names)
	{
		add(key.getName(), names);
	}
	
	protected void add(Biome key, String... names)
	{
		add(key.getTranslationKey(), names);
	}
	
	protected void add(Effect key, String... names)
	{
		add(key.getName(), names);
	}
	
	protected void add(EntityType<?> key, String... names)
	{
		add(key.getTranslationKey(), names);
	}
	
	protected void add(String key, String... values)
	{
		for (int i = 0; i < this.languages.size(); i++)
		{
			this.languages.get(i).add(key, values[i]);
		}
	}
	
	@Override
	public String getName()
	{
		return MFFJamEtoileeMod.MOD_ID + " Languages";
	}

	private static class LanguagePartProvider extends LanguageProvider
	{
		public LanguagePartProvider(DataGenerator gen, String modid, String locale)
		{
			super(gen, modid, locale);
		}

		@Override
		protected void addTranslations()
		{ }
		
		@Override
		public void add(String key, String value)
		{
			super.add(key, value);
		}
	}
}
