package com.mybatis06.dao;

import com.mybatis06.bean.Employee;

import java.util.List;

/**
 * @author smile
 */
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public List<Employee> getEmps();
}
