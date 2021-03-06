package org.kucro3.keleton.impl.config;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.kucro3.keleton.impl.config.klink.CustomType;
import org.kucro3.keleton.impl.config.klink.CustomType.Decompiler;
import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.Module;
import org.kucro3.klink.Util;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.inject.Inject;

@Plugin(id = "keleton-impl-config",
		name = "keleton-impl-config",
		version = "1.0",
		description = "Configuration Implementation for Keleton Framework",
		authors = "Kumonda221")
@Module(id = "keleton-impl-config")
public class SpongeMain implements KeletonInstance {
	@Inject
	public SpongeMain(Logger logger)
	{
		SpongeMain.logger = logger;
	}

	@Override
	public CompletableFuture<Void> onLoad()
	{
		ensureFolder();
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public CompletableFuture<Void> onEnable()
	{
		ImplementationInstance.initialize();
		ImplementationInstance._Init_setLogger(logger);
		bindExtensions();
		return CompletableFuture.completedFuture(null);
	}
	
	static void bindExtensions()
	{
		ImplementationInstance._API_bindCustom(Extensions.location());
	}
	
	static void ensureFolder()
	{
		File file = new File(".\\config");
		if(!file.exists() || file.isDirectory())
			file.mkdir();
	}
	
	private static Logger logger;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static class Extensions
	{
		static CustomType<Location<World>> location()
		{
			return (CustomType) new CustomType<>("location", Location.class,
			(obj) -> 
				obj instanceof Location ? Optional.of((Location<World>) obj) : Optional.empty(),
			(ExpressionCompiler.Level0) (lib, seq) -> {
				final String worldName = seq.next();
				final double x = Util.parseDouble(seq.next());
				final double y = Util.parseDouble(seq.next());
				final double z = Util.parseDouble(seq.next());
				return (sys, env) -> {
					Optional<World> world = Sponge.getServer().getWorld(worldName);
					if(world.isPresent())
						env.setReturnSlot(world.get().getLocation(x, y, z));
				};
			},
			(Decompiler<Location>) (location) ->
				new StringBuilder(((Location<World>) location).getExtent().getName())
					.append(" ")
					.append(location.getX())
					.append(" ")
					.append(location.getY())
					.append(" ")
					.append(location.getZ())
					.toString()
			);
		}
	}
}
