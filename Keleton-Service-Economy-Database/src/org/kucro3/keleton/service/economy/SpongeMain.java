package org.kucro3.keleton.service.economy;

import org.kucro3.keleton.economy.EnhancedEconomyService;
import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.Module;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.kucro3.keleton.sql.DatabaseKeys;
import org.kucro3.keleton.sql.DatabasePool;
import org.kucro3.keleton.sql.JDBCUrlFactory;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;

import com.google.inject.Inject;

@Plugin(id = "keleton-impl-economy",
		name = "keleton-impl-economy",
		version = "1.0",
		description = "Economy Service Implementation",
		authors = "Kumonda221")
@Module(id = "keleton-impl-economy",
		dependencies = {"keletonframework", "keleton-impl-db"})
public class SpongeMain implements KeletonInstance {
	@Inject
	public SpongeMain(Logger logger)
	{
		this.logger = logger;
	}

	@Override
	public void onLoad()
	{
		INSTANCE = this;
	}

	@Override
	public void onEnable()
	{
		try {
			DatabasePool dbpool = ObjectService.get(DatabaseKeys.DATABASE)
					.orElseThrow(() -> new IllegalStateException("Database service not reachable"));
			JDBCUrlFactory urlfactory = ObjectService.get(DatabaseKeys.JDBC_URL_FACTORY).get();
			DatabaseConnection connection = dbpool.forDatabase(urlfactory.createUrl("keleton-economy"))
					.orElseThrow(() -> new IllegalStateException("Failed to create database connection"));
			final EconomyServiceImpl impl = new EconomyServiceImpl(connection, "keleton_economy")._ENABLE_();

			EnhancedEconomyService.TOKEN.put(impl);
			Sponge.getServiceManager().setProvider(this, EconomyService.class, impl);
		} catch (Exception e) {
			logger.error("Cannot initialize permission service", e);
		}
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public static SpongeMain getInstance()
	{
		return INSTANCE;
	}
	
	private static SpongeMain INSTANCE;
	
	private final Logger logger;
}
