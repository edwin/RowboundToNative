package com.github.edwin.mybatis.dialect;

import ch.vorburger.mariadb4j.DB;
import com.github.edwin.mybatis.bean.TestExample;
import com.github.edwin.mybatis.config.MyBatisSqlSessionFactory;
import com.github.edwin.mybatis.mapper.TestMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
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
        DB db = DB.newEmbeddedDB(3306);
        db.start();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS `test`");
        statement.executeUpdate("create table test(field1 VARCHAR(40) NOT NULL, PRIMARY KEY ( field1 ))");
        statement.executeUpdate("insert into test values('"+UUID.randomUUID().toString()+"')");
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
        List<com.github.edwin.mybatis.bean.Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds(0, 1));
        Assert.assertEquals(1, tests.size());
    }
    
}
