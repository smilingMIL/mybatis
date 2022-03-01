package com.mybatis04.test04;

import com.mybatis04.bean04.Department;
import com.mybatis04.bean04.Employee;
import com.mybatis04.dao04.EmployeeMapperDynamicSQL;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void test01() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //测试 if\where
            Employee employee = new Employee(null,"%S%","PSW@qq.com",null);
            List<Employee> empsByConditionIf = mapper.getEmpsByConditionIf(employee);
            for (Employee emp:empsByConditionIf) {
                System.out.println(emp);
            }
            System.out.println("===================================================================");

            //查询的时候如果某些条件可能sql拼装会有问题
            //1、给where 后面加上1=1，后面的条件都and xxx；
            //2、mybatis 使用 where标签 来将所有的查询条件包括在内。mybatis就会将where标签中拼装的sql，多出来的and或者or去掉
                //where只会去掉第一个多出来的and或者or

            //测试Trim
            List<Employee> empsByConditionTrim = mapper.getEmpsByConditionTrim(employee);
            for (Employee emp:empsByConditionTrim) {
                System.out.println(emp);
            }
            System.out.println("===================================================================");


            //测试choose
            List<Employee> empsByConditionChoose = mapper.getEmpsByConditionChoose(employee);
            for (Employee emp:empsByConditionChoose) {
                System.out.println(emp);
            }
            System.out.println("===================================================================");


            //测试set
            Employee employee1 = new Employee(1,"psw",null,null);
            mapper.updateEmp(employee1);
//            提交
            openSession.commit();
            System.out.println("===================================================================");

            //测试foreach
            List<Employee> empsByConditionForeach = mapper.getEmpsByConditionForeach(Arrays.asList(1, 2, 3));
            for (Employee employee2:empsByConditionForeach) {
                System.out.println(employee2);
            }

        }finally {
            openSession.close();
        }

    }

    @Test
    public void test02() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //测试批量添加
            List<Employee> emps = new ArrayList<>();
            emps.add(new Employee(null,"zw","zw@qq.com","1",new Department(1)));
            emps.add(new Employee(null,"lye","lye@qq.com","0",new Department(2)));
            mapper.addEmps(emps);
            openSession.commit();
        }finally {
            openSession.close();
        }
    }



    @Test
    public void test03() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee2 = new Employee();
            employee2.setLasName("p");
            List<Employee> empsTestInnerParameter = mapper.getEmpsTestInnerParameter(employee2);
            for (Employee employee: empsTestInnerParameter) {
                System.out.println(employee);
            }
        }finally {
            openSession.close();
        }

    }


}
