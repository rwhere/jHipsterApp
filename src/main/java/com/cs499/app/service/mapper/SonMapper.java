package com.cs499.app.service.mapper;

import com.cs499.app.domain.*;
import com.cs499.app.service.dto.SonDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Son and its DTO SonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SonMapper {

    @Mapping(source = "father.id", target = "fatherId")
    SonDTO sonToSonDTO(Son son);

    List<SonDTO> sonsToSonDTOs(List<Son> sons);

    @Mapping(source = "fatherId", target = "father")
    Son sonDTOToSon(SonDTO sonDTO);

    List<Son> sonDTOsToSons(List<SonDTO> sonDTOs);

    default Father fatherFromId(Long id) {
        if (id == null) {
            return null;
        }
        Father father = new Father();
        father.setId(id);
        return father;
    }
}
