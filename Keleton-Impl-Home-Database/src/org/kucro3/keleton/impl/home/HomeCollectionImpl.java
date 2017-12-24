package org.kucro3.keleton.impl.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.datalayer.api.home.DataHome;
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

        this.homes.setCallbackOnLogin((player, cache) -> tryLoad(player.getUniqueId(), cache));

        this.homes.setCallbackOnLogoff((player, cache) -> cache.setSoft(player.getUniqueId()));
    }

    public CompletableFuture<Void> preload(UUID uuid)
    {
        return tryLoad(uuid, homes);
    }

    CompletableFuture<Void> tryLoad(UUID uuid, PlayerRelatedCache<Homes> cache)
    {
        return CompletableFuture.<Void>runAsync(() -> tryLoad(uuid, cache));
    }

    void tryLoad0(UUID uuid, PlayerRelatedCache<Homes> cache)
    {
        if(cache.available(uuid))
        {
            cache.setStrong(uuid);
            return;
        }

        final Homes collection = new Homes();

        try {
            service.db.process((conn) -> DataHome.load(conn, tableName, uuid, (data) -> {
                HomeImpl home = new HomeImpl(this, data);
                collection.put(data.getName(), home);
            }));

            cache.set(uuid, collection);
            failures.remove(uuid);
        } catch (SQLException e) {
            failures.put(uuid, new HomeInternalException("SQL Failure on preloading", e));
        }
    }

    void check(UUID uuid)
    {
        HomeException e;
        if((e = failures.get(uuid)) != null)
            throw e;
    }

    @Override
    public CompletableFuture<Optional<Home>> getHome(UUID uuid, @CaseInsensitive String name, World world) throws HomeException
    {
        final String lname = name.toLowerCase();

        if(homes.available(uuid))
            return CompletableFuture.completedFuture(getHomeDirectly(uuid, lname, world));

        return CompletableFuture.supplyAsync(() -> {
            tryLoad0(uuid, homes);
            return getHomeDirectly(uuid, lname, world);
        });
    }

    Optional<Home> getHomeDirectly(UUID uuid, @CaseInsensitive String name, World world) throws HomeException
    {
        Home home = homes.get(uuid).get(name);
        if(home != null && (world == null || home.getLocation().getExtent().equals(world)))
            return Optional.of(home);
        return Optional.empty();
    }

    @Override
    public CompletableFuture<Optional<Home>> getHome(UUID uuid, @CaseInsensitive String name) throws HomeException
    {
        return getHome(uuid, name, null);
    }

    @Override
    public CompletableFuture<Boolean> hasHome(UUID uuid, @CaseInsensitive String name) throws HomeException
    {
        return hasHome(uuid, name, null);
    }

    @Override
    public CompletableFuture<Boolean> hasHome(UUID uuid, @CaseInsensitive String name, World world) throws HomeException
    {
        return false;
    }

    @Override
    public CompletableFuture<Boolean> deleteHome(UUID uuid, @CaseInsensitive String name, Cause casue) throws HomeException
    {
        return false;
    }

    @Override
    public CompletableFuture<Boolean> clearHomes(Cause cause) throws HomeException
    {
        return false;
    }

    @Override
    public CompletableFuture<Boolean> clearHomes(World world, Cause casue) throws HomeException
    {
        return false;
    }

    @Override
    public CompletableFuture<Boolean> clearHomes(UUID uuid, Cause casue) throws HomeException
    {
        return false;
    }

    @Override
    public CompletableFuture<Map<String, Home>> getHomes(UUID uuid) throws HomeException
    {
        Homes collection = homes.get(uuid);
        return CompletableFuture.completedFuture(homes == null ? Collections.emptyMap() : Collections.unmodifiableMap(homes.get(uuid)));
    }

    @Override
    public CompletableFuture<Integer> homeCount(UUID uuid) throws HomeException
    {
        return null;
    }

    @Override
    public CompletableFuture<Home> setHome(UUID uuid, String name, Location<World> location, Cause casue) throws HomeException
    {
        return null;
    }

    private final String tableName;

    private final PlayerRelatedCache<Homes> homes;

    private final HashMap<UUID, HomeException> failures = new HashMap<>();

    final HomeServiceImpl service;

    class Homes extends HashMap<String, HomeImpl>
    {
    }
}
