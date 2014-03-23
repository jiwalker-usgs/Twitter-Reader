package gov.usgs.cida.twitter.data.dao;

import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.event.EventType;
import gov.usgs.cida.twitter.data.model.TwitterEvent;
import gov.usgs.cida.twitter.data.model.TwitterEventType;
import gov.usgs.cida.twitter.data.observer.DatabaseEventObserver;
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
public class DatabaseEventObserverTest {

    private static Connection conn;
    private static SqlSessionFactory sqlSessionFactory;
    private static Liquibase liquibase;
    private TwitterEventDAO instance = null;
    private static final Contexts contexts = new Contexts("integration-test");

    public DatabaseEventObserverTest() {
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
        liquibase.update("integration-test-load-event-data", new PrintWriter(System.out));
    }

    @After
    public void afterTest() throws LiquibaseException {
        liquibase.rollback("base-tables-data-loaded", "integration-test-load-event-data", new PrintWriter(System.out));
    }

    @Test
    public void testHandleEvent() {
        System.out.println("testHandleEvent");
        
        DatabaseEventObserver observer = new DatabaseEventObserver(sqlSessionFactory);
        observer.handleEvent(new Event(EventType.CONNECTED));
        List<TwitterEvent> result = instance.getAll();
        assertThat(result.size(), greaterThan(0));
        assertThat(result.get(result.size() - 1).getEventType().getEventDescription(), is(TwitterEventType.Type.CONNECTED.getEventDescription()));
    }
}
