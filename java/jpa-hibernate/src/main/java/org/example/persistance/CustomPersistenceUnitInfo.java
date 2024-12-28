package org.example.persistance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

import javax.sql.DataSource;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class CustomPersistenceUnitInfo implements PersistenceUnitInfo {
    @Override
    public String getPersistenceUnitName() {
        return "my-persistence";
    }

    @Override
    public String getPersistenceProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
    }

    @Override
    public String getScopeAnnotationName() {
        return "";
    }

    @Override
    public List<String> getQualifierAnnotationNames() {
        return List.of();
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public DataSource getJtaDataSource() {
        Properties properties = null;
        try {
            var filePath = "F:\\dev\\learning\\java\\jpa-hibernate\\src\\main\\resources\\META-INF\\hibernate.properties";
            properties = new Properties();
            properties.load(Files.newInputStream(Path.of(filePath)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Create HikariCP DataSource
        var config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("hibernate.connection.url"));// Database URL
        config.setUsername(properties.getProperty("hibernate.username"));// Database username
        config.setPassword(properties.getProperty("hibernate.password"));// Database password
        config.setDriverClassName(properties.getProperty("hibernate.driverClassName"));// JDBC driver

        config.setMaximumPoolSize(10);// Max number of connections in the pool
        config.setMinimumIdle(2);// Minimum number of idle connections
        config.setIdleTimeout(30000);// Idle timeout in milliseconds
        config.setMaxLifetime(1800000);// Max lifetime of a connection in milliseconds
        config.setConnectionTimeout(30000);// Connection timeout in milliseconds

        return new HikariDataSource(config);
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return null;
    }

    @Override
    public List<String> getMappingFileNames() {
        return List.of();
    }

    @Override
    public List<URL> getJarFileUrls() {
        return List.of();
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    @Override
    public List<String> getManagedClassNames() {
        return List.of("org.example.models.Product");
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return null;
    }

    @Override
    public ValidationMode getValidationMode() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return "";
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void addTransformer(ClassTransformer classTransformer) {

    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }
}