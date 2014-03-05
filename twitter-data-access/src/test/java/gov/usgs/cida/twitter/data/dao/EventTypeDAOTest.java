package gov.usgs.cida.twitter.data.dao;

import gov.usgs.cida.twitter.data.model.EventType;
import java.io.InputStream;
import java.util.List;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.categories.Category;

/**
 *
 * @author isuftin
 */
@Category(IntegrationTest.class)
public class EventTypeDAOTest {
    
    private static SqlSessionFactory sqlSessionFactory;
    
    public EventTypeDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "integration-test");
        } catch (Exception ex) {
            System.out.println("Error initializing SqlSessionFactoryBuilder: " + ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
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
