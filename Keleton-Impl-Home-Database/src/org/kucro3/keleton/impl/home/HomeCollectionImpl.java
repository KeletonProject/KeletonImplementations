package org.kucro3.keleton.impl.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.datalayer.api.home.DataHome;
import org.kucro3.keleton.datalayer.ref.ResilientReferenceGroup;
import org.kucro3.keleton.datalayer.ref.sponge.PlayerRelatedCache;
import org.kucro3.keleton.world.home.Home;
import org.kucro3.keleton.world.home.HomeCollection;
import org.kucro3.keleton.world.home.exception.HomeException;
import org.kucro3.keleton.world.home.exception.HomeInternalException;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class HomeCollectionImpl implements HomeCollection {
    HomeCollectionImpl(HomeServiceImpl service, String tableName)
    {
        this.service = service;
        this.tableName = tableName;
        this.homes = new PlayerRelatedCache<>(SpongeMain.getInstance());

        this.homes.enable();

        final HomeCollectionImpl pthis = this;

        this.homes.setCallbackOnLogin((player, cache) -> {
            state = false;
            final UUID uuid = player.getUniqueId();
            CompletableFuture.<Void>runAsync(() -> {
                ResilientReferenceGroup<Homes> group;

                if((group = cache.getGroup(uuid)) != null)
                    if(group.size() != 0)
                    {
                        group.setStrong();
                        return;
                    }
                    else;
                else
                    cache.addGroup(uuid, group = new ResilientReferenceGroup<>());

                final Homes collection = new Homes();

                try {
                    service.db.process((conn) -> DataHome.load(conn, tableName, uuid, (data) -> {
                        HomeImpl home = new HomeImpl(pthis, data);
                        collection.put(data.getName(), home);
                    }));

                    group.add(collection);
                    failures.remove(uuid);
                } catch (SQLException e) {
                    failures.put(uuid, new HomeInternalException("SQL Failure on preloading", e));
                }
            }).thenAccept((unused) -> state = true);
        });

        this.homes.setCallbackOnLogoff((player, cache) -> cache.setSoft(player.getUniqueId()));
    }

    void lock()
    {
        while(!state);
    }

    void check(UUID uuid)
    {
        HomeException e;
        if((e = failures.get(uuid)) != null)
            throw e;
        lock();
    }

    @Override
    public Optional<Home> getHome(UUID uuid, @CaseInsensitive String name, World world) throws HomeException
    {
        return Optional.empty();
    }

    @Override
    public boolean hasHome(UUID uuid, @CaseInsensitive String name) throws HomeException
    {
        return false;
    }

    @Override
    public boolean hasHome(UUID uuid, @CaseInsensitive String name, World world) throws HomeException
    {
        return false;
    }

    @Override
    public boolean deleteHome(UUID uuid, @CaseInsensitive String name, Cause casue) throws HomeException
    {
        return false;
    }

    @Override
    public boolean clearHomes(Cause cause) throws HomeException
    {
        return false;
    }

    @Override
    public boolean clearHomes(World world, Cause casue) throws HomeException
    {
        return false;
    }

    @Override
    public boolean clearHomes(UUID uuid, Cause casue) throws HomeException
    {
        return false;
    }

    @Override
    public Collection<Home> getHomes(UUID uuid) throws HomeException
    {
        return null;
    }

    @Override
    public Home setHome(UUID uuid, String s, Location<World> location, Cause casue) throws HomeException
    {
        return null;
    }

    volatile boolean state;

    private final String tableName;

    private final PlayerRelatedCache<Homes> homes;

    private final HashMap<UUID, HomeException> failures = new HashMap<>();

    final HomeServiceImpl service;

    class Homes extends HashMap<String, HomeImpl>
    {
    }
}
