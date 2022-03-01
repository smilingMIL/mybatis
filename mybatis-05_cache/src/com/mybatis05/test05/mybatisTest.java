package com.mybatis05.test05;

import com.mybatis05.bean05.Employee;
import com.mybatis05.dao05.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;


import java.io.IOException;
import java.io.InputStream;

/**
 * @author smile
 */
public class mybatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //利用SqlSessionFactoryBuilder创建SqlSessionFactory
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 两级缓存：
     * 一级缓存：（本地缓存）：sqlSession级别的缓存，一级缓存是一直开启的。SqlSession级别的一个Map
     *          与数据库同一次会话期间查询到的数据会放在本地缓存中。
     *          以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库；
     *
     *          一级缓存失效情况（没有使用到当前一级缓存的情况，效果就是：还需要再向数据库发送查询）
     *          1.sqlSession不同。
     *          2.sqlSession相同，查询条件不同.(当前一级缓存中还没有这个数据)
     *          3.sqlSession相同，但两次查询之间执行了增删改操作
     *          4.同一个sqlSession两次查询期间手动清空了缓存
     */
    @Test
    public void testFirstLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            //默认是一级缓存
            Employee empById1 = mapper.getEmpById(1);
            System.out.println(empById1);
            Employee empById2 = mapper.getEmpById(1);
            System.out.println(empById2);
            System.out.println(empById1==empById2);
            System.out.println("============================================================================");

            //2.sqlSession相同，查询条件不同
            Employee empById4 = mapper.getEmpById(3);
            System.out.println(empById4);

            //4.同一个sqlSession两次查询期间手动清空了缓存
            openSession.clearCache();

            Employee empById5 = mapper.getEmpById(3);
            System.out.println(empById5);
            System.out.println(empById4==empById5);
            System.out.println("============================================================================");

            //1.不同的sqlSession
            SqlSession openSession1 = sqlSessionFactory.openSession();
            EmployeeMapper mapper1 = openSession1.getMapper(EmployeeMapper.class);
            Employee empById3 = mapper1.getEmpById(1);
            System.out.println(empById3);


            openSession1.close();
        }finally {
            openSession.close();
        }
    }


    /**
     *  二级缓存：（全局缓存）：基于namespace级别的缓存；一个namespace可以对应一个二级缓存；
     *           工作机制：
     *          1、一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中；
     *          2、如果会话关闭，一级缓存中的数据会被保存到二级缓存中；新的会话查询信息，就可以参照二级缓存
     *          3、sqlSession===EmployeeMapper==>Employee
     *                          DepartmentMapper==>DepartmentMapper
     *               不同namespace查出的数据会放在自己对应的缓存中(map)
     *               效果：数据会从二级缓存中获取
     *                      查出的数据都会默认先放在一级缓存中。
     *                      只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中
     *          使用：
     *              1）开启全局二级缓存配置：<setting name="cacheEnabled" value="true"/>
     *              2) 去每个mapper.xml中配置使用二级缓存：<cache/>
     *              3）我们的POJO(javaBean)需要实现序列化接口
     *
     * 和缓存有关的设置/属性
     *              1）、全局setting的 cacheEnabled=true；false：关闭缓存（关闭二级缓存，一级缓存一直开着）
     *              2）、每个select标签的useCache属性：是否使用二级缓存，true/false
     *              3）、每个增删改标签：默认flushCache="true"：增删改执行完之后会将一级二级清空。
     *              4）、sqlSession.clearCache()：只是用来清除一级缓存。
     *              5）、localCacheScope：本地缓存作用域：（一级缓存session）；当前会话的所有数据保存在会话缓存中
     *                                      statement：可以禁用一级缓存；
     *
     * 第三方缓存整合：
     *          1）：导入第三方缓存包；
     *          2）：导入与第三方缓存整合的适配包；
     *          3）：mapper.xml中使用自定义缓存；
     *          <cache type="org.mybatis.caches.ehcache.EhcacheCache"></cache>
     */
    @Test
    public void testSecondLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession1 = sqlSessionFactory.openSession();
        SqlSession openSession2 = sqlSessionFactory.openSession();

        try {
            //1.
            EmployeeMapper mapper1 = openSession1.getMapper(EmployeeMapper.class);
            EmployeeMapper mapper2 = openSession2.getMapper(EmployeeMapper.class);

            Employee empById1 = mapper1.getEmpById(1);
            System.out.println(empById1);
            openSession1.close();

            //第二次查询是从二级缓存拿到的数据，并没有发送新的sql
            Employee empById2 = mapper2.getEmpById(1);
            System.out.println(empById2);
            openSession2.close();

        }finally {

        }
    }

}
