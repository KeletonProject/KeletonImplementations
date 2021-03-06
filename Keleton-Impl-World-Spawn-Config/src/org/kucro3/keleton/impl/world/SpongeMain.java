package org.kucro3.keleton.impl.world;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.kucro3.keleton.config.ConfigurationKeys;
import org.kucro3.keleton.exception.KeletonException;
import org.kucro3.keleton.exception.KeletonInternalException;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.Module;
import org.kucro3.keleton.world.SpawnProvider;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

@Plugin(id = "keleton-impl-worldspawn",
		name = "keleton-impl-worldspawn",
		version = "1.0",
		description = "World Spawn Implementation for Keleton Framework",
		authors = "Kumonda221")
@Module(id = "keleton-impl-worldspawn",
		dependencies = {"keleton-impl-config"})
public class SpongeMain implements KeletonInstance {
	@Inject
	public SpongeMain(Logger logger)
	{
		SpongeMain.logger = logger;
	}

	@Override
	public CompletableFuture<Void> onLoad()
	{
		if(!CONFIG.exists() || !CONFIG.isFile()) {
			try {
				CONFIG.createNewFile();
			} catch (IOException e) {
				throw new KeletonInternalException(e);
			}
		}

		impl.initialize();

		return CompletableFuture.completedFuture(null);
	}

	@Override
	public CompletableFuture<Void> onEnable() throws KeletonException
	{
		SpawnProvider.TOKEN.put(impl = new SpawnProviderImpl(
				ObjectService.get(ConfigurationKeys.SERVICE).get().getOperator("KLINK").get().readConfiguration(File.class, CONFIG)));

		return CompletableFuture.completedFuture(null);
	}

	public static Logger getLogger()
	{
		return logger;
	}
	
	private static SpawnProviderImpl impl;
	
	private static final File CONFIG = new File(".\\config\\spawns.klnk");
	
	private static Logger logger;
}
