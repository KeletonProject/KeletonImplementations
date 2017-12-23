package org.kucro3.keleton.impl.home;

import org.kucro3.annotation.CaseInsensitive;
import org.kucro3.keleton.datalayer.api.home.DataHome;
import org.kucro3.keleton.world.home.Home;
import org.kucro3.keleton.world.home.HomeCollection;
import org.kucro3.keleton.world.home.exception.HomeException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class HomeCollectionImpl implements HomeCollection {
    HomeCollectionImpl(HomeServiceImpl service)
    {
        this.service = service;
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

    private final HomeServiceImpl service;

    class Homes extends HashMap<String, HomeImpl>
    {
        HomeCollectionImpl getCollection()
        {
            return HomeCollectionImpl.this;
        }
    }
}
