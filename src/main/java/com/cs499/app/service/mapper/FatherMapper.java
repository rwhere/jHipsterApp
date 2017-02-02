package com.cs499.app.service.mapper;

import com.cs499.app.domain.*;
import com.cs499.app.service.dto.FatherDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Father and its DTO FatherDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FatherMapper {

    FatherDTO fatherToFatherDTO(Father father);

    List<FatherDTO> fathersToFatherDTOs(List<Father> fathers);

    @Mapping(target = "sons", ignore = true)
    @Mapping(target = "daughters", ignore = true)
    Father fatherDTOToFather(FatherDTO fatherDTO);

    List<Father> fatherDTOsToFathers(List<FatherDTO> fatherDTOs);
}
