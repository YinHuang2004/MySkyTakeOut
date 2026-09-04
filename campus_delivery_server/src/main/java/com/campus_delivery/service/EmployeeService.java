package com.campus_delivery.service;

import com.campus_delivery.dto.EmployeeDTO;
import com.campus_delivery.dto.EmployeeLoginDTO;
import com.campus_delivery.dto.EmployeePageQueryDTO;

import com.campus_delivery.dto.PasswordEditDTO;

import com.campus_delivery.entity.Employee;
import com.campus_delivery.result.PageResult;

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


    Employee getInfoById(Long id);

    void update(EmployeeDTO employeeDTO);

    void editPassword(PasswordEditDTO passwordEditDTO);

}
