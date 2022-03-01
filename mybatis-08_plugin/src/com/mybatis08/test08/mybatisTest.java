package com.mybatis08.test08;

import com.mybatis08.bean08.Employee;
import com.mybatis08.dao08.EmployeeMapper;
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
     * 在四大对象创建的时候
     * 1、每个创建出来的对象不是直接返回，而是
     *          interceptorChain.pluginAll(parameterHandler);
     * 2、获取到所有的interceptor(拦截器) （插件需要实现的接口）；
     *      调用interceptor.plugin(target);返回target包装后的对象
     * 3、插件机制，我们可以使用插件为目标对象创建一个代理对象；AOP（面向切面）
     *       我们的插件可以为四大对象创建出代理对象；
     *       代理对象就可以拦截到四大对象的每个执行：
     *
     *
     *  public Object pluginAll(Object target){
     *      for(Interceptor interceptor : interceptors){
     *          target = interceptor.plugin(target);
     *      }
     *      return target;
     *  }
     */

    /**
     * 插件的编写：
     * 1.编写Interceptor的实现类
     * 2.使用@Intercepts注解完成插件签名
     * 3.将写好的插件注册到全局配置文件中
     */
    @Test
    public void test() throws IOException {

        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession1 = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper1 = openSession1.getMapper(EmployeeMapper.class);
            Employee empById1 = mapper1.getEmpById(1);
            System.out.println(empById1);
        }finally {
            openSession1.close();
        }

    }
}
