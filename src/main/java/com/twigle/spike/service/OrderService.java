package com.twigle.spike.service;

import com.twigle.spike.dao.OrderDao;
import com.twigle.spike.model.Orders;
import com.twigle.spike.model.SpikeOrders;
import com.twigle.spike.model.SpikeUser;
import com.twigle.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    public SpikeOrders getSpikeOrderByUserIDGoodsID(long userId, long goodsId) {
        return orderDao.getSpikeOrderByUserIDGoodsID(userId, goodsId);
    }

    @Transactional
    public Orders createOrder(SpikeUser user, GoodsVo goods) {
        Orders order = new Orders();
        //make order
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goods.getSpikePrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderDao.insertOrder(order);//insert into orders, order will get ID from sql selectKey
        //make spike order
        SpikeOrders spikeOrder = new SpikeOrders();
        spikeOrder.setGoodsId(goods.getId());
        spikeOrder.setOrderId(order.getId());
        spikeOrder.setUserId(user.getId());
        orderDao.insertSpikeOrder(spikeOrder);//insert into spikeOrder
        return order;
    }
}
//        private Long id;
//        private Long userId;
//        private Long goodsId;
//        private Long  deliveryAddrId;
//        private String goodsName;
//        private Integer goodsCount;
//        private Double goodsPrice;
//        private Integer orderChannel;
//        private Integer status;
//        private Date createDate;
//        private Date payDate;