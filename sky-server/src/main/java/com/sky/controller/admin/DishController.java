package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
=======

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }
    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);//后绪步骤定义
        return Result.success(pageResult);
    }
<<<<<<< HEAD


=======
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
    /**
     * 菜品批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
<<<<<<< HEAD
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除：{}", ids);
        dishService.deleteBatch(ids);//后绪步骤实现
        return Result.success();
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }
    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 修改菜品售卖状态
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("修改菜品售卖状态")
    public Result startOrStop(@PathVariable Integer status){
        log.info("修改菜品售卖状态为:{}",status);
        dishService.startOrStop(status);
        return Result.success();
    }



    @GetMapping("list")
    @ApiOperation("根据分类id查询菜品")
    public Result getDishByCategoryId(Long categoryId){
        log.info("根据分类id查询菜品:{}",categoryId);
        List<Dish>list=dishService.getDishByCategoryId(categoryId);
        return Result.success(list);
    }


}
=======
    public Result delete(@RequestParam List<Long> ids){
        log.info("根据id批量删除菜品:{}",ids);
        dishService.deleteBatch(ids);
        return null;
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO>getInfoById(@PathVariable Long id){
        log.info("根据id查询菜品信息:{}",id);
        DishVO dishvo=dishService.getInfoById(id);
        return Result.success(dishvo);
    }
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品信息为：{}",dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }
    @PostMapping("/status/{status}")
    @ApiOperation("修改菜品起售状态")
    public Result startOrStop(@PathVariable Integer status,@RequestParam("id") Long id){
        log.info("修改菜品信息为：{}",status);
        dishService.startOrStop(status,id);
        return Result.success();
    }
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result selectDishByCageId(Long categoryId){
        log.info("根据分类id查询菜品:{}",categoryId);
        List<Dish>dishList=dishService.getDishByCageId(categoryId);
        return Result.success(dishList);
    }

}
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
