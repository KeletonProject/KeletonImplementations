package org.kucro3.keleton.impl.sql.h2;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.keyring.ObjectService;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

@Plugin(id = "keleton-impl-db",
		name = "keleton-impl-db",
		version = "1.0",
		description = "H2 SQL Implementation for Keleton Framework",
		authors = {"Kumonda221"})
@KeletonModule(name = "keleton-impl-db",
			   dependencies = "keletonframework")
public class SpongeMain extends KeletonInstance {
	@Inject
	public SpongeMain(Logger logger)
	{
		this.logger = logger;
	}
	
	@Listener
	public void onLoad(GameConstructionEvent event)
	{
		ObjectService.put(H2Service.SERVICE_SIMPLE_LOGGING, (info) -> logger.info(info));
		H2Service.touch();
	}
	
	private final Logger logger;
}