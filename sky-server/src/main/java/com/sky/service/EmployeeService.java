package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
<<<<<<< HEAD
import com.sky.dto.PasswordEditDTO;
=======
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, Long id);

<<<<<<< HEAD
    Employee getInfoById(Long id);

    void update(EmployeeDTO employeeDTO);

    void editPassword(PasswordEditDTO passwordEditDTO);
=======
    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
}
