package com.twigle.spike.service;

import com.twigle.spike.dao.OrderDao;
import com.twigle.spike.domain.Orders;
import com.twigle.spike.domain.SpikeOrders;
import com.twigle.spike.domain.SpikeUser;
import com.twigle.spike.redis.OrderPrefix;
import com.twigle.spike.redis.RedisService;
import com.twigle.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisService redisService;

    public SpikeOrders getSpikeOrderByUserIDGoodsID(long userId, long goodsId) {
        SpikeOrders spikeOrders = redisService.get(OrderPrefix.getSpikeOrderByUidGid,userId + "_" + goodsId, SpikeOrders.class);
        if(spikeOrders!=null)
            return spikeOrders;
        spikeOrders = orderDao.getSpikeOrderByUserIDGoodsID(userId, goodsId);
        if (spikeOrders != null)
            redisService.set(OrderPrefix.getSpikeOrderByUidGid, userId + "_" + goodsId, spikeOrders);
        return spikeOrders;
    }

    public Orders getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
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
        order.setCreateDate(LocalDateTime.now());
        orderDao.insertOrder(order);//insert into orders, order will get ID from sql selectKey
        //make spike order
        SpikeOrders spikeOrder = new SpikeOrders();
        spikeOrder.setGoodsId(goods.getId());
        spikeOrder.setOrderId(order.getId());
        spikeOrder.setUserId(user.getId());
        orderDao.insertSpikeOrder(spikeOrder);//insert into spikeOrder
        redisService.set(OrderPrefix.getSpikeOrderByUidGid, user.getId() + "_" + goods.getId(), spikeOrder);
        return order;
    }

    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteSpikeOrders();
    }
}
