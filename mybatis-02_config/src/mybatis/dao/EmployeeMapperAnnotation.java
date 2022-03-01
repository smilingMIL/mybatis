package mybatis.dao;

import mybatis.bean.Employee;
import org.apache.ibatis.annotations.Select;

/**
 * @author smile
 */
public interface EmployeeMapperAnnotation {

    @Select("select * from tbl_employee where id = #{id}")
    public Employee getEmpById(Integer id);
}
