package com.goylik.weather.model.mapper;

import com.goylik.weather.model.entity.BaseEntity;

public interface Mapper<E extends BaseEntity, Dto> {
    Dto map(E e);
    E map(Dto dto);
}
