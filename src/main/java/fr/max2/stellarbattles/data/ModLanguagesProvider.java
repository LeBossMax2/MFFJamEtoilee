package fr.max2.stellarbattles.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.max2.stellarbattles.StellarBattlesMod;
import fr.max2.stellarbattles.init.ModBlocks;
import fr.max2.stellarbattles.init.ModEntities;
import fr.max2.stellarbattles.init.ModItemGroups;
import fr.max2.stellarbattles.init.ModItems;
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
		add(ModBlocks.STAR_BEACON.get(), "Stabilized Star", "Etoile stabilisée");

    	// Items
		add(ModItems.FALLEN_STAR.get(), "Fallen Star", "Etoile filante");
		add(ModItems.MINIATURE_STAR.get(), "Miniature Star", "Etoile miniature");
		add(ModItems.STAR_CANNON.get(), "Star Cannon", "Canon à étoiles");
    	
    	// ItemGroups
    	add(ModItemGroups.MAIN, "Stellar Battles", "Stellar Battles");
    	
    	// Entities
    	add(ModEntities.STAR_PROJECTILE.get(), "Star Projectile", "Projectile étoilé");
    	
    	// Commands
    	add(StellarBattlesMod.MOD_ID + ".command.setstarsfalling.start", "The stars begin falling from the sky !", "Les étoiles commencent à tomber du ciel !");
    	add(StellarBattlesMod.MOD_ID + ".command.setstarsfalling.stop", "The stars cease falling from the sky !", "Les étoiles cessent de tomber du ciel !");
    	add(StellarBattlesMod.MOD_ID + ".command.setstarsfalling.error.alreadystarted", "The stars are already falling from the sky", "Les étoiles tombent déjà du ciel !");
    	add(StellarBattlesMod.MOD_ID + ".command.setstarsfalling.error.alreadystopped", "The stars already ceased falling from the sky !", "Les étoiles ont déja cessé de tomber du ciel !");
    	add(StellarBattlesMod.MOD_ID + ".command.setstarsfalling.error.day", "Cannot set stars falling at daytime", "Impossible de faire tomber les étoiles de jour");
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
		return StellarBattlesMod.MOD_ID + " Languages";
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
