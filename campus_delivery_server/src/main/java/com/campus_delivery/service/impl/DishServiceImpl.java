package com.campus_delivery.service.impl;

import com.campus_delivery.constant.MessageConstant;
import com.campus_delivery.constant.StatusConstant;
import com.campus_delivery.dto.DishDTO;
import com.campus_delivery.dto.DishPageQueryDTO;
import com.campus_delivery.entity.Dish;
import com.campus_delivery.entity.DishFlavor;
import com.campus_delivery.exception.DeletionNotAllowedException;
import com.campus_delivery.mapper.DishFlavorMapper;
import com.campus_delivery.mapper.DishMapper;
import com.campus_delivery.mapper.SetmealDishMapper;
import com.campus_delivery.result.PageResult;
import com.campus_delivery.service.DishService;
import com.campus_delivery.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.*;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品和对应的口味
     *
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //向菜品表插入1条数据
        dishMapper.insert(dish);

        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        //向口味表插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表批量插入数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 菜品批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能够删除---是否存在起售中的菜品
        Long onSaleCount = dishMapper.countOnSale(ids);
        if (onSaleCount > 0) {
            //起售中的菜品不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        //判断当前菜品是否能够删除---是否被套餐关联
        Long relateSetmealCount = setmealDishMapper.countRelateSetmeal(ids);
        if (relateSetmealCount > 0) {
            //当前菜品关联了套餐,不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品表中的菜品数据
        dishMapper.deleteBatch(ids);

        //删除菜品关联的口味数据
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查询菜品和对应的口味
     *
     * @param dishId
     * @return
     */
    public DishVO getByIdWithFlavor(Long dishId) {
        //根据id查询菜品数据
        Dish dish = dishMapper.getById(dishId);

        //根据菜品id查询口味数据
        List<DishFlavor> flavors = dishFlavorMapper.getByDishIds(Arrays.asList(dishId));

        //将查询到的数据封装到DishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    /**
     * 根据id修改菜品基本信息和对应的口味信息
     *
     * @param dishDTO
     */
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //修改菜品表基本信息
        dishMapper.update(dish);

        //删除原有的口味数据

        dishFlavorMapper.deleteByDishIds(Arrays.asList(dishDTO.getId()));
        //重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品起售、停售
     *
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    public List<Dish> getDishByCategoryId(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);
    }

    /**
     * 条件查询菜品和口味
     *
     * @param categoryId
     * @return
     */
    public List<DishVO> listWithFlavor(Long categoryId) {
        //根据分类id查询所有菜品
        List<Dish> dishList = dishMapper.getDishByCategoryId(categoryId);
        //提取所有菜品id
        List<Long> dishIds = new ArrayList<>();
        for (Dish dish : dishList) {
            dishIds.add(dish.getId());
        }
        //批量查询菜品的口味
        List<DishFlavor> allFlavors = dishFlavorMapper.getByDishIds(dishIds);
        //按照dishId将口味进行分组
        Map<Long, List<DishFlavor>> flavorMap = new HashMap<>();
        for (DishFlavor flavor : allFlavors) {
            Long dishId = flavor.getDishId();
            if (!flavorMap.containsKey(dishId)) {
                flavorMap.put(dishId, new ArrayList<>());
            }
            flavorMap.get(dishId).add(flavor);
        }
        //组装结果
        List<DishVO> dishVOList = new ArrayList<>();
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            // 获取口味列表
            List<DishFlavor> flavors = flavorMap.get(d.getId());
            if (flavors == null) {
                flavors = new ArrayList<>();
            }
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);

        }
        return dishVOList;
    }
}
