package org.kucro3.keleton.impl.home.event;

import org.kucro3.keleton.world.home.Home;
import org.kucro3.keleton.world.home.event.HomeSetEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class HomeSetEventImpl {
    private HomeSetEventImpl()
    {
    }

    public static class Completed extends AbstractHomeEvent implements HomeSetEvent.Completed
    {
        public Completed(String name, Location<World> location, Home home, Cause cause)
        {
            super(name, location, cause);
            this.home = home;
        }

        @Override
        public Home getHome()
        {
            return home;
        }

        private final Home home;
    }

    public static class Failed extends AbstractHomeEvent implements HomeSetEvent.Failed
    {
        public Failed(String name, Location<World> location, Cause cause)
        {
            super(name, location, cause);
        }
    }

    public static class Pre extends AbstractHomeEvent.Cancellable implements HomeSetEvent.Pre
    {
        public Pre(String name, Location<World> location, Cause cause)
        {
            super(name, location, cause);
        }
    }
}
