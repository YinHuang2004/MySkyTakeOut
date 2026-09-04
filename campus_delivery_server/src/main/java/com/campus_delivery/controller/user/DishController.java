package com.campus_delivery.controller.user;

import com.campus_delivery.constant.StatusConstant;
import com.campus_delivery.entity.Dish;
import com.campus_delivery.result.Result;
import com.campus_delivery.service.DishService;
import com.campus_delivery.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    @Cacheable(cacheNames="dish",key="#categoryId")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("根据分类id查询菜品:{}",categoryId);
        List<DishVO> list = dishService.listWithFlavor(categoryId);
        return Result.success(list);
    }

}
