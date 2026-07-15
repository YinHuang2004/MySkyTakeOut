package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.properties.BaiduProperties;
import com.sky.properties.ShopProperties;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private ShopProperties shopAddress;
    @Autowired
    private BaiduProperties baiduProperties;

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        //开启分页
       PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
       //条件查询返回entity对象
       Page<Orders>page=orderMapper.pageQuery(ordersPageQueryDTO);
       //将entity对象封装为vo对象（包含订单详情信息）
        List<OrderVO>orderVOList=getOrderVOList(page);
        return new PageResult(page.getTotal(),orderVOList);

    }

    /**
     * 将传入的order封装为视图对象返回,
     * @param page
     * @return
     */
    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        //需要返回的订单菜品信息(每个订单都有对应的订单详情集合）
        List<OrderVO>orderVOList=new ArrayList<>();
        //获取传入的orders对象集合
        List<Orders> ordersList = page.getResult();
        for (Orders orders : ordersList) {
            OrderVO orderVO=new OrderVO();
            //将共同属性copy
            BeanUtils.copyProperties(orders,orderVO);
            //获取订单详情字符串（因为我们只需要name，所以使用字符串速度快而不是返回订单详情集合
            String orderDishStr=getOrderDishStr(orders);
            orderVO.setOrderDishes(orderDishStr);
            ordersList.add(orderVO);
        }
        return orderVOList;
    }

    private String getOrderDishStr(Orders orders) {
        //查询订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        StringBuilder sb=new StringBuilder();
        //将每一条订单详情转为字符串
        for (OrderDetail orderDetail : orderDetailList) {
            sb.append(orderDetail.getName()).append("*").append(orderDetail.getNumber()).append(";");
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //处理业务异常(比如说地址簿为空，购物车数据为空
        //根据前端传入的地址簿id去查询该地址是否存在
        AddressBook addressBook=addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook==null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //查询当前用户购物车数据
        ShoppingCart shoppingCart=ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if(shoppingCartList==null||shoppingCartList.isEmpty()){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //插入订单数据(需要会显主键）
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,order);
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));//通过valueof将其他数据类型转为string
        order.setUserId(BaseContext.getCurrentId());
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());
        order.setAddress(addressBook.getDetail());
        //向订单表插入1条数据
        orderMapper.insert(order);

        //插入订单明细数据

        //对于购物车每条记录都生成一条订单明细并需要设置回显的订单id
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);
        //清空购物车
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
        return OrderSubmitVO.builder()
                                    .id(order.getId())
                                    .orderTime(order.getOrderTime())
                                    .orderAmount(order.getAmount())
                                    .orderNumber(order.getNumber())
                                    .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        // 当前登录用户id
