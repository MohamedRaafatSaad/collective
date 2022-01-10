package com.collective.myapp.service;

import com.collective.myapp.domain.Plants;
import com.collective.myapp.service.dto.PlantsDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Service Interface for managing {@link com.collective.myapp.domain.Plants}.
 */
public interface PlantsService {
    /**
     * Save a plants.
     *
     * @param plantsDTO the entity to save.
     * @return the persisted entity.
     */
    PlantsDTO save(PlantsDTO plantsDTO);

    /**
     * Partially updates a plants.
     *
     * @param plantsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlantsDTO> partialUpdate(PlantsDTO plantsDTO);

    /**
     * Get all the plants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlantsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" plants.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlantsDTO> findOne(Long id);

    /**
     * Delete the "id" plants.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    
    
    Page<PlantsDTO> findAllTopAsc(Pageable pageable);
    
    Page<PlantsDTO> findAllTopDesc(Pageable pageable);
 
    Page<PlantsDTO> findAllBottomDesc(Pageable pageable);

    Page<PlantsDTO> findAllBottomAsc(Pageable pageable);

    ObjectNode getPercAndCount(String pname);
    
    Page<PlantsDTO> getPlantsFilter(Specification<Plants> spec, Pageable pageable);
    
    Long getPlantsFilterCount(Specification<Plants> spec);
}
