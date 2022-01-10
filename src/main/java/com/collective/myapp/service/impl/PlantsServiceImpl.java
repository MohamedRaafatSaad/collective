package com.collective.myapp.service.impl;

import com.collective.myapp.domain.Plants;
import com.collective.myapp.repository.PlantsRepository;
import com.collective.myapp.service.PlantsService;
import com.collective.myapp.service.dto.PlantsDTO;
import com.collective.myapp.service.mapper.PlantsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Plants}.
 */
@Service
@Transactional
public class PlantsServiceImpl implements PlantsService {

	private final Logger log = LoggerFactory.getLogger(PlantsServiceImpl.class);

	private final PlantsRepository plantsRepository;

	private final PlantsMapper plantsMapper;

	@Autowired
	ObjectMapper objectmapper;

	public PlantsServiceImpl(PlantsRepository plantsRepository, PlantsMapper plantsMapper) {
		this.plantsRepository = plantsRepository;
		this.plantsMapper = plantsMapper;
	}

	@Override
	public PlantsDTO save(PlantsDTO plantsDTO) {
		log.debug("Request to save Plants : {}", plantsDTO);
		Plants plants = plantsMapper.toEntity(plantsDTO);
		plants = plantsRepository.save(plants);
		return plantsMapper.toDto(plants);
	}

	@Override
	public Optional<PlantsDTO> partialUpdate(PlantsDTO plantsDTO) {
		log.debug("Request to partially update Plants : {}", plantsDTO);

		return plantsRepository.findById(plantsDTO.getId()).map(existingPlants -> {
			plantsMapper.partialUpdate(existingPlants, plantsDTO);

			return existingPlants;
		}).map(plantsRepository::save).map(plantsMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PlantsDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Plants");
		return plantsRepository.findAll(pageable).map(plantsMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<PlantsDTO> findOne(Long id) {
		log.debug("Request to get Plants : {}", id);
		return plantsRepository.findById(id).map(plantsMapper::toDto);
	}

	@Override
	public void delete(Long id) {
		log.debug("Request to delete Plants : {}", id);
		plantsRepository.deleteById(id);
	}

	@Override
	public Page<PlantsDTO> findAllTopAsc(Pageable pageable) {
		return plantsRepository.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id")))
				.map(plantsMapper::toDto);
	}

	@Override
	public Page<PlantsDTO> findAllTopDesc(Pageable pageable) {
		Page<PlantsDTO> page = null;
		page = plantsRepository.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id")))
				.map(plantsMapper::toDto);
		List<PlantsDTO> rev = new ArrayList<PlantsDTO>(page.getContent());
		Collections.reverse(rev);
		page = new PageImpl<PlantsDTO>(rev);
		return page;
	}

	@Override
	public Page<PlantsDTO> findAllBottomDesc(Pageable pageable) {
		return plantsRepository.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id")))
				.map(plantsMapper::toDto);
	}

	@Override
	public Page<PlantsDTO> findAllBottomAsc(Pageable pageable) {
		Page<PlantsDTO> page = null;
		page = plantsRepository.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id")))
				.map(plantsMapper::toDto);
		List<PlantsDTO> rev = new ArrayList<PlantsDTO>(page.getContent());
		Collections.reverse(rev);
		page = new PageImpl<PlantsDTO>(rev);
		return page;
	}

	@Override
	public ObjectNode getPercAndCount(String pname) {
		ObjectNode object = objectmapper.createObjectNode();
		double fedCount = plantsRepository.countByName(pname);
		double total = plantsRepository.count();
		object.put("actualValue", fedCount);
		object.put("Percentage-%", ((fedCount/total)*100));
		return object;
	}
	
	
	@Override
	public Page<PlantsDTO> getPlantsFilter(Specification<Plants> spec, Pageable pageable) {
		List<PlantsDTO> plantsModel;
		plantsModel = plantsRepository.findAll(spec,pageable).map(plantsMapper::toDto).stream()
                .collect(Collectors.toList());
			return new PageImpl<>(plantsModel, pageable, plantsModel.size());
	}

	@Override
	public Long getPlantsFilterCount(Specification<Plants> spec) {
		return plantsRepository.count(spec);
	}
}
