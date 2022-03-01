package com.batis.dao03;

import com.batis.bean03.Employee;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author smile
 */
public interface EmployeeMapper {
    //返回为list
    public List<Employee> getEmps();

    //返回一条记录的map：key就是列名，value就是对应的值
    public Map<String,Object> getEmpByIdMap(Integer id);

    //查询多条记录封装一个map，Map<Integer,Employee>:键是这条记录的主键，值是记录封装后的javabean对象
    @MapKey("id")//告诉mybatis封装这个map的时候使用哪个属性作为主键
    public  Map<Integer,Employee> getEmpsMap();

    public Employee getEmpById(Integer id);

    public void addEmp(Employee employee);

    public void updateEmp(Employee employee);

    public void deleteEmpById(Integer id);
}
