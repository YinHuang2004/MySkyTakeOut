package com.sky.service.impl;

<<<<<<< HEAD

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
=======
import com.sky.annotation.AutoFill;
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
<<<<<<< HEAD
import com.sky.exception.DeletionNotAllowedException;
=======
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
<<<<<<< HEAD

@Service
@Slf4j
public class DishServiceImpl implements DishService {

=======
@Slf4j
@Transactional
@Service
public class DishServiceImpl implements DishService {
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
<<<<<<< HEAD
   private SetmealDishMapper setMealDishMapper;

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

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表插入n条数据
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
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);//后绪步骤实现
        return new PageResult(page.getTotal(), page.getResult());
    }




    /**
     * 菜品批量删除
     *
     * @param ids
     */
    @Transactional//事务
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能够删除---是否存在起售中的菜品？？
        Long countOnSale=dishMapper.countOnSale(ids);
        if(countOnSale>0){
            //菜品处于起售中，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        //判断当前菜品是否能够删除---是否被套餐关联了？？
       Long countRelateSetmeal = setMealDishMapper.countRelateSetmeal(ids);
        if (countRelateSetmeal>0) {
            //当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品表中的菜品数据及其味道
        dishMapper.deleteBatch(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查询菜品和对应的口味数据
     *
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
        //根据id查询菜品数据
        Dish dish = dishMapper.getById(id);

        //根据菜品id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);//后绪步骤实现

        //将查询到的数据封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 根据id修改菜品基本信息和对应的口味信息
     *
     * @param dishDTO
     */
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
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 修改菜品售卖状态
     * @param status
     */
    @Override
    public void startOrStop(Integer status) {
        Dish dish=new Dish();
        dish.setStatus(status);
=======
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    @AutoFill(value=OperationType.INSERT)
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        //获取insert语句生成的主键值
        Long id=dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null){
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
        }
        dishFlavorMapper.insertBatch(flavors);
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //先获取总页数
        Long total=dishMapper.getTotal(dishPageQueryDTO);
        log.info("获取到的总记录数：{}",total);
        int page = dishPageQueryDTO.getPage();
        int pageSize = dishPageQueryDTO.getPageSize();
        page=page<0?0:page;
        dishPageQueryDTO.setPage((page-1)*pageSize);
        List<DishVO> records=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(total,records);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否可以被删除
        //首先判断该菜品是否是起售状态

        //新建一个计数器，计算起售个数是否大于0
        Long enableCount=dishMapper.countEnableDishByIds(ids);
        if(enableCount>0)throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        //其次判断该菜品是否被套餐关联
        List<Long>setmealIds=setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品表中的菜品数据
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }
    @Transactional
    @Override
    public DishVO getInfoById(Long id) {
        //符合单一职责原则
        //首先根据菜名id返回dish表基础字段
        Dish dish = dishMapper.getInfoById(id);
        //其次根据id返回该菜的口味
        List<DishFlavor> dishFlavor=dishFlavorMapper.getInfoByDishId(id);
        //最后根据id返回该菜所属的分类
//        String categoryName=categoryMapper.getInfoByCategoryId(id);
        //这里应该是根据返回的菜的所属分类id来查询其所属分类，而不是根据菜的id
        String categoryName=categoryMapper.getInfoByCategoryId(dish.getCategoryId());
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setCategoryName(categoryName);
        dishVO.setFlavors(dishFlavor);
        return dishVO;
    }

    @Override
    @AutoFill(value= OperationType.UPDATE)
    public void update(DishDTO dishDTO) {
        Dish dish=new Dish();
        //首先修改dish表关于鱼的基础信息
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //其次根据dish_id修改dishflavor的口味
        //首先删除
        dishFlavorMapper.deleteByDishIds(Arrays.asList(dish.getId()));
        //其次添加
        dishFlavorMapper.insertBatch(dishDTO.getFlavors());
    }

    @Override
    public void startOrStop(Integer status,Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
        dishMapper.update(dish);
    }

    @Override
<<<<<<< HEAD
    public List<Dish> getDishByCategoryId(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);

    }
}
=======
    public List<Dish> getDishByCageId(Long categoryId) {
        return dishMapper.getDishByCateId(categoryId);
    }
}
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
