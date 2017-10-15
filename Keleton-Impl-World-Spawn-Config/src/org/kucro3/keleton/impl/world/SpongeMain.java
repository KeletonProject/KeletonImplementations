package org.kucro3.keleton.impl.world;

import java.io.File;
import java.io.IOException;

import org.kucro3.keleton.config.ConfigurationException;
import org.kucro3.keleton.config.ConfigurationKeys;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.world.SpawnProvider;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

@Plugin(id = "keleton-worldspawn",
		name = "keleton-worldspawn",
		version = "1.0",
		description = "World Spawn Implementation for Keleton Framework",
		authors = "Kumonda221")
public class SpongeMain {
	@Inject
	public SpongeMain(Logger logger)
	{
		SpongeMain.logger = logger;
	}
	
	@Listener
	public void onLoad(GamePreInitializationEvent event)
	{
		try {
			if(!CONFIG.exists() || !CONFIG.isFile())
				CONFIG.createNewFile();
			SpawnProvider.TOKEN.put(impl = new SpawnProviderImpl(
					ObjectService.get(ConfigurationKeys.SERVICE).get().getOperator("KLINK").get().readConfiguration(File.class, CONFIG)));
		} catch (ConfigurationException | IOException e) {
			logger.error("Failed to initialize", e);
		}
	}
	
	@Listener
	public void onStarted(GameStartedServerEvent event)
	{
		impl.initialize();
	}
	
	public static Logger getLogger()
	{
		return logger;
	}
	
	private static SpawnProviderImpl impl;
	
	private static final File CONFIG = new File(".\\config\\spawns.klnk");
	
	private static Logger logger;
}
