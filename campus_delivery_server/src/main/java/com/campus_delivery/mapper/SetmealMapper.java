package com.campus_delivery.mapper;

import com.github.pagehelper.Page;
import com.campus_delivery.annotation.AutoFill;
import com.campus_delivery.dto.SetmealPageQueryDTO;
import com.campus_delivery.entity.Setmeal;
import com.campus_delivery.enumeration.OperationType;
import com.campus_delivery.vo.DishItemVO;
import com.campus_delivery.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐数量
     * @param categoryId
     * @return
     */
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入套餐数据
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 统计在售套餐数量（根据id集合）
     * @param ids
     * @return
     */
    Long countOnSaleSetmeal(List<Long> ids);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    Setmeal getById(Long id);

    /**
     * 根据id动态修改套餐数据
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);
    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */

    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
