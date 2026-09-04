package com.campus_delivery.controller.admin;

import com.campus_delivery.dto.DishDTO;
import com.campus_delivery.dto.DishPageQueryDTO;
import com.campus_delivery.entity.Dish;
import com.campus_delivery.result.PageResult;
import com.campus_delivery.result.Result;
import com.campus_delivery.service.DishService;
import com.campus_delivery.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    @CacheEvict(cacheNames = "dishCache",key="#dishDTO.categoryId")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
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
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    @CacheEvict(cacheNames = "dishCache",allEntries = true)//删掉所有以dish开头的请求
    //为什么要删掉所有以dish开头的菜品呢？因为管理员批量删除菜品时可能菜品属于不同的分类
    //如果修改了菜品所属分类，那么我们应该把菜品的旧分类和新分类都删掉（不然导致数据不一致，假设用户又查询老分类菜品，那么还是显示旧数据）
    //假设你要删除的菜品ID列表为 [101, 102]，但你不确定：
    //菜品101属于哪个分类？（可能是分类1）
    //菜品102属于哪个分类？（可能是分类2）
    //如果写 key = "#ids"，删除的缓存key是 dish::[101,102]，但实际需要清除的是 dish::1 和 dish::2
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除：{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
        //当然你也可以先收集这些菜品的所属分类id，然后通过redistemplate删除
//        List<Long>categoryIdList=dishService.getCategoryIdsByIds(ids);
//        for (Long categoryId : categoryIdList) {
//            String key="dish_"+categoryId;
//            redisTemplate.delete(key);
//        }
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
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        return Result.success();
    }

    /**
     * 修改菜品售卖状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("修改菜品售卖状态")
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    //为什么也修改所有，因为你需要实现方法：通过菜品id获取对应分类，复杂化
    public Result startOrStop(@PathVariable Integer status, @RequestParam("id") Long id) {
        log.info("修改菜品售卖状态为:{}, id:{}", status, id);
        dishService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> getDishByCategoryId(Long categoryId) {
        log.info("根据分类id查询菜品:{}", categoryId);
        List<Dish> list = dishService.getDishByCategoryId(categoryId);
        return Result.success(list);
    }
}