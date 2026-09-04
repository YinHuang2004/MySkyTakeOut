package com.campus_delivery.mapper;

import com.campus_delivery.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id集合统计关联的套餐数量
     * @param dishIds
     * @return
     */
    Long countRelateSetmeal(List<Long> dishIds);

    /**
     * 批量插入套餐菜品关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id集合批量删除套餐菜品关系
     * @param setmealIds
     */
    void deleteBySetmealIds(List<Long> setmealIds);

    /**
     * 根据套餐id查询套餐菜品关系
     * @param setmealId
     * @return
     */
    List<SetmealDish> getBySetmealId(Long setmealId);
}
