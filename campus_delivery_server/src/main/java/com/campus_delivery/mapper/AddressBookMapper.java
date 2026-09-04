package com.campus_delivery.mapper;

import com.campus_delivery.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    void insert(AddressBook addressBook);


    List<AddressBook> list(AddressBook addressBook);


    /**
     * 根据id修改
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id
     */

    void deleteById(Long id);
    /**
     * 根据id查询
     * @param id
     * @return
     */

    AddressBook getById(Long id);

    void updateDefaultByUserId(AddressBook addressBook);
}
