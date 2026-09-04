package com.campus_delivery.service.impl;

import com.campus_delivery.constant.MessageConstant;
import com.campus_delivery.constant.StatusConstant;
import com.campus_delivery.context.BaseContext;
import com.campus_delivery.dto.CategoryDTO;
import com.campus_delivery.dto.CategoryPageQueryDTO;
import com.campus_delivery.entity.Category;
import com.campus_delivery.exception.DeletionNotAllowedException;
import com.campus_delivery.mapper.CategoryMapper;
import com.campus_delivery.mapper.DishMapper;
import com.campus_delivery.mapper.SetmealMapper;
import com.campus_delivery.result.PageResult;
import com.campus_delivery.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        //对象属性拷贝
        BeanUtils.copyProperties(categoryDTO, category);

        //设置分类状态，默认启用
        category.setStatus(StatusConstant.ENABLE);

        //公共字段由AutoFill切面自动填充

        //调用持久层，插入数据
        categoryMapper.insert(category);
    }

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);

        long total = page.getTotal();
        List<Category> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    public void deleteById(Long id) {
        //查询当前分类是否关联了菜品或套餐
        Integer dishCount = dishMapper.countByCategoryId(id);
        if (dishCount > 0) {
            //当前分类关联了菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        Integer setmealCount = setmealMapper.countByCategoryId(id);
        if (setmealCount > 0) {
            //当前分类关联了套餐，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        //没有关联，可以删除
        categoryMapper.deleteById(id);
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        //公共字段由AutoFill切面自动填充

        categoryMapper.update(category);
    }

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();

        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
