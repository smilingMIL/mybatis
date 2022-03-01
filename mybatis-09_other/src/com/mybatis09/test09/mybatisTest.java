package com.mybatis09.test09;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mybatis09.bean09.Employee;
import com.mybatis09.dao09.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
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


    //分页插件测试
    @Test
    public void testgetEmps() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Page<Object> page = PageHelper.startPage(1, 2);
            List<Employee> emps = mapper.getEmps();
            //PageInfo<Employee> info = new PageInfo<>(emps);
            //传入连续显示多少页
            PageInfo<Employee> info = new PageInfo<>(emps, 2);
            for (Employee employee: emps) {
                System.out.println(employee);
            }
            /*System.out.println("当前页码："+page.getPageNum());
            System.out.println("总记录数："+page.getTotal());
            System.out.println("每页的记录数："+page.getPageSize());
            System.out.println("总页码"+page.getPages());*/

            System.out.println("当前页码："+info.getPageNum());
            System.out.println("总记录数："+info.getTotal());
            System.out.println("每页的记录数："+info.getPageSize());
            System.out.println("总页码"+info.getPages());
            System.out.println("是否第一页："+info.isIsFirstPage());
            System.out.println("连续显示的页码:");
            int[] nums = info.getNavigatepageNums();
            for(int i = 0;i<nums.length;i++){
                System.out.println(nums[i]);
            }
        }finally {
            openSession.close();
        }
    }

    public void testaddEmp() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //可以执行批量操作的sqlSession
        SqlSession openSession = sqlSessionFactory.openSession(ExecutorType.BATCH);

        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);

        }finally {
            openSession.close();
        }
    }


    //存储过程
    @Test
    public void testProcedure(){

    }
}
