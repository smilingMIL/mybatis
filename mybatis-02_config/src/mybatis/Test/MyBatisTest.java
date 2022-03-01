package mybatis.Test;


import com.mybatis09.dao09.EmployeeMapper;
import com.mybatis09.bean09.Employee;

import mybatis.dao.EmployeeMapperAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 1.接口式编程：
 * 原生：     Dao ==>>   DaoImpl
 * com.mybatis   Mapper  ==>>   xxMapper.xml
 *
 * 2.SqlSession代表和数据库的一次会话，用完必须关闭
 * 3.SqlSession和connection一样都是线程不安全，每次使用都应该去获取新的对象
 * 4.mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 *              （将接口和xml进行绑定）
 *  EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
 * 5.两个重要的配置文件：
 *         mybatis的全局配置文件，包含数据库连接池信息，事务管理器。。。系统运行环境信息
 *         sql映射文件：保存每一个sql语句的映射信息，
 *
 * @author smile
 */
public class MyBatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 1.根据xml配置文件（全局配置文件）创建一个SqlSessionFactory
     *              有数据源一些运行环境信息
     * 2.sql映射文件，配置了每一个sql，以及sql的封装规则等。
     * 3.将sql映射文件注册在全局配置文件中
     * 4.写代码：
     *          1）根据全局配置文件得到SqlSessionFactory；
     *          2）使用sqlSession工厂，获取到sqlSession对象使用他来执行增删改查
     *                  一个sqlSession就是代表和数据库的一次会话，用完要关闭
     *          3）使用sql的唯一标识来告诉MyBatis执行哪个sql。sql都是保存在sql映射文件的。
     * @throws IOException
     */

    //旧版本
    @Test
    public void test() throws IOException {
        String resource = "mybatis-config.xml";
        //读取配置文件 com.mybatis-config.xml
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //根据配置文件构建 SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //2.获取SqlSession实例，能直接执行已经映射的sql语句
        SqlSession openSession = sqlSessionFactory.openSession();
        //第一个参数是sql唯一的标识符
        //第二个参数是执行sql要用的参数
        try {
            Employee selectOne = openSession.selectOne("mybatis.EmployeeMapper.selectEmp", 1);
            System.out.println(selectOne);
        }finally {
            //关闭资源
            openSession.close();
        }

    }


    //新版本
    @Test
    public void test01() throws IOException {
        //1.获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        //2.获取sqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            //3.获取接口的实现类对象
            //会为接口自动创建一个代理对象，代理对象去执行增删改查方法
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);

            System.out.println(mapper.getClass());
            System.out.println(employee);
        }finally {
            openSession.close();
        }

    }


    //没有sql映射文件，基于注解写在接口上
    @Test
    public void test2() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
            mybatis.bean.Employee empById = mapper.getEmpById(1);
            System.out.println(empById);
        }finally {
            openSession.close();
        }

    }
}
