package org.kucro3.keleton.impl.sql.h2;

import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.Module;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import java.util.concurrent.CompletableFuture;

@Plugin(id = "keleton-impl-db",
		name = "keleton-impl-db",
		version = "1.0",
		description = "H2 SQL Implementation for Keleton Framework",
		authors = {"Kumonda221"})
@Module(id = "keleton-impl-db")
public class SpongeMain implements KeletonInstance {
	@Inject
	public SpongeMain(Logger logger)
	{
		this.logger = logger;
	}

	@Override
	public CompletableFuture<Void> onLoad()
	{
		ObjectService.put(H2Service.SERVICE_SIMPLE_LOGGING, (info) -> logger.info(info));
		H2Service.touch();

		return CompletableFuture.completedFuture(null);
	}
	
	private final Logger logger;
}