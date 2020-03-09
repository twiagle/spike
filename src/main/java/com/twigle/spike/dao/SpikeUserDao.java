package com.twigle.spike.dao;

import com.twigle.spike.model.SpikeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SpikeUserDao {
    final String TABLE_NAME = "spike_user";

    @Select("select * from " + TABLE_NAME + " where id = #{id}")
    public SpikeUser getByID(@Param("id") long id);
}
