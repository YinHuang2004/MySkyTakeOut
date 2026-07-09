package com.sky.service.impl;

<<<<<<< HEAD
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.config.BCryptPwdEncoderConfiguration;
=======
import com.fasterxml.jackson.databind.ser.Serializers;
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
<<<<<<< HEAD
import com.sky.dto.PasswordEditDTO;
=======
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

=======
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
<<<<<<< HEAD
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
=======

>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

<<<<<<< HEAD
        if (!bCryptPasswordEncoder.matches(password, employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
=======
        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

<<<<<<< HEAD
=======

    /**
     * 添加员工
     * @param employeeDTO
     */
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
<<<<<<< HEAD
        //默认为合法的状态
        employee.setStatus(StatusConstant.ENABLE);
        //默认密码为123456，使用bc算法加密
        employee.setPassword(bCryptPasswordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD));
=======
        employee.setStatus(1);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        employee.setStatus(StatusConstant.ENABLE);
        //设置加密密码
//        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //记录当前记录创建人id
        //TODO 后期需要真实修改当前登陆用户的创建人id
//        employee.setCreateUser(BaseContext.getCurrentId());
        //记录当前记录修改人
//        employee.setUpdateUser(BaseContext.getCurrentId());
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
<<<<<<< HEAD
        //设置分页参数
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //分页查询
        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);
        long total=page.getTotal();
        List<Employee> records=page.getResult();
=======
        //开始分页查询
        Long total=employeeMapper.getTotal();
        int page = employeePageQueryDTO.getPage();
        int pageSize = employeePageQueryDTO.getPageSize();
        page=page<0?0:page;
        page=(page-1)*pageSize;
        employeePageQueryDTO.setPage(page);
        List<Employee> records=employeeMapper.pageQuery(employeePageQueryDTO);
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
        return new PageResult(total,records);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
<<<<<<< HEAD
        Employee employee=Employee.builder().status(status).id(id).build();
        employeeMapper.update(employee);

    }

    @Override
    public Employee getInfoById(Long id) {
        Employee employee=employeeMapper.getById(id);
        employee.setPassword("******");
        return employee;
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO
     */
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeMapper.update(employee);
    }

    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
       //获取当前登录人的id，然后判断id和密码是否相等，如果想等则设置新密码
        Long id=BaseContext.getCurrentId();

        //查询密码是否正确
     Employee employee=employeeMapper.getById(id);
        String password=passwordEditDTO.getNewPassword();

       if(!bCryptPasswordEncoder.matches(password,employee.getPassword())){
           throw new AccountNotFoundException(MessageConstant.PASSWORD_ERROR);
       }
       //密码正确后我们就设置新密码
        //对新密码加密并复用mapper层的update进行密码修改
       password=bCryptPasswordEncoder.encode(password);
       employee.setPassword(password);
=======
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.update(employee);
    }

    @Override
    public Employee getById(Long id) {
        Employee employee=Employee.builder().id(id).build();
        employee.setPassword("******");
        return employeeMapper.getById(id);
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        //首先拷贝基础数据
        Employee employee=new Employee();
        //拷贝基础数据
        BeanUtils.copyProperties(employeeDTO,employee);
        //然后重置修改时间
//        employee.setUpdateTime(LocalDateTime.now());
        //并且重置修改人
       // employee.setCreateUser(BaseContext.getCurrentId());
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
        employeeMapper.update(employee);
    }

}
