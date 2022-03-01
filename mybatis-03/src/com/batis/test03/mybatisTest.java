package com.batis.test03;

import com.batis.bean03.Department;
import com.batis.dao03.DepartmentMapper;
import com.batis.dao03.EmployeeMapper;
import com.batis.bean03.Employee;
import com.batis.dao03.EmployeeMapperPlus;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
    public void test() throws IOException {
        //1.获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2.获取sqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            //根据id查询
/*            Employee empById = mapper.getEmpById(1);
            System.out.println(empById);*/

            //查询全部
/*            List<Employee> emps = mapper.getEmps();
            for (Employee employee: emps ) {
                System.out.println(employee);
            }*/

            //根据id查询返回map

/*            Map<String, Object> empByIdMap = mapper.getEmpByIdMap(1);
            System.out.println(empByIdMap);*/

            //查询全部数据封装在map
            Map<Integer, Employee> empsMap = mapper.getEmpsMap();
            System.out.println(empsMap);
        } finally {
            openSession.close();
        }
    }


    /**
     * 测试增删改
     * 1.mybatis允许增删改直接定义以下类型的返回值
     * Integer,long,Boolean
     * 2.需要手动提交数据   openSession.commit();
     *
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1.获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);

            //测试添加操作
/*            Employee employee = new Employee(null, "jerry", "jerry@qq.com", "1");
            mapper.addEmp(employee);
            System.out.println(employee.getId());*/

            //测试修改操作
/*            Employee employee = new Employee(1, "PSW", "PSW@qq.com", "0");
            mapper.updateEmp(employee);*/

            //测试删除操作
            /*mapper.deleteEmpById(2);*/

            //2.需要手动提交数据
            openSession.commit();
        } finally {
            openSession.close();
        }

    }

    @Test
    public void test3() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperPlus mapper1 = openSession.getMapper(EmployeeMapperPlus.class);

            /*Employee empById = mapper1.getEmpById(1);
            System.out.println(empById);*/

/*            Employee empAndDept = mapper1.getEmpAndDept(1);
            System.out.println(empAndDept);
            System.out.println(empAndDept.getDept());*/

            Employee empById = mapper1.getEmpByStep(3);
            System.out.println(empById);
            System.out.println(empById.getDept());
        }finally {
            openSession.close();
        }

    }


    @Test
    public void test4() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);

 /*           Department deptByIdPlus = mapper.getDeptByIdPlus(1);
            System.out.println(deptByIdPlus);
            System.out.println(deptByIdPlus.getEmps());*/

            Department deptByIdStep = mapper.getDeptByIdStep(1);
            System.out.println(deptByIdStep);
            System.out.println(deptByIdStep.getEmps());
        }finally {
            openSession.close();
        }

    }

}
