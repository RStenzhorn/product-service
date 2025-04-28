package de.rjst.ps.container.datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TableLockRepository {

    private static final String LOCK_QUERY = "LOCK TABLE %s.%s IN ACCESS EXCLUSIVE MODE";

    private final EntityManager entityManager;

    public void apply(@NonNull final String schema, @NonNull final String table) {
        final Query query = entityManager.createNativeQuery(LOCK_QUERY.formatted(schema, table));
        query.executeUpdate();
    }

}
