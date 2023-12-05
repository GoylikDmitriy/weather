package com.goylik.weather.model.mapper;

import com.goylik.weather.model.entity.BaseEntity;
import com.goylik.weather.model.mapper.exception.MappingException;

public interface Mapper<E extends BaseEntity, Dto> {
    Dto map(E e) throws MappingException;
    E map(Dto dto) throws MappingException;
}