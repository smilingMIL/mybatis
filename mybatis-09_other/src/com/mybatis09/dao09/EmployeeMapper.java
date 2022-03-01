package com.mybatis09.dao09;

import com.mybatis09.bean09.Employee;

import java.util.List;

/**
 * @author smile
 */
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public List<Employee> getEmps();

    public Long addEmp(Employee employee);
}
