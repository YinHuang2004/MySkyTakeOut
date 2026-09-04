package com.campus_delivery.controller.admin;

import com.campus_delivery.constant.JwtClaimsConstant;
import com.campus_delivery.dto.EmployeeDTO;
import com.campus_delivery.dto.EmployeeLoginDTO;
import com.campus_delivery.dto.EmployeePageQueryDTO;
import com.campus_delivery.dto.PasswordEditDTO;
import com.campus_delivery.entity.Employee;
import com.campus_delivery.properties.JwtProperties;
import com.campus_delivery.result.PageResult;
import com.campus_delivery.result.Result;
import com.campus_delivery.service.EmployeeService;
import com.campus_delivery.utils.JwtUtil;
import com.campus_delivery.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());//放员工id到jwt令牌中，key为员工属性id，value为员工id值
        //传进来的配置项用java对象封装
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),//key为管理端员工生成jwt令牌相关配置中的密钥
                jwtProperties.getAdminTtl(),//ttl为管理端员工生成jwt令牌相关配置中的过期时间
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                //将员工id、用户名、姓名、jwt令牌通过方法封装到EmployeeLoginVO对象中，只是方法名与属性名一致（前提是添加builder注解）
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工,employeeDTO:{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询，参数为：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用禁用员工账号：状态：{},id：{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据id回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id回显数据")
    public Result<Employee> getInfoById(@PathVariable Long id) {
        log.info("根据id回显数据:{}", id);
        Employee employee = employeeService.getInfoById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 修改员工密码
     * @param passwordEditDTO
     * @return
     */
    @PutMapping("/editPassword")
    @ApiOperation("修改员工密码")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改员工密码:{}", passwordEditDTO);
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }
}