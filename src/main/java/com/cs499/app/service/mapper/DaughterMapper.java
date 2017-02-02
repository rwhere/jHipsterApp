package com.cs499.app.service.mapper;

import com.cs499.app.domain.*;
import com.cs499.app.service.dto.DaughterDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Daughter and its DTO DaughterDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DaughterMapper {

    @Mapping(source = "father.id", target = "fatherId")
    DaughterDTO daughterToDaughterDTO(Daughter daughter);

    List<DaughterDTO> daughtersToDaughterDTOs(List<Daughter> daughters);

    @Mapping(source = "fatherId", target = "father")
    Daughter daughterDTOToDaughter(DaughterDTO daughterDTO);

    List<Daughter> daughterDTOsToDaughters(List<DaughterDTO> daughterDTOs);

    default Father fatherFromId(Long id) {
        if (id == null) {
            return null;
        }
        Father father = new Father();
        father.setId(id);
        return father;
    }
}
