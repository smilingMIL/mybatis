package mybatis.dao;

import mybatis.bean.Employee;

/**
 * @author smile
 */
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

}
