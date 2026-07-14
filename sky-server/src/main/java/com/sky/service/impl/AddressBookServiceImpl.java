package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressBook
     */
    @Override
    @Transactional
    public void save(AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * 查询当前用户的所有地址
     * @return
     */
    @Override
    public List<AddressBook> list() {
        AddressBook addressBook= AddressBook.builder().userId(BaseContext.getCurrentId()).build();
         return addressBookMapper.list(addressBook);

    }

    @Override
    public AddressBook getDefault() {

        AddressBook addressBook=AddressBook.builder().userId(BaseContext.getCurrentId()).isDefault(1).build();
        List<AddressBook> list = addressBookMapper.list(addressBook);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     */
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }
    /**
     * 根据id删除地址
     *
     * @param id
     */
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public AddressBook getById(Long id) {
        AddressBook addressBook = addressBookMapper.getById(id);
        return addressBook;
    }

    @Override
    public void setDefault(AddressBook addressBook) {

        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        //将当前用户所有地址设置为非默认地址
        addressBookMapper.updateDefaultByUserId(addressBook);
        //再设置当前地址为默认地址
        addressBook.setIsDefault(1);
        //上面需要动态获取当前用户id修改，这里通过这条地址簿记录id修改
        addressBookMapper.update(addressBook);
    }

}
