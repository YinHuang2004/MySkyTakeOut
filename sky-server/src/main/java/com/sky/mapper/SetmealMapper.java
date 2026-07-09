package com.sky.mapper;

<<<<<<< HEAD
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
=======
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
<<<<<<< HEAD
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    Integer countByCategoryId(Long id);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);


    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    Long countOnSaleSetmeal(List<Long> ids);

    void deleteBatch(List<Long> ids);

    Setmeal getById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
=======
     * 根据分类id查询套餐下菜品的数量
     * 根据分类
     * @param id
     * @return
     */
    Long countByCategoryId(Long id);

    void insert(Setmeal setmeal);

    Long getTotal(SetmealDishMapper setmealDishMapper);
//需要查询多张表，所以需要用到多表查询，已经用vo封装好了
    List<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    Long countOnSaleByIds(List<Long> ids);

    void deleteBatch(List<Long> ids);
}
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
