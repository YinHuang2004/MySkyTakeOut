package com.sky.service.impl;

import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
@Slf4j
@Service
public class SetMeatServiceImpl implements SetMealService {
    @Autowired
    private SetmealMapper setMealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;





    //因为要操作多表，所以需要有事务
    @Transactional
    @Override
    @AutoFill(value= OperationType.INSERT)
    public void save(SetmealDTO setmealDTO) {
        //首先保存到套餐
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setMealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();
        //保存套餐生成的id要返回来赋值给套餐菜品表
        List<SetmealDish> setmealDishList=setmealDTO.getSetmealDishes();
        setmealDishList.forEach(setmealDish-> setmealDish.setSetmealId(setmealId));
        //其次保存到中间表，当前套餐包含几种菜品
        setmealDishMapper.insertBatch(setmealDishList);

    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        //统计总记录数
        Long count=setMealMapper.getTotal(setmealDishMapper);
        log.info("获取到的记录数:{}",count);
        //需要多表查询（因为categoryname是其他表的）
        int page = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        page=page<0?0:page;
        setmealPageQueryDTO.setPage((page-1)*pageSize);
        List<SetmealVO>setmealVOList=setMealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(count,setmealVOList);
    }
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        //如果存在在售的物品就抛出异常
        Long countOnSale=setMealMapper.countOnSaleByIds(ids);
        if(countOnSale>0)throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        //否则就批量删除套餐
        //首先删除套餐表
        setMealMapper.deleteBatch(ids);
        //其次删除菜品套餐表
        setmealDishMapper.deleteBatch(ids);
    }

    @Override
    public SetmealVO getByIdWithDish(Long id) {
        return null;
    }

    @Override
    public void update(SetmealDTO setmealDTO) {

    }
}
