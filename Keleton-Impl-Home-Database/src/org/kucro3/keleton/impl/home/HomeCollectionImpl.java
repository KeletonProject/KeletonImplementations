package org.kucro3.keleton.impl.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.datalayer.api.home.DataHome;
import org.kucro3.keleton.datalayer.ref.ResilientReferenceGroup;
import org.kucro3.keleton.datalayer.ref.sponge.PlayerRelatedCache;
import org.kucro3.keleton.world.home.Home;
import org.kucro3.keleton.world.home.HomeCollection;
import org.kucro3.keleton.world.home.exception.HomeException;
import org.kucro3.keleton.world.home.exception.HomeInternalException;
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

                if((group = homes.getGroup(uuid)) != null)
                    if (group.size() != 0)
                        return;
                    else;
                else
                    homes.addGroup(uuid, group = new ResilientReferenceGroup<>());

                final Homes collection = new Homes();

                try {
                    service.db.process((conn) -> DataHome.load(conn, tableName, uuid, (data) ->
                        collection.put(data.getName(), new HomeImpl(pthis, data))
                    ));

                    group.add(collection);
                } catch (SQLException e) {
                    failures.put(uuid, new HomeInternalException("SQL Failure on preloading", e));
                }
            }).thenAccept((unused) -> state = true);
        });

        this.homes.setCallbackOnLogoff((player, cache) -> {
            // TODO
        });
    }

    void lock()
    {
        while(!state);
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
    public boolean deleteHome(UUID uuid, @CaseInsensitive String name) throws HomeException
    {
        return false;
    }

    @Override
    public boolean clearHomes() throws HomeException
    {
        return false;
    }

    @Override
    public boolean clearHomes(World world) throws HomeException
    {
        return false;
    }

    @Override
    public boolean clearHomes(UUID uuid) throws HomeException
    {
        return false;
    }

    @Override
    public Collection<Home> getAllHomes() throws HomeException
    {
        return null;
    }

    @Override
    public Collection<Home> getHomes(World world) throws HomeException
    {
        return null;
    }

    @Override
    public Collection<Home> getHomes(UUID uuid) throws HomeException
    {
        return null;
    }

    @Override
    public Home setHome(UUID uuid, String s, Location<World> location) throws HomeException
    {
        return null;
    }

    void addHome(DataHome entity)
    {
    }

    volatile boolean state;

    private final String tableName;

    private final PlayerRelatedCache<Homes> homes;

    private final HashMap<UUID, HomeException> failures = new HashMap<>();

    private final HashMap<String, Homes> worldMapped = new HashMap<>();

    private final HomeServiceImpl service;

    class Homes extends HashMap<String, HomeImpl>
    {
        HomeCollectionImpl getCollection()
        {
            return HomeCollectionImpl.this;
        }
    }
}
