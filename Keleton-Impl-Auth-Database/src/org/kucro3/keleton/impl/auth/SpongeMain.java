package org.kucro3.keleton.impl.auth;

import org.kucro3.keleton.auth.AuthKeys;
import org.kucro3.keleton.implementation.InvokeOnEnable;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.sql.DatabaseKeys;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

@Plugin(id = "keleton-impl-auth",
	name = "keleton-impl-auth",
	version = "1.0",
	description = "Auth system Implementation fro Keleton Framework",
	authors = {"Kumonda221"})
@KeletonModule(name = "keleton-impl-auth",
			   dependencies = {"keletonframework", "keleton-impl-db"})
public class SpongeMain {
	@Inject
	public SpongeMain(Logger logger)
	{
		this.logger = logger;
		instance = this;
	}
	
	public Logger getLogger()
	{
		return logger;
	}

	@InvokeOnEnable
	public void onEnable()
	{
		ObjectService.put(AuthKeys.SERVICE_POOL, new AuthServicePoolImpl(DatabaseKeys.DATABASE, DatabaseKeys.JDBC_URL_FACTORY));
	}
	
	public static SpongeMain getInstance()
	{
		return instance;
	}
	
	private static SpongeMain instance;
	
	private final Logger logger;
}