[![Build Status](https://travis-ci.org/edwin/RowboundToNative.svg?branch=master)](https://travis-ci.org/edwin/RowboundToNative)
## MyBatis Plugin for Converterting RowBound class to Native Paging Queries
----
MyBatis have a builtin pagination query using RowBound, but it use java's ResultSet absolute method for cursor position moving[1], but with using this library we can convert Rowbound(?, ?) into native sql queries, such as limit ?, ? for mysql. Supporting both manual MyBatis code and MyBatis Generator's generated codes. 

Simply by adding Plugin tag on Connection.xml, 
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
    <plugins>
        <plugin interceptor="com.github.edwin.mybatis.plugin.RowboundInterceptor">
            <property name="dialectClass" value="com.github.edwin.mybatis.dialect.MySQLDialect"/>
        </plugin>
    </plugins>
```

can make your java class [2]
```java
List<Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds(2, 2));
```

have sql queries with limit like this 
```sql
01:11:44 DEBUG selectByExampleWithRowbounds - ==>  Preparing: select field1 from test limit 2,2 
01:11:44 DEBUG selectByExampleWithRowbounds - ==> Parameters: 
01:11:44 DEBUG selectByExampleWithRowbounds - <==      Total: 2
```


Currently supporting 3 databases which i used the most, MySql, Postgresql and Oracle. Feel free to fork and do PR if you want to add some more databases.

Hopefully, it will help a lot of people out there.

Appreciations
----
found a lot of inspiration from googling and several github pages. :heart: for you guys 


Footnote
----
[1] https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html#absolute(int)

[2] https://github.com/edwin/RowboundToNative/blob/master/src/test/java/com/github/edwin/mybatis/dialect/MySqlDialectTest.java#L57