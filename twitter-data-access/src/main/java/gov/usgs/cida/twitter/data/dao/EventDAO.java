package gov.usgs.cida.twitter.data.dao;

import gov.usgs.cida.twitter.data.model.Event;
import gov.usgs.cida.twitter.data.util.MyBatisConnectionFactory;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author isuftin
 */
public class EventDAO {

    private final SqlSessionFactory sqlSessionFactory;

    public EventDAO() {
        sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
    }

    public EventDAO(SqlSessionFactory factory) {
        sqlSessionFactory = factory;
    }

    public List<Event> getAll() {
        List<Event> eventTypeList;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            eventTypeList = session.selectList("gov.usgs.cida.mybatis.mappers.EventMapper.getAll");
        }

        return eventTypeList;
    }

    public Event getByEventId(int id) {
        Event result;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            result = session.selectOne("gov.usgs.cida.mybatis.mappers.EventMapper.getByEventId", id);
        }

        return result;
    }

    public int insertEvent(Event event) {
        int affectedRowsCount;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            affectedRowsCount = session.insert("gov.usgs.cida.mybatis.mappers.EventMapper.insertEvent", event);
            session.commit();
        }
        return affectedRowsCount;
    }

}
