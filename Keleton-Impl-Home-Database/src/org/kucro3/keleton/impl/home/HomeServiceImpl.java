package org.kucro3.keleton.impl.home;

import org.kucro3.keleton.UniqueService;
import org.kucro3.keleton.datalayer.api.home.HomeStorage;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.kucro3.keleton.world.home.HomeCollection;
import org.kucro3.keleton.world.home.HomeService;
import org.kucro3.keleton.world.home.exception.HomeStorageException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;

public class HomeServiceImpl implements HomeService, UniqueService {
    HomeServiceImpl(DatabaseConnection db, Executor async)
    {
        this.db = db;
        this.async = async;
    }

    @Override
    public Optional<HomeCollection> getCollection(String s)
    {
        HomeCollectionImpl impl = collections.get(s);

        if(impl != null)
            return Optional.of(impl);

        final String tableName = "home_" + s;
        impl = new HomeCollectionImpl(this, tableName, async);

        try {
            db.apply((conn) -> HomeStorage.ensureTable(conn, tableName));
        } catch (SQLException e) {
            throw new HomeStorageException("No further information", e);
        }

        return Optional.of(impl);
    }

    @Override
    public boolean available(String s)
    {
        return collections.containsKey(s);
    }

    @Override
    public UUID getUniqueId()
    {
        return uuid;
    }

    final DatabaseConnection db;

    private final Map<String, HomeCollectionImpl> collections = new HashMap<>();

    private final UUID uuid = UUID.randomUUID();

    private final Executor async;
}
