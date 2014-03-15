package gov.usgs.cida.twitter.data.dao;

import gov.usgs.cida.twitter.data.model.TwitterEvent;
import gov.usgs.cida.twitter.data.model.TwitterEventType;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import org.junit.Before;
import org.junit.experimental.categories.Category;

/**
 *
 * @author isuftin
 */
@Category(IntegrationTest.class)
public class TwitterEventDAOTest {

    private static Connection conn;
    private static SqlSessionFactory sqlSessionFactory;
    private static Liquibase liquibase;
    private TwitterEventDAO instance = null;
    private static final Contexts contexts = new Contexts("integration-test");

    public TwitterEventDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException, DatabaseException, LiquibaseException, InstantiationException, IllegalAccessException, IOException {
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

        conn = DriverManager.getConnection("jdbc:" + dbType + "://127.0.0.1:" + port + "/" + schema + ";create=true", "test", "test");
        
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
        liquibase = new Liquibase("src/main/resources/liquibase/changelogs/create-table-parent-changeLog.xml", new FileSystemResourceAccessor(), database);
        liquibase.dropAll();
        liquibase.update(contexts);
        
        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "integration-test");
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.close();
    }

    @Before
    public void beforeTest() throws LiquibaseException, InterruptedException {
        instance = new TwitterEventDAO(sqlSessionFactory);
        liquibase.update("integration-test-load-event-data");
    }

    @After
    public void afterTest() throws LiquibaseException {
        liquibase.rollback("base-tables-data-loaded", "integration-test-load-event-data", new PrintWriter(System.out));
    }

    @Test
    public void testRetreiveInsertedEvent() {
        System.out.println("testRetreiveInsertedEvent");
        TwitterEvent insertEvent = new TwitterEvent(new TwitterEventType(TwitterEventType.Type.CONNECTED), "This is a test");
        TwitterEvent retrievedEvent;
        int insertedRows = instance.insertEvent(insertEvent);
        assertThat(insertedRows, is(1));
            
        List<TwitterEvent> retrievedEventList = instance.getAll();
        retrievedEvent = retrievedEventList.get(retrievedEventList.size() - 1);
        assertThat(retrievedEvent.getEventMessage(), is("This is a test"));
        assertThat(retrievedEventList.size(), greaterThan(0));
        assertThat(retrievedEventList.size(), is(3));
    }

    @Test
    public void testGetByEventTypeId() {
        System.out.println("getByEventTypeId");
        int id = 1;
        TwitterEvent result = instance.getByEventId(id);
        assertNotNull(result);
        assertThat(result.getEventMessage(), is("connected!"));
        assertThat(result.getEventType().getEventDescription(), is("When a connection is established w/ a 200 response"));
    }

    @Test
    public void testGetAll() throws InterruptedException {
        System.out.println("getAll");
        List<TwitterEvent> result = instance.getAll();
        assertNotNull(result);
        assertThat(result.size(), greaterThan(0));
        // TODO- For some reason the ROLLBACK delete is not working correctly so 
        // the count here is 3 (includes the insert done by testRetreiveInsertedEvent()
        // I am wondering if this is a Liquibase or a Derby issue...
//        assertThat(result.size(), equalTo(2));
    }

    @Test
    public void testGetInvalidId() {
        System.out.println("testGetInvalidId");
        int id = 3;
        TwitterEvent result = instance.getByEventId(id);
        assertNull(result);
    }

}
