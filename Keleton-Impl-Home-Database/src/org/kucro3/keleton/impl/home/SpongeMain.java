package org.kucro3.keleton.impl.home;

import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.Module;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Plugin(id = "keleton-impl-home",
        name = "keleton-impl-home",
        version = "1.0",
        description = "Home Service Implementation for Keleton Framework",
        authors = "Kumonda221")
@Module(id = "keleton-impl-home",
        dependencies = {"keleton-datalayer", "keleton-impl-db"})
public class SpongeMain implements KeletonInstance {
    @Override
    public CompletableFuture<Void> onLoad()
    {
        INSTANCE = this;

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> onEnable()
    {
        Executor async = Sponge.getScheduler().createAsyncExecutor(this);

        // TODO

        return CompletableFuture.completedFuture(null);
    }

    public static SpongeMain getInstance()
    {
        return INSTANCE;
    }

    private static SpongeMain INSTANCE;
}
