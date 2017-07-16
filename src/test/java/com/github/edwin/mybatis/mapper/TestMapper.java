package com.github.edwin.mybatis.mapper;

import com.github.edwin.mybatis.bean.Test;
import com.github.edwin.mybatis.bean.TestExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

public interface TestMapper {
    int countByExample(TestExample example);

    int deleteByExample(TestExample example);

    int deleteByPrimaryKey(String field1);

    int insert(Test record);

    int insertSelective(Test record);

    List<Test> selectByExampleWithRowbounds(TestExample example, RowBounds rowBounds);

    List<Test> selectByExample(TestExample example);

    int updateByExampleSelective(@Param("record") Test record, @Param("example") TestExample example);

    int updateByExample(@Param("record") Test record, @Param("example") TestExample example);
    
    @Results({
          @Result(property = "field1", column = "field1")
        })
    @Select("SELECT * from test WHERE field1 like #{field1}")
    List<Test> selectLike(String field1, RowBounds rowBounds);
    
    List<Map> selectByExample2(RowBounds rowBounds);
}