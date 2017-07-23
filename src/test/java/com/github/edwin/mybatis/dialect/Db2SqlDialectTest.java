package com.github.edwin.mybatis.dialect;

import com.github.edwin.mybatis.bean.TestExample;
import com.github.edwin.mybatis.config.MyBatisDerbySqlSessionFactory;
import com.github.edwin.mybatis.mapper.TestMapper;
import com.github.edwin.mybatis.session.RowBounds2;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import junit.framework.Assert;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * <pre>
 *  com.github.edwin.mybatis.dialect.Db2SqlDialectTest
 * </pre>
 *
 * @author edwin < edwinkun at gmail dot com >
 * @see https://stackoverflow.com/questions/3760471/is-there-a-good-in-memory-database-that-would-act-like-db2
 * Jul 23, 2017 1:55:08 PM
 *
 */
public class Db2SqlDialectTest {

    @BeforeTest
    private void buildDerbyDb() throws Exception {
        EmbeddedDriver driver = new EmbeddedDriver();
        Connection connection = driver.connect("jdbc:derby:memory:MyDatabase;create=true", new Properties());
        
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE SCHEMA ME");
        statement.executeUpdate("CREATE TABLE ME.TEST(field1 VARCHAR(40))");
        statement.executeUpdate("insert into ME.TEST values('lele')");
        statement.executeUpdate("insert into ME.TEST values('" + UUID.randomUUID().toString() + "')");
        statement.executeUpdate("insert into ME.TEST values('" + UUID.randomUUID().toString() + "')");
        statement.executeUpdate("insert into ME.TEST values('" + UUID.randomUUID().toString() + "')");
        statement.executeUpdate("insert into ME.TEST values('" + UUID.randomUUID().toString() + "')");
        statement.executeUpdate("insert into ME.TEST values('" + UUID.randomUUID().toString() + "')");
    }
    
    @Test
    public void sanityCheckCount() throws Exception {
        SqlSession sqlSession = MyBatisDerbySqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        Assert.assertEquals(6, testMapper.countByExample(new TestExample()));
    }
    
    
    @Test
    public void select() throws Exception {
        SqlSession sqlSession = MyBatisDerbySqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds(2, 2));
        Assert.assertEquals(2, tests.size());
    }
    
    @Test
    public void selectRowbounds2() throws Exception {
        SqlSession sqlSession = MyBatisDerbySqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds2(20000000000l, 2));
        Assert.assertEquals(0, tests.size());
    }
    
    @Test
    public void selectWithLikes() throws Exception {
        SqlSession sqlSession = MyBatisDerbySqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        TestExample testExample = new TestExample();
        testExample.createCriteria().andField1Like("lele");
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(testExample, new RowBounds(0, 5));
        Assert.assertEquals(1, tests.size());
    }
    
    @Test
    public void selectWithLikesWithAnnotation() throws Exception {
        SqlSession sqlSession = MyBatisDerbySqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectLike("lele", new RowBounds(0, 5));
        Assert.assertEquals(1, tests.size());
    }
    
    @Test
    public void selectWithLikesWithXml() throws Exception {
        SqlSession sqlSession = MyBatisDerbySqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<Map> tests = testMapper.selectByExample2(new RowBounds(0, 5));
        Assert.assertEquals(5, tests.size());
    }

}
