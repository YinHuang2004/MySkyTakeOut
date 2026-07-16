package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;



    //可以直接更新，如果后续有操作的话还是先查询保存再更新

    /**
     * 处理支付超时订单
     */
    @Scheduled(cron="0 * * * * ?")//在第0秒，分时日月单位都是每一x，星期不指定：合起来就是每天每小时每分钟的第0秒执行一次
    public void processTimeOutOrder(){
        log.info("处理超时订单:{}",new Date());
        //plusminutes是在此时间加多少minutes，如果是负数就是减去多少minutes
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orderList = orderMapper.getByStatusAndOrdertimeLT(Orders.PENDING_PAYMENT,localDateTime);
        List<Long>ordersIdList=orderList.stream().map(orders->orders.getId()).collect(Collectors.toList());
       if(ordersIdList!=null&&!ordersIdList.isEmpty()){
           orderMapper.batchCancelByCondition(Orders.CANCELLED,"支付超时，订单取消",LocalDateTime.now(),ordersIdList);
       }
    }
    /**
     * 处理派送中状态的订单
     *
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("处理派送中状态的订单:{}",new Date());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-60);//12点前的都算送到
        List<Orders> orderList = orderMapper.getByStatusAndOrdertimeLT(Orders.DELIVERY_IN_PROGRESS, localDateTime);
        if(orderList!=null&&!orderList.isEmpty()){
            for (Orders orders : orderList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
