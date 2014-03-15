package gov.usgs.cida.twitter.data.dao;

import gov.usgs.cida.twitter.data.model.TwitterEventType;
import gov.usgs.cida.twitter.data.util.MyBatisConnectionFactory;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author isuftin
 */
public class TwitterEventTypeDAO {

    private final SqlSessionFactory sqlSessionFactory;

    public TwitterEventTypeDAO() {
        sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
    }

    public TwitterEventTypeDAO(SqlSessionFactory factory) {
        sqlSessionFactory = factory;
    }

    public List<TwitterEventType> getAll() {
        List<TwitterEventType> eventTypeList;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            eventTypeList = session.selectList("gov.usgs.cida.mybatis.mappers.TwitterEventTypeMapper.getAll");
        }

        return eventTypeList;
    }

    public TwitterEventType getByEventTypeId(int id) {
        TwitterEventType result;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            result = session.selectOne("gov.usgs.cida.mybatis.mappers.TwitterEventTypeMapper.getByEventTypeId", id);
        }

        return result;
    }

}
