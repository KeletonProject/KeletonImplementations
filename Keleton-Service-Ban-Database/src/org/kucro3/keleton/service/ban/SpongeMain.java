package org.kucro3.keleton.service.ban;

import java.util.UUID;

import org.kucro3.keleton.ban.EnhancedBanService;
import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.Module;
import org.kucro3.keleton.keyring.ObjectService;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.kucro3.keleton.sql.DatabaseKeys;
import org.kucro3.keleton.sql.DatabasePool;
import org.kucro3.keleton.sql.JDBCUrlFactory;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ban.BanService;

import com.google.inject.Inject;

@Plugin(id = "keleton-impl-ban",
		name = "keleton-impl-ban",
		version = "1.0",
		description = "Ban Service Implementation",
		authors = "Kumonda221")
@Module(id = "keleton-impl-ban",
		dependencies = {"keletonframework", "keleton-impl-db"})
public class SpongeMain implements KeletonInstance {
	@Inject
	public SpongeMain(Logger logger)
	{
		SpongeMain.logger = logger;
	}

	@Override
	public void onLoad()
	{
		instance = this;
	}

	@Override
	public void onEnable()
	{
		try {
			DatabasePool dbpool = ObjectService.get(DatabaseKeys.DATABASE)
					.orElseThrow(() -> new IllegalStateException("Database service not reachable"));
			JDBCUrlFactory urlfactory = ObjectService.get(DatabaseKeys.JDBC_URL_FACTORY).get();
			DatabaseConnection connection = dbpool.forDatabase(urlfactory.createUrl("keleton-banlist"))
					.orElseThrow(() -> new IllegalStateException("Failed to create database connection"));
			
			service = new BanServiceImpl(connection, logger);
			
			Sponge.getEventManager().registerListeners(this, new BanIpListener());

			EnhancedBanService.TOKEN.put(service);
			Sponge.getServiceManager().setProvider(this, BanService.class, service);
		} catch (Exception e) {
			logger.error("Cannot initialize ban service", e);
		}
	}
	
	static Cause from(UUID uuid)
	{
		return Cause.builder().named("handler", (FromUniqueService) () -> uuid).build();
	}
	
	public static Logger getLogger()
	{
		return logger;
	}
	
	public static SpongeMain getInstance()
	{
		return instance;
	}
	
	private static BanServiceImpl service;
	
	private static Logger logger;
	
	private static SpongeMain instance;
}
