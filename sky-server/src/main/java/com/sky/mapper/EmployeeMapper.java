package com.sky.mapper;

<<<<<<< HEAD
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
=======
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

<<<<<<< HEAD
=======
import java.util.List;

>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */

    Employee getByUsername(String username);
<<<<<<< HEAD
    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);

    Employee getById(Long id);

=======
    @AutoFill(value= OperationType.INSERT)
    void insert(Employee employee);

    Long getTotal();

    List<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);



    Employee getById(Long id);
    @AutoFill(value= OperationType.UPDATE)
    void update(Employee employee);
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
}
