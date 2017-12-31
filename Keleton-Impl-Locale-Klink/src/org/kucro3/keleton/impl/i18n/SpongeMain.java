package org.kucro3.keleton.impl.i18n;

import java.io.File;

import org.kucro3.keleton.i18n.LocaleService;
import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

@Plugin(id = "keleton-impl-i18n",
		name = "keleton-impl-i18n",
		version = "1.0",
		description = "International Implementation for Keleton Framework powered by Klink",
		authors = "Kumonda221")
@KeletonModule(name = "keleton-impl-i18n",
			   dependencies = "keletonframework")
public class SpongeMain extends KeletonInstance {
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
	public void onLoad()
	{
		ensureFolder();
		instance = this;
	}

	@Override
	public void onEnable()
	{
		LocaleService.TOKEN.put(new LocaleServiceImpl(LOCALE_FOLDER));
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