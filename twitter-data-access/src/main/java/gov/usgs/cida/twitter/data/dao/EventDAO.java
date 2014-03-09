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
        List<Event> eventList;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            eventList = session.selectList("gov.usgs.cida.mybatis.mappers.EventMapper.getAll");
        }

        return eventList;
    }

    public Event getByEventId(int id) {
        Event event;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            event = session.selectOne("gov.usgs.cida.mybatis.mappers.EventMapper.getByEventId", id);
        }

        return event;
    }

    public int insertEvent(Event event) {
        int affectedRowsCount;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            affectedRowsCount = session.insert("gov.usgs.cida.mybatis.mappers.EventMapper.insertEvent", event);
            session.commit(true);
            session.close();
        }
        return affectedRowsCount;
    }

}
