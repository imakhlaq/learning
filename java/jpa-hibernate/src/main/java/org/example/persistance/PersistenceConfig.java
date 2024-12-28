package org.example.persistance;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.io.IOException;
import java.util.HashMap;

public class PersistenceConfig {

    private static EntityManagerFactory entityManagerFactory = null;

    public static EntityManagerFactory getEntityManagerFactory() throws IOException {

        if (entityManagerFactory == null) {
            synchronized (PersistenceConfig.class) {
                if (entityManagerFactory == null)
                    entityManagerFactory = new HibernatePersistenceProvider()
                        .createContainerEntityManagerFactory(new CustomPersistenceUnitInfo(), new HashMap<>());
            }
        }
        return entityManagerFactory;
    }
}