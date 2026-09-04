package com.campus_delivery.service;

import com.campus_delivery.dto.SetmealDTO;
import com.campus_delivery.dto.SetmealPageQueryDTO;
import com.campus_delivery.entity.Setmeal;
import com.campus_delivery.result.PageResult;
import com.campus_delivery.vo.DishItemVO;
import com.campus_delivery.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐和对应的菜品关系
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐和对应的菜品关系
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 修改套餐和对应的菜品关系
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    List<Setmeal> list(Setmeal setmeal);
    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
