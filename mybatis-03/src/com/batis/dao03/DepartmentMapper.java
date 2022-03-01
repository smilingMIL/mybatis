package com.batis.dao03;

import com.batis.bean03.Department;

/**部门信息
 * @author smile
 */
public interface DepartmentMapper {

    public Department getDeptById(Integer id);

    //Collection 集合类型&嵌套结果集
    public Department getDeptByIdPlus(Integer id);

    //Collection分步查询
    public Department getDeptByIdStep(Integer id);
}
