package org.kucro3.keleton.impl.home;

import org.kucro3.keleton.implementation.InvokeOnEnable;
import org.kucro3.keleton.implementation.InvokeOnLoad;
import org.kucro3.keleton.implementation.KeletonModule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Plugin;

import java.util.concurrent.Executor;

@Plugin(id = "keleton-impl-home",
        name = "keleton-impl-home",
        version = "1.0",
        description = "Home Service Implementation for Keleton Framework",
        authors = "Kumonda221")
@KeletonModule(name = "keleton-impl-home",
               dependencies = {"keletonframework", "keleton-datalayer", "keleton-impl-db"})
public class SpongeMain {
    @InvokeOnLoad
    public void onLoad()
    {
        INSTANCE = this;
    }

    @InvokeOnEnable
    public void onEnable()
    {
        Executor async = Sponge.getScheduler().createAsyncExecutor(this);

        // TODO
    }

    public static SpongeMain getInstance()
    {
        return INSTANCE;
    }

    private static SpongeMain INSTANCE;
}
