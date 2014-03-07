package gov.usgs.cida.twitter.data.dao;

import gov.usgs.cida.twitter.data.model.Event;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import liquibase.Contexts;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.experimental.categories.Category;

/**
 *
 * @author isuftin
 */
@Category(IntegrationTest.class)
public class EventDAOTest {

    private static Connection conn;
    private static SqlSessionFactory sqlSessionFactory;
    private static Liquibase liquibase;

    public EventDAOTest() {
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
        
        liquibase.update(new Contexts("integration-test"));
        liquibase.tag("integration-test");
    }


    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.close();
    }

    @After
    public void afterTest() throws DatabaseException, LiquibaseException {
        liquibase.rollback("integration-test", new Contexts("integration-test"));
    }

    @Test
    public void testGetAll() {
        System.out.println("getAll");
        EventDAO instance = new EventDAO(sqlSessionFactory);
        List<Event> result = instance.getAll();
        assertNotNull(result);
        assertThat(result.size(), greaterThan(0));
        assertThat(result.size(), equalTo(2));
    }

    @Test
    public void testGetByEventTypeId() {
        System.out.println("getByEventTypeId");
        int id = 1;
        EventDAO instance = new EventDAO(sqlSessionFactory);
        Event result = instance.getByEventId(id);
        assertNotNull(result);
        assertThat(result.getEventMessage(), is("We've connected!"));
        assertThat(result.getEventType().getEventDescription(), is("When a connection is established w/ a 200 response"));
    }
    
    @Test
    public void testGetInvalidId() {
        System.out.println("testGetInvalidId");
        int id = 3;
        EventDAO instance = new EventDAO(sqlSessionFactory);
        Event result = instance.getByEventId(id);
        assertNull(result);
    }

}
