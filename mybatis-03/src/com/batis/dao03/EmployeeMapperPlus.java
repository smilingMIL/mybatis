package com.batis.dao03;

import com.batis.bean03.Employee;

import java.util.List;

/**员工信息
 * @author smile
 */
public interface EmployeeMapperPlus {

    public Employee getEmpById(Integer id);

    public Employee getEmpAndDept(Integer id);

    public Employee getEmpByStep(Integer id);

    //根据部门id查询所有员工
    public List<Employee> getEmpsByDeptId(Integer id);
}
