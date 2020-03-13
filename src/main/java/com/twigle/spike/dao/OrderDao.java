package com.twigle.spike.dao;

import com.twigle.spike.model.Orders;
import com.twigle.spike.model.SpikeOrders;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {
    @Insert("insert into orders (user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date) " +
            "values (#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    //keyProperty Orders   keyColumn sql  select last_insert_id() return autoincrement id
    public long insertOrder(Orders orders);

    @Insert("insert into spike_orders (user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{orderId})")
    public long insertSpikeOrder(SpikeOrders spikeOrders);

    @Select("select * from spike_orders where user_id = #{userId} and goods_id = #{goodsId}")
    public SpikeOrders getSpikeOrderByUserIDGoodsID(@Param("userId") long userId, @Param("goodsId") long goodsId);
}
