package com.goylik.weather.model.mapper;

import com.goylik.weather.model.entity.BaseEntity;

/**
 * Generic interface for mapping between entity and DTO objects.
 *
 * @param <E>   the entity type extending BaseEntity.
 * @param <Dto> the DTO type.
 */
public interface Mapper<E extends BaseEntity, Dto> {
    /**
     * Maps an entity to a DTO object.
     *
     * @param e the entity to be mapped.
     * @return a DTO object mapped from the entity.
     */
    Dto map(E e);

    /**
     * Maps a DTO object to an entity.
     *
     * @param dto the DTO object to be mapped.
     * @return an entity mapped from the DTO object.
     */
    E map(Dto dto);
}