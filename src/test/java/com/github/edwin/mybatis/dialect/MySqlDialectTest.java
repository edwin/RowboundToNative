package com.github.edwin.mybatis.dialect;

import ch.vorburger.mariadb4j.DB;
import com.github.edwin.mybatis.bean.TestExample;
import com.github.edwin.mybatis.config.MyBatisSqlSessionFactory;
import com.github.edwin.mybatis.mapper.TestMapper;
import com.github.edwin.mybatis.session.RowBounds2;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import junit.framework.Assert;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * <pre>
 *  com.github.edwin.mybatis.dialect.MySqlDialectTest 
 * </pre>
 *
 * @author edwin < edwinkun at gmail dot com >
 * Jul 15, 2017 11:10:20 PM
 *
 */
public class MySqlDialectTest {

    @BeforeTest
    private void buildMysqlDb() throws Exception {
        DB db = DB.newEmbeddedDB(33307);
        db.start();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:33307/test", "root", "");
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS `test`");
        statement.executeUpdate("create table test(field1 VARCHAR(40) NOT NULL, PRIMARY KEY ( field1 ))");
        statement.executeUpdate("insert into test values('lele')");
        statement.executeUpdate("insert into test values('"+UUID.randomUUID().toString()+"')");
        statement.executeUpdate("insert into test values('"+UUID.randomUUID().toString()+"')");
        statement.executeUpdate("insert into test values('"+UUID.randomUUID().toString()+"')");
        statement.executeUpdate("insert into test values('"+UUID.randomUUID().toString()+"')");
        statement.executeUpdate("insert into test values('"+UUID.randomUUID().toString()+"')");
    }
    
    @Test
    public void sanityCheckCount() throws Exception {
        SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        Assert.assertEquals(6, testMapper.countByExample(new TestExample()));
    }
    
    @Test
    public void select() throws Exception {
        SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds(2, 2));
        Assert.assertEquals(2, tests.size());
    }
    
    @Test
    public void selectRowbounds2() throws Exception {
        SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds2(20000000000l, 2));
        Assert.assertEquals(0, tests.size());
    }
    
    @Test
    public void selectWithLikes() throws Exception {
        SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        TestExample testExample = new TestExample();
        testExample.createCriteria().andField1Like("lele");
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(testExample, new RowBounds(0, 5));
        Assert.assertEquals(1, tests.size());
    }
    
    @Test
    public void selectWithLikesWithAnnotation() throws Exception {
        SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectLike("lele", new RowBounds(0, 5));
        Assert.assertEquals(1, tests.size());
    }
    
    @Test
    public void selectWithLikesWithXml() throws Exception {
        SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<Map> tests = testMapper.selectByExample2(new RowBounds(0, 5));
        Assert.assertEquals(5, tests.size());
    }
    
}