//        Long userId = BaseContext.getCurrentId();
//        User user = userMapper.getById(userId);
//
//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
//
//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        vo.setPackageStr(jsonObject.getString("package"));
//
//        return vo;
        log.info("跳过微信支付，支付成功");
        paySuccess(ordersPaymentDTO.getOrderNumber());
        return new OrderPaymentVO();
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        //首先获取当前操作人id标识是哪个用户查询历史订单
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        //返回视图对象
        List<OrderVO>orderVOList=new ArrayList<>();
        //设置分页
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        //查询当前用户所有历史订单
        Page<Orders> page=orderMapper.pageQuery(ordersPageQueryDTO);
        if(page!=null&&!page.isEmpty()){
            //对每个订单分别查询对应订单明细
            for (Orders orders : page) {
                //封装每个视图对象加入集合
                OrderVO orderVO=new OrderVO();
                Long ordersId = orders.getId();
                BeanUtils.copyProperties(orders,orderVO);
                List<OrderDetail>orderDetailList=orderDetailMapper.getByOrderId(ordersId);
                orderVO.setOrderDetailList(orderDetailList);
                orderVOList.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(),orderVOList);
    }

    @Override
    public OrderVO details(Long id) {
        Orders orders=orderMapper.getById(id);
        Long orderId=orders.getId();
        //根据订单id查询对应订单明细
        List<OrderDetail> orderDetailList=orderDetailMapper.getByOrderId(orderId);
        OrderVO orderVO=new OrderVO();
        BeanUtils.copyProperties(orders,orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 用户取消订单
     *
     * @param id
     */
    public void cancel(Long id) throws Exception {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 订单处于待接单状态下取消，需要进行退款
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //调用微信支付退款接口
            weChatPayUtil.refund(
                    ordersDB.getNumber(), //商户订单号
                    ordersDB.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额

            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }
    //再来一单：根据订单id将该已完成的订单菜品/套餐重新放上购物车
    public void repetition(Long id){
        Long currentId = BaseContext.getCurrentId();
        //根据订单id查询该订单详情
        List<OrderDetail>orderDetailList=orderMapper.getByOrderId(id);
        //将订单详情对象转为购物车对象
        List<ShoppingCart>shoppingCartList=orderDetailList.stream().map(new Function<OrderDetail, ShoppingCart>() {
            @Override
            public ShoppingCart apply(OrderDetail orderDetail) {
                //思考如何将订单详情对象转为购物车对象
                //购物车的每条记录就是一个菜品/套餐
                ShoppingCart shoppingCart=ShoppingCart.builder().userId(currentId).createTime(LocalDateTime.now()).build();
                BeanUtils.copyProperties(orderDetail,shoppingCart,"id");

               return shoppingCart;
            }
        }).collect(Collectors.toList());
        //往数据库批量插入购物车
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 各个状态的订单数量统计
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
        // 根据状态，分别查询出待接单、待派送、派送中的订单数量
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);
        return OrderStatisticsVO.builder()
                .toBeConfirmed(toBeConfirmed)
                .confirmed(confirmed)
                .deliveryInProgress(deliveryInProgress).build();

    }

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();

        orderMapper.update(orders);
    }
    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     */
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        // 根据id查询订单
        Orders orders = orderMapper.getById(ordersRejectionDTO.getId());

        // 订单只有存在且状态为2（待接单）才可以拒单
        if (orders == null || !orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //支付状态
        Integer payStatus = orders.getPayStatus();
        if (payStatus == Orders.PAID) {
            //用户已支付，需要退款
            String refund = weChatPayUtil.refund(
                    orders.getNumber(),
                    orders.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }

        // 拒单需要退款，根据订单id更新订单状态、拒单原因、取消时间
        orders= Orders.builder()
                .id(orders.getId())
                .status(Orders.CANCELLED)
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .cancelTime(LocalDateTime.now()).build();

        orderMapper.update(orders);
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     */
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // 根据id查询订单
        Orders orders = orderMapper.getById(ordersCancelDTO.getId());

        //支付状态
        Integer payStatus = orders.getPayStatus();
        if (payStatus == 1) {
            //用户已支付，需要退款
            String refund = weChatPayUtil.refund(
                    orders.getNumber(),
                    orders.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }

        // 管理端取消订单需要退款，根据订单id更新订单状态、取消原因、取消时间
       orders =Orders
               .builder()
               .id(orders.getId())
               .status(Orders.CANCELLED)
               .cancelReason(ordersCancelDTO.getCancelReason())
               .cancelTime(LocalDateTime.now()).build();

        orderMapper.update(orders);
    }

    /**
     * 派送订单
     *
     * @param id
     */
    public void delivery(Long id) {
        // 根据id查询订单
        Orders orders = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为3
        if (orders == null || !orders.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 更新订单状态,状态转为派送中
       orders =Orders.builder().id(orders.getId()).status(Orders.DELIVERY_IN_PROGRESS).build();

        orderMapper.update(orders);
    }

    @Override
    public void complete(Long id) {
        Orders orders=orderMapper.getById(id);
        if(orders==null||!orders.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //更新订单状态为已完成
        orders=Orders.builder().id(orders.getId()).status(Orders.COMPLETED).build();
        orderMapper.update(orders);
    }

    /**
     * 检查客户的收货地址是否超出配送范围
     * @param address
     */
    private void checkOutOfRange(String address) {
        Map map = new HashMap();
        map.put("address",shopAddress.getAddress());
        map.put("output","json");
        map.put("ak",baiduProperties.getAk());

        //获取店铺的经纬度坐标
        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("店铺地址解析失败");
        }

        //数据解析
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lat = location.getString("lat");
        String lng = location.getString("lng");
        //店铺经纬度坐标
        String shopLngLat = lat + "," + lng;

        map.put("address",address);
        //获取用户收货地址的经纬度坐标
        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        jsonObject = JSON.parseObject(userCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("收货地址解析失败");
        }

        //数据解析
        location = jsonObject.getJSONObject("result").getJSONObject("location");
        lat = location.getString("lat");
        lng = location.getString("lng");
        //用户收货地址经纬度坐标
        String userLngLat = lat + "," + lng;

        map.put("origin",shopLngLat);
        map.put("destination",userLngLat);
        map.put("steps_info","0");

        //路线规划
        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving", map);

        jsonObject = JSON.parseObject(json);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("配送路线规划失败");
        }

        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");

        if(distance > 5000){
            //配送距离超过5000米
            throw new OrderBusinessException("超出配送范围");
        }
    }
}
