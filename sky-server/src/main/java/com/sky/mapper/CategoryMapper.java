package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
<<<<<<< HEAD
import com.sky.enumeration.OperationType;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
=======
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 插入数据
     * @param category
     */
<<<<<<< HEAD
    @AutoFill(OperationType.INSERT)
=======
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value= OperationType.INSERT)
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
    void insert(Category category);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据id删除分类
     * @param id
     */
<<<<<<< HEAD
=======
    @Delete("delete from category where id = #{id}")
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
    void deleteById(Long id);

    /**
     * 根据id修改分类
     * @param category
     */
<<<<<<< HEAD
    @AutoFill(OperationType.UPDATE)
=======
    @AutoFill(value=OperationType.UPDATE)
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
    void update(Category category);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);
<<<<<<< HEAD
}
=======

    String getInfoByCategoryId(Long categoryId);
}
>>>>>>> 424555f2080e30ea9bb7a8fb209e617ef52310b6
