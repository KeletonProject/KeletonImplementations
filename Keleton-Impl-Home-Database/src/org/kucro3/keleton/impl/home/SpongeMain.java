package org.kucro3.keleton.impl.home;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.concurrent.Executor;

@Plugin(id = "keleton-home",
        name = "keleton-home",
        version = "1.0",
        description = "Home Service Implementation for Keleton Framework",
        authors = "Kumonda221")
public class SpongeMain {
    @Listener
    public void onLoad(GameConstructionEvent event)
    {
        INSTANCE = this;

        Executor async = Sponge.getScheduler().createAsyncExecutor(this);
    }

    public static SpongeMain getInstance()
    {
        return INSTANCE;
    }

    private static SpongeMain INSTANCE;
}
