package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端地址簿接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result save(@RequestBody AddressBook addressBook){
        log.info("员工新增地址:{}",addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 查询当前用户所有地址信息
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        log.info("查询当前登录用户的所有地址信息");

        return Result.success(addressBookService.list());
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefault(){
        log.info("查询当前用户默认地址");
        AddressBook addressBook = addressBookService.getDefault();
        //判断当前用户是否存在默认地址
        if(addressBook!=null){
            return Result.success(addressBook);
        }
        //有可能当前用户不存在默认地址，那么就提示信息
        return Result.error("当前用户不存在默认地址");
    }
    /**
     * 根据id修改地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }
    /**
     * 根据id删除地址
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

}
