package org.kucro3.keleton.impl.auth;

import org.kucro3.keleton.auth.AuthKeys;
import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.Module;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.sql.DatabaseKeys;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import java.util.concurrent.CompletableFuture;

@Plugin(id = "keleton-impl-auth",
	name = "keleton-impl-auth",
	version = "1.0",
	description = "Auth system Implementation fro Keleton Framework",
	authors = {"Kumonda221"})
@Module(id = "keleton-impl-auth",
		dependencies = {"keleton-impl-db"})
public class SpongeMain implements KeletonInstance {
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

	@Override
	public CompletableFuture<Void> onEnable()
	{
		ObjectService.put(AuthKeys.SERVICE_POOL, new AuthServicePoolImpl(DatabaseKeys.DATABASE, DatabaseKeys.JDBC_URL_FACTORY));
		return CompletableFuture.completedFuture(null);
	}
	
	public static SpongeMain getInstance()
	{
		return instance;
	}
	
	private static SpongeMain instance;
	
	private final Logger logger;
}