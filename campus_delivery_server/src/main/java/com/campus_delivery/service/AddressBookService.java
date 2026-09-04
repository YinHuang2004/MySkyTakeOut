package com.campus_delivery.service;

import com.campus_delivery.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 新增用户地址
     * @param addressBook
     */
    public void save(AddressBook addressBook);

    /**
     * 查询当前用户的所有地址
     * @return
     */
    List<AddressBook> list();

    AddressBook getDefault();

    void update(AddressBook addressBook);
    void deleteById(Long id);

    AddressBook getById(Long id);

    void setDefault(AddressBook addressBook);
}
