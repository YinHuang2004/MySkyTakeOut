package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
<<<<<<< HEAD
    /**
     * 批量插入口味数据
     * @param flavors
     */
=======
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
    void insertBatch(List<DishFlavor> flavors);

    void deleteByDishIds(List<Long> ids);

<<<<<<< HEAD
    /**
     * 根据菜品id查询对应的口味数据
     * @param dishId
     * @return
     */

    List<DishFlavor> getByDishId(Long dishId);


}
=======
    List<DishFlavor> getInfoByDishId(Long id);
}
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
