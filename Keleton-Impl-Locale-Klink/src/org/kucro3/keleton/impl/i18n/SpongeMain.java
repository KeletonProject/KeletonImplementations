package org.kucro3.keleton.impl.i18n;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.kucro3.keleton.i18n.LocaleService;
import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.Module;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

@Plugin(id = "keleton-impl-i18n",
		name = "keleton-impl-i18n",
		version = "1.0",
		description = "International Implementation for Keleton Framework powered by Klink",
		authors = "Kumonda221")
@Module(id = "keleton-impl-i18n")
public class SpongeMain implements KeletonInstance {
	@Inject
	public SpongeMain(Logger logger)
	{
		this.logger = logger;
	}
	
	public Logger getLogger()
	{
		return logger;
	}

	@Override
	public CompletableFuture<Void> onLoad()
	{
		ensureFolder();
		instance = this;

		return CompletableFuture.completedFuture(null);
	}

	@Override
	public CompletableFuture<Void> onEnable()
	{
		LocaleService.TOKEN.put(new LocaleServiceImpl(LOCALE_FOLDER));

		return CompletableFuture.completedFuture(null);
	}
	
	public static void ensureFolder()
	{
		File file = LOCALE_FOLDER;
		if(!file.exists() || !file.isDirectory())
			file.mkdir();
	}
	
	public static SpongeMain getInstance()
	{
		return instance;
	}
	
	public static final File LOCALE_FOLDER = new File(".\\locale");
	
	private static SpongeMain instance;
	
	private final Logger logger;
}