package com.sky.mapper;

<<<<<<< HEAD
import com.sky.entity.SetmealDish;
=======
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealVO;
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
<<<<<<< HEAD
    /**
     * 统计菜品被套餐关联的数量
     * @param ids
     * @return
     */
    Long countRelateSetmeal(List<Long> ids);

    /**
     * 批量保存套餐和菜品的关联关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    void deleteBySetmealIds(List<Long> ids);

    /**
     * 根据套餐id查询对应菜品
     * @param id
     * @return
     */
    List<SetmealDish> getBySetmealId(Long id);


}
=======
    List<Long> getSetmealIdsByDishIds(List<Long> ids);

    void insertBatch(List<SetmealDish> setmealDishList);

    void deleteBatch(List<Long> ids);
}
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
