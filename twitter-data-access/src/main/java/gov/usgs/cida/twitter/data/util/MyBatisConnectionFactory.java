package gov.usgs.cida.twitter.data.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isuftin
 */
public class MyBatisConnectionFactory {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MyBatisConnectionFactory.class);
    private static SqlSessionFactory sqlSessionFactory = null;
    private final static String RESOURCE = "mybatis-config.xml";

    static {
        try (InputStream inputStream = Resources.getResourceAsStream(RESOURCE)) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            log.debug("Created a new SqlSessionFactory");
        } catch (IOException ex) {
            log.error("Error initializing SqlSessionFactoryBuilder", ex);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
