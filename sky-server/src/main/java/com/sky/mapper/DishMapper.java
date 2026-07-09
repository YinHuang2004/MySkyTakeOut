package com.sky.mapper;

<<<<<<< HEAD
import com.github.pagehelper.Page;
=======
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
<<<<<<< HEAD
=======
import org.apache.ibatis.annotations.Select;
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
<<<<<<< HEAD
    Integer countByCategoryId(Long categoryId);


    /**
     * 插入菜品数据
     *
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);



    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);


    /**
     * 统计在售菜品数量
     * @param ids
     * @return
     */
    Long countOnSale(List<Long> ids);

    void deleteBatch(List<Long> ids);

    Dish getById(Long id);


    /**
     * 根据id动态修改菜品数据
     *
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> getDishByCategoryId(Long categoryId);
=======
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Long countByCategoryId(Long categoryId);
    @AutoFill(value= OperationType.INSERT)
    void insert(Dish dish);

    Long getTotal(DishPageQueryDTO dishPageQueryDTO);

    List<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
    

    void deleteByIds(List<Long> ids);

    Long countEnableDishByIds(List<Long> ids);

    Dish getInfoById(Long id);

    void update(Dish dish);

    List<Dish> getDishByCateId(Long categoryId);
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
}
