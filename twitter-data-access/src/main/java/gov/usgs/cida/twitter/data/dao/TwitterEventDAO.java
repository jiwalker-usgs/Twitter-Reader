package gov.usgs.cida.twitter.data.dao;

import gov.usgs.cida.twitter.data.model.TwitterEvent;
import gov.usgs.cida.twitter.data.util.MyBatisConnectionFactory;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author isuftin
 */
public class TwitterEventDAO {

    private final SqlSessionFactory sqlSessionFactory;

    public TwitterEventDAO() {
        sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
    }

    public TwitterEventDAO(SqlSessionFactory factory) {
        sqlSessionFactory = factory;
    }

    public List<TwitterEvent> getAll() {
        List<TwitterEvent> eventList;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            eventList = session.selectList("gov.usgs.cida.mybatis.mappers.TwitterEventMapper.getAll");
        }

        return eventList;
    }

    public TwitterEvent getByEventId(int id) {
        TwitterEvent event;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            event = session.selectOne("gov.usgs.cida.mybatis.mappers.TwitterEventMapper.getByEventId", id);
        }

        return event;
    }

    public int insertEvent(TwitterEvent event) {
        int affectedRowsCount;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            affectedRowsCount = session.insert("gov.usgs.cida.mybatis.mappers.TwitterEventMapper.insertEvent", event);
            session.commit(true);
            session.close();
        }
        return affectedRowsCount;
    }

}
