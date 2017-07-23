package com.github.edwin.mybatis.dialect;

import com.github.edwin.mybatis.bean.TestExample;
import com.github.edwin.mybatis.config.MyBatisSqlServerSqlSessionFactory;
import com.github.edwin.mybatis.mapper.TestMapper;
import com.github.edwin.mybatis.session.RowBounds2;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import junit.framework.Assert;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * <pre>
 *  com.github.edwin.mybatis.dialect.SQLServerSQLDialectTest 
 * </pre>
 *
 * @author edwin < edwinkun at gmail dot com >
 * Jul 23, 2017 3:35:33 PM
 *
 */
public class SQLServerSQLDialectTest {

    @BeforeTest
    private void buildH2Db() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:mytable;MODE=MSSQLServer;", new Properties());
        
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS TEST(field1 VARCHAR(40))");
        statement.executeUpdate("insert into TEST values('lele')");
        statement.executeUpdate("insert into TEST values('" + UUID.randomUUID().toString() + "')");
        statement.executeUpdate("insert into TEST values('" + UUID.randomUUID().toString() + "')");
        statement.executeUpdate("insert into TEST values('" + UUID.randomUUID().toString() + "')");
        statement.executeUpdate("insert into TEST values('" + UUID.randomUUID().toString() + "')");
        statement.executeUpdate("insert into TEST values('" + UUID.randomUUID().toString() + "')");
        connection.commit();
    }
    
    @Test
    public void sanityCheckCount() throws Exception {
        SqlSession sqlSession = MyBatisSqlServerSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        Assert.assertEquals(6, testMapper.countByExample(new TestExample()));
    }
    
    @Test
    public void select() throws Exception {
        SqlSession sqlSession = MyBatisSqlServerSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds(2, 2));
        Assert.assertEquals(2, tests.size());
    }
    
    @Test
    public void selectRowbounds2() throws Exception {
        SqlSession sqlSession = MyBatisSqlServerSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds2(2000l, 2));
        Assert.assertEquals(0, tests.size());
    }
    
    @Test
    public void selectRowbounds3() throws Exception {
        SqlSession sqlSession = MyBatisSqlServerSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds2(2, 2));
        Assert.assertEquals(2, tests.size());
    }
    
    @Test
    public void selectRowbounds2WithLikes() throws Exception {
        SqlSession sqlSession = MyBatisSqlServerSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        TestExample testExample = new TestExample();
        testExample.createCriteria().andField1Like("lele");
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(testExample, new RowBounds2(0, 5));
        Assert.assertEquals(1, tests.size());
    }
    
    @Test
    public void selectWithLikes() throws Exception {
        SqlSession sqlSession = MyBatisSqlServerSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        TestExample testExample = new TestExample();
        testExample.createCriteria().andField1Like("lele");
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(testExample, new RowBounds(0, 5));
        Assert.assertEquals(1, tests.size());
    }
    
    @Test
    public void selectWithLikesWithAnnotation() throws Exception {
        SqlSession sqlSession = MyBatisSqlServerSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectLike("lele", new RowBounds(0, 5));
        Assert.assertEquals(1, tests.size());
    }
    
    @Test
    public void selectWithLikesWithXml() throws Exception {
        SqlSession sqlSession = MyBatisSqlServerSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<Map> tests = testMapper.selectByExample2(new RowBounds(0, 5));
        Assert.assertEquals(5, tests.size());
    }
    
}
