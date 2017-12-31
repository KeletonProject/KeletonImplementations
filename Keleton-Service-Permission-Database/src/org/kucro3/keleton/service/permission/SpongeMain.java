package org.kucro3.keleton.service.permission;

import org.kucro3.keleton.implementation.InvokeOnLoad;
import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.permission.EnhancedPermissionService;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.kucro3.keleton.sql.DatabaseKeys;
import org.kucro3.keleton.sql.DatabasePool;
import org.kucro3.keleton.sql.JDBCUrlFactory;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.permission.PermissionService;

import com.google.inject.Inject;

@Plugin(id = "keleton-impl-permission",
		name = "keleton-impl-permission",
		version = "1.0",
		description = "Permission Service Implementation",
		authors = "Kumonda221")
@KeletonModule(name = "keleton-impl-permission",
			   dependencies = {"keleton-framework", "keleton-impl-db"})
public class SpongeMain {
	@Inject
	public SpongeMain(Logger logger)
	{
		this.logger = logger;
		INSTANCE = this;
	}

	@InvokeOnLoad
	public void onLoad()
	{
		try {
			DatabasePool dbpool = ObjectService.get(DatabaseKeys.DATABASE)
					.orElseThrow(() -> new IllegalStateException("Database service not reachable"));
			JDBCUrlFactory urlfactory = ObjectService.get(DatabaseKeys.JDBC_URL_FACTORY).get();
			DatabaseConnection connection = dbpool.forDatabase(urlfactory.createUrl("keleton-permission"))
					.orElseThrow(() -> new IllegalStateException("Failed to create database connection"));
			final PermissionServiceImpl impl = new PermissionServiceImpl(connection)._ENABLE_();

			EnhancedPermissionService.TOKEN.put(impl);
			Sponge.getServiceManager().setProvider(this, PermissionService.class, impl);
		} catch (Exception e) {
			logger.error("Cannot initialize economy service", e);
		}
	}
	
	public static SpongeMain getInstance()
	{
		return INSTANCE;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	private final Logger logger;
	
	private static SpongeMain INSTANCE;
}
