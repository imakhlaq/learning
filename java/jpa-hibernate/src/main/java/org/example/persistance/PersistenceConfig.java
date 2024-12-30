package org.example.persistance;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.io.IOException;
import java.util.HashMap;

/*
EntityManagerFactory is only one for one db.
Because creating instance EntityManagerFactory is resource intensive so this class make sure EntityManagerFactory have only one instance (Singleton).
 */
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