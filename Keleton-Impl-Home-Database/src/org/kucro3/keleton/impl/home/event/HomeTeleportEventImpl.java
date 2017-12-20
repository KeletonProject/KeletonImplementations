package org.kucro3.keleton.impl.home.event;

import org.kucro3.keleton.world.TeleportationResult;
import org.kucro3.keleton.world.home.Home;
import org.kucro3.keleton.world.home.event.HomeTeleportEvent;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class HomeTeleportEventImpl {
    private HomeTeleportEventImpl()
    {
    }

    public static class Completed extends AbstractHomeEvent implements HomeTeleportEvent.Completed
    {
        public Completed(String name, Location<World> location, Home home, Entity entity, Cause cause)
        {
            super(name, location, cause);
            this.home = home;
            this.entity = entity;
        }

        @Override
        public Home getHome()
        {
            return home;
        }

        @Override
        public Entity getEntity()
        {
            return entity;
        }

        @Override
        public TeleportationResult getResult()
        {
            return TeleportationResult.SUCCESS;
        }

        private final Home home;

        private final Entity entity;
    }

    public static class Failed extends AbstractHomeEvent implements HomeTeleportEvent.Failed
    {
        public Failed(String name, Location<World> location, Home home, Entity entity, Cause cause)
        {
            super(name, location, cause);
            this.home = home;
            this.entity = entity;
        }

        @Override
        public Home getHome()
        {
            return home;
        }

        @Override
        public Entity getEntity()
        {
            return entity;
        }

        private final Home home;

        private final Entity entity;
    }

    public static class Pre extends AbstractHomeEvent.Cancellable implements HomeTeleportEvent.Pre
    {
        public Pre(String name, Location<World> location, Home home, Entity entity, Cause cause)
        {
            super(name, location, cause);
            this.home = home;
            this.entity = entity;
        }

        @Override
        public Home getHome()
        {
            return home;
        }

        @Override
        public Entity getEntity()
        {
            return entity;
        }

        private final Home home;

        private final Entity entity;
    }
}
