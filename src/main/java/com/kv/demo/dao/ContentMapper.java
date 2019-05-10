package com.kv.demo.dao;

import com.kv.demo.entity.Content;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentMapper {
    Integer insert(Content content);
}
