package org.kucro3.keleton.impl.world;

import java.io.File;
import java.util.Optional;

import org.kucro3.keleton.config.Configuration;
import org.kucro3.keleton.config.ConfigurationException;
import org.kucro3.keleton.config.ConfigurationSection;
import org.kucro3.keleton.world.SpawnProvider;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@SuppressWarnings({ "rawtypes", "unchecked" })
class SpawnProviderImpl implements SpawnProvider {
	SpawnProviderImpl(Configuration<File> config)
	{
		this.config = config;
		this.section = config.getSection();
	}
	
	void initialize()
	{
		for(World world : Sponge.getServer().getWorlds())
			if(!section.get(world.getName(), Location.class).isPresent())
				section.put(world.getName(), Location.class, world.getSpawnLocation());
		try {
			config.save();
		} catch (ConfigurationException e) {
			SpongeMain.getLogger().error("Error saving config", e);
		}
	}
	
	@Override
	public Location<World> getSpawn(World world) 
	{
		Optional<Location<World>> location = section.get(world.getName(), (Class) Location.class);
		if(!location.isPresent())
		{
			section.put(world.getName(), Location.class, world.getSpawnLocation());
			try {
				config.save();
			} catch (ConfigurationException e) {
				SpongeMain.getLogger().error("Error saving config", e);
			}
			return world.getSpawnLocation();
		}
		else
			return location.get();
	}

	@Override
	public boolean setSpawn(World world, Location<World> location)
	{
		section.put(world.getName(), Location.class, location);
		try {
			config.save();
		} catch (ConfigurationException e) {
			SpongeMain.getLogger().error("Error saving config", e);
			return false;
		}
		return true;
	}
	
	private final ConfigurationSection<File> section;
	
	private final Configuration<File> config;
}
