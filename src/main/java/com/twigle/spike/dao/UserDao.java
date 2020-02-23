package com.twigle.spike.dao;

import com.twigle.spike.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    final String TABLE_NAME = " user ";
    final String INSERT_FIELDs = " id,name ";
    @Select("select * from " +TABLE_NAME+ " where id = #{id}")
    public User getById(@Param("id") int id);

    @Insert("insert into " + TABLE_NAME + "(" + INSERT_FIELDs + ")" + " values(#{id},#{name})")
    public int insert(User user);
}
