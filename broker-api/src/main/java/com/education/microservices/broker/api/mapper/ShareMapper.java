package com.education.microservices.broker.api.mapper;

import com.education.microservices.broker.api.dto.ShareDto;
import com.education.microservices.broker.api.model.ShareEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShareMapper {

    ShareDto toDto(ShareEntity share);

    ShareEntity toEntity(ShareDto shareDto);

    List<ShareDto> toListDto(List<ShareEntity> shares);
}
