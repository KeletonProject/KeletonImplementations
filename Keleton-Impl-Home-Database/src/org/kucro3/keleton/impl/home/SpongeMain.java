package org.kucro3.keleton.impl.home;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.Module;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Plugin;

import java.util.concurrent.Executor;

@Plugin(id = "keleton-impl-home",
        name = "keleton-impl-home",
        version = "1.0",
        description = "Home Service Implementation for Keleton Framework",
        authors = "Kumonda221")
@Module(id = "keleton-impl-home",
        dependencies = {"keletonframework", "keleton-datalayer", "keleton-impl-db"})
public class SpongeMain implements KeletonInstance {
    @Override
    public void onLoad()
    {
        INSTANCE = this;
    }

    @Override
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
