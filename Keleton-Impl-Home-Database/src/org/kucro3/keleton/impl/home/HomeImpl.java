package org.kucro3.keleton.impl.home;

import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.datalayer.api.home.HomeData;
import org.kucro3.keleton.event.FailureCause;
import org.kucro3.keleton.impl.home.event.HomeTeleportEventImpl;
import org.kucro3.keleton.world.home.Home;
import org.kucro3.keleton.world.home.HomeCollection;
import org.kucro3.keleton.world.home.event.HomeTeleportEvent;
import org.kucro3.keleton.world.home.exception.InvalidHomeException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class HomeImpl implements Home {
    HomeImpl(HomeCollectionImpl collection, HomeData data)
    {
        this.collection = collection;
        this.data = data;
    }

    @Override
    public String getName()
    {
        return data.getName();
    }

    @Override
    public UUID getOwnerUniqueId()
    {
        return data.getUniqueId();
    }

    @Override
    public Location<World> getLocation()
    {
        check();
        return new Location<>(Sponge.getServer().getWorld(data.getWorld()).get(), data.getX(), data.getY(), data.getZ());
    }

    @Override
    public HomeCollection getCollection()
    {
        return collection;
    }

    @Override
    public boolean valid()
    {
        return Sponge.getServer().getWorld(data.getWorld()).isPresent();
    }

    @Override
    public boolean teleport(Entity entity, Cause cause)
    {
        cause = cause.merge(Cause.of(NamedCause.of("handler", (FromUniqueService) () -> collection.service.getUniqueId())));

        String name = getName();
        Location<World> location = getLocation();

        HomeTeleportEvent.Pre preEvent = new HomeTeleportEventImpl.Pre(name, location, this, entity, cause);
        Sponge.getEventManager().post(preEvent);

        if(preEvent.isCancelled() || !entity.setLocationSafely(location))
        {
            Cause failureCause = FailureCause.builder()
                    .cancellation(true)
                    .cause(preEvent.getCancellationCause().orElse(null))
                    .build()
                    .toCause();

            HomeTeleportEvent.Failed failedEvent = new HomeTeleportEventImpl.Failed(name, location, this, entity, failureCause);
            Sponge.getEventManager().post(failedEvent);

            return false;
        }

        HomeTeleportEvent.Completed completedEvent = new HomeTeleportEventImpl.Completed(name, location, this, entity, cause);
        Sponge.getEventManager().post(completedEvent);

        return true;
    }

    void check()
    {
        if(!valid())
            throw new InvalidHomeException();
    }

    private final HomeCollectionImpl collection;

    HomeData getData()
    {
        return data;
    }

    HomeData data;
}
