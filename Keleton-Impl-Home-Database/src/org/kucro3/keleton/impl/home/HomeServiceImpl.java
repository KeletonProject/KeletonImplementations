package org.kucro3.keleton.impl.home;

import org.kucro3.keleton.UniqueService;
import org.kucro3.keleton.datalayer.api.home.DataHome;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.kucro3.keleton.world.home.HomeCollection;
import org.kucro3.keleton.world.home.HomeService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class HomeServiceImpl implements HomeService, UniqueService {
    HomeServiceImpl(DatabaseConnection db)
    {
        this.db = db;
    }

    @Override
    public Optional<HomeCollection> getCollection(String s)
    {
        HomeCollectionImpl impl = collections.get(s);

        if(impl != null)
            return Optional.of(impl);

        final String tableName = "home_" + s;
        impl = new HomeCollectionImpl(this, tableName);

        try {
            db.process((conn) -> DataHome.ensureTable(conn, tableName));
        } catch (SQLException e) {
            return Optional.empty();
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
}
