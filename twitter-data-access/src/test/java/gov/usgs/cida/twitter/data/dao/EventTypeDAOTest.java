package gov.usgs.cida.twitter.data.dao;

import gov.usgs.cida.twitter.data.model.EventType;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import liquibase.CatalogAndSchema;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.experimental.categories.Category;

/**
 *
 * @author isuftin
 */
@Category(IntegrationTest.class)
public class EventTypeDAOTest {

    private static Connection conn;
    private static SqlSessionFactory sqlSessionFactory;
    private static Liquibase liquibase;

    public EventTypeDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException, DatabaseException, LiquibaseException, InstantiationException, IllegalAccessException {
        String port = System.getProperty("db.twitter.integration-test.port");
        String driver = System.getProperty("db.twitter.integration-test.driver");
        String dbType = System.getProperty("db.twitter.integration-test.dbtype");
        String schema = System.getProperty("db.twitter.integration-test.schema");
        if (StringUtils.isBlank(port)) {
            throw new NullPointerException("System property \"db.twitter.integration-test.port\" not found");
        }
        if (StringUtils.isBlank(driver)) {
            throw new NullPointerException("System property \"db.twitter.integration-test.driver\" not found");
        }
        if (StringUtils.isBlank(dbType)) {
            throw new NullPointerException("System property \"db.twitter.integration-test.dbType\" not found");
        }
        if (StringUtils.isBlank(schema)) {
            throw new NullPointerException("System property \"db.twitter.integration-test.schema\" not found");
        }
        Class.forName(driver).newInstance();
        conn = DriverManager.getConnection("jdbc:" + dbType + "://localhost:" + port + "/" + schema + ";create=true", "test", "test");

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
        liquibase = new Liquibase("src/main/resources/liquibase/changelogs/create-table-parent-changeLog.xml", new FileSystemResourceAccessor(), database);

        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "integration-test");
        } catch (Exception ex) {
            System.out.println("Error initializing SqlSessionFactoryBuilder: " + ex);
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.close();
    }

    @Before
    public void beforeTest() throws DatabaseException, LiquibaseException {
        liquibase.dropAll(new CatalogAndSchema("test", "twitter"));
        liquibase.update("");
    }

    @After
    public void afterTest() throws DatabaseException {
        liquibase.dropAll(new CatalogAndSchema("test", "twitter"));
    }

    @Test
    public void testGetAll() {
        System.out.println("getAll");
        EventTypeDAO instance = new EventTypeDAO(sqlSessionFactory);
        List<EventType> result = instance.getAll();
        assertNotNull(result);
    }

    @Test
    public void testGetByEventTypeId() {
        System.out.println("getByEventTypeId");
        int id = 1;
        EventTypeDAO instance = new EventTypeDAO(sqlSessionFactory);
        EventType result = instance.getByEventTypeId(id);
        assertNotNull(result);
    }

}
