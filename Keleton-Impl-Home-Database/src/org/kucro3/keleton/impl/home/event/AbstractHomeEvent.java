package org.kucro3.keleton.impl.home.event;

import org.kucro3.keleton.event.CancellableWithCause;
import org.kucro3.keleton.world.home.event.HomeEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class AbstractHomeEvent implements HomeEvent {
    AbstractHomeEvent(String name, Location<World> location, Cause cause)
    {
        this.name = name;
        this.location = location;
        this.cause = cause;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Location<World> getLocation()
    {
        return location;
    }

    @Override
    public Cause getCause()
    {
        return cause;
    }

    final String name;

    final Location<World> location;

    final Cause cause;

    public static class Cancellable extends AbstractHomeEvent implements CancellableWithCause
    {
        Cancellable(String name, Location<World> location, Cause cause)
        {
            super(name, location, cause);
        }

        @Override
        public Optional<Cause> getCancellationCause()
        {
            return Optional.ofNullable(cause);
        }

        @Override
        public void cancel(Cause cause)
        {
            this.cause = cause;
            this.setCancelled(true);
        }

        @Override
        public boolean isCancelled()
        {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel)
        {
            if(!cancel)
                cause = null;

            this.cancelled = cancel;
        }

        Cause cause;

        boolean cancelled;
    }
}
