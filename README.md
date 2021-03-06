[![Build Status](https://travis-ci.org/edwin/RowboundToNative.svg?branch=master)](https://travis-ci.org/edwin/RowboundToNative)
## MyBatis Plugin for Converterting RowBound class to Native Paging Queries

MyBatis have a builtin pagination query using RowBound, but it use java's ResultSet absolute method for cursor position moving <sup>[1](#myfootnote1)</sup>, but with using this library we can convert Rowbound(?, ?) into native sql queries, such as limit ?, ? for mysql. Supporting both manual MyBatis code and MyBatis Generator's generated codes. 

Simply by adding Plugin tag on Connection.xml <sup>[2](#myfootnote2)</sup>, 
```xml
    <plugins>
        <plugin interceptor="com.github.edwin.mybatis.plugin.RowboundInterceptor">
            <property name="dialectClass" value="com.github.edwin.mybatis.dialect.MySQLDialect"/>
        </plugin>
    </plugins>
```

can make your java class <sup>[3](#myfootnote3)</sup>
```java
List<Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds(2, 2));
```

have sql queries with limit like this 
```sql
01:11:44 DEBUG selectByExampleWithRowbounds - ==>  Preparing: select field1 from test limit 2,2 
01:11:44 DEBUG selectByExampleWithRowbounds - ==> Parameters: 
01:11:44 DEBUG selectByExampleWithRowbounds - <==      Total: 2
```

Querying Large Number of Data
----
Providing RowBounds2 to replace RowBounds, which can accept long as both offset and limit <sup>[4](#myfootnote4)</sup>. 
```java
List<Test> tests = testMapper.selectByExampleWithRowbounds(new TestExample(), new RowBounds2(20000000000l, 2));
```


Currently supporting 5 databases which i used the most. MySql 5.5.27, Postgresql 9.4, DB2 9.7 (tested using Apache Derby 10.13.1.1), SQL Server 2012 (tested using H2 1.4.192) and Oracle 11g. Feel free to fork and do PR if you want to add some more databases, create Issue for requests, or you could just ping me anytime :wink:.

Hopefully, it will help a lot of people out there.

Appreciations
----
found a lot of inspiration from googling and several github pages. :heart: for you guys 


Footnote
----
<a name="myfootnote1">[1]</a> :  https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html#absolute(int)

<a name="myfootnote2">[2]</a> :  https://github.com/edwin/RowboundToNative/blob/master/src/test/resources/ConfigurationMySql.xml

<a name="myfootnote3">[3]</a> :  https://github.com/edwin/RowboundToNative/blob/master/src/test/java/com/github/edwin/mybatis/dialect/MySqlDialectTest.java#L57

<a name="myfootnote4">[4]</a> :  https://github.com/edwin/RowboundToNative/blob/master/src/main/java/com/github/edwin/mybatis/session/RowBounds2.java