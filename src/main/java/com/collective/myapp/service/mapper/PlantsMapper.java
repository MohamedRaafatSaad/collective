package com.collective.myapp.service.mapper;

import com.collective.myapp.domain.*;
import com.collective.myapp.service.dto.PlantsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plants} and its DTO {@link PlantsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlantsMapper extends EntityMapper<PlantsDTO, Plants> {}
