package gov.usgs.cida.twitter.data.dao;

import gov.usgs.cida.twitter.data.model.EventType;
import gov.usgs.cida.twitter.data.util.MyBatisConnectionFactory;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author isuftin
 */
public class EventTypeDAO {

    private final SqlSessionFactory sqlSessionFactory;

    public EventTypeDAO() {
        sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
    }

    public EventTypeDAO(SqlSessionFactory factory) {
        sqlSessionFactory = factory;
    }

    public List<EventType> getAll() {
        List<EventType> eventTypeList;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            eventTypeList = session.selectList("gov.usgs.cida.mybatis.mappers.EventTypeMapper.getAll");
        }

        return eventTypeList;
    }

    public EventType getByEventTypeId(int id) {
        EventType result;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            result = session.selectOne("gov.usgs.cida.mybatis.mappers.EventTypeMapper.getByEventTypeId", id);
        }

        return result;
    }

}
