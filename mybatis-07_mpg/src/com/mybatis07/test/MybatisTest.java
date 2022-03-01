package com.mybatis07.test;

import com.mybatis07.bean.Employee;
import com.mybatis07.bean.EmployeeExample;
import com.mybatis07.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author smile
 */
public class MybatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //利用SqlSessionFactoryBuilder创建SqlSessionFactory
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void testMBG() throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File("mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

    @Test
    public void testSimple() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            List<Employee> employees = mapper.selectByExample(null);
            for (Employee employee: employees) {
                System.out.println(employee.getLastName());
            }
        }finally {
            openSession.close();
        }
    }


    @Test
    public void testMyBatis3() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            //xxxExample就是封装查询条件的
            //1.查询所有
            List<Employee> employees = mapper.selectByExample(null);
            //2.查询员工名字有p字母，和员工性别是0的
            //封装员工查询条件的
            EmployeeExample employeeExample = new EmployeeExample();
            //创建一个Criteria，这个就是拼装查询条件
            EmployeeExample.Criteria criteria = employeeExample.createCriteria();
            criteria.andLastNameLike("%p%");
            criteria.andGenderEqualTo("0");
            List<Employee> employees1 = mapper.selectByExample(employeeExample);

            for (Employee employee: employees1) {
                System.out.println(employee.getLastName());
            }
        }finally {
            openSession.close();
        }
    }
}
