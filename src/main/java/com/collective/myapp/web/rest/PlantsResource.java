package com.collective.myapp.web.rest;

import com.collective.myapp.domain.Plants;
import com.collective.myapp.repository.PlantsRepository;
import com.collective.myapp.service.PlantsService;
import com.collective.myapp.service.dto.PlantsDTO;
import com.collective.myapp.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sipios.springsearch.anotation.SearchSpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.collective.myapp.domain.Plants}.
 */
@RestController
@RequestMapping("/api")
public class PlantsResource {

	private final Logger log = LoggerFactory.getLogger(PlantsResource.class);

	private static final String ENTITY_NAME = "plants";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final PlantsService plantsService;

	private final PlantsRepository plantsRepository;

	public PlantsResource(PlantsService plantsService, PlantsRepository plantsRepository) {
		this.plantsService = plantsService;
		this.plantsRepository = plantsRepository;
	}

	/**
	 * {@code POST  /plants} : Create a new plants.
	 *
	 * @param plantsDTO the plantsDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new plantsDTO, or with status {@code 400 (Bad Request)} if
	 *         the plants has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/plants")
	public ResponseEntity<PlantsDTO> createPlants(@Valid @RequestBody PlantsDTO plantsDTO) throws URISyntaxException {
		log.debug("REST request to save Plants : {}", plantsDTO);
		if (plantsDTO.getId() != null) {
			throw new BadRequestAlertException("A new plants cannot already have an ID", ENTITY_NAME, "idexists");
		}
		PlantsDTO result = plantsService.save(plantsDTO);
		return ResponseEntity
				.created(new URI("/api/plants/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /plants/:id} : Updates an existing plants.
	 *
	 * @param id        the id of the plantsDTO to save.
	 * @param plantsDTO the plantsDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated plantsDTO, or with status {@code 400 (Bad Request)} if
	 *         the plantsDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the plantsDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/plants/{id}")
	public ResponseEntity<PlantsDTO> updatePlants(@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody PlantsDTO plantsDTO) throws URISyntaxException {
		log.debug("REST request to update Plants : {}, {}", id, plantsDTO);
		if (plantsDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, plantsDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!plantsRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		PlantsDTO result = plantsService.save(plantsDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plantsDTO.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /plants/:id} : Partial updates given fields of an existing
	 * plants, field will ignore if it is null
	 *
	 * @param id        the id of the plantsDTO to save.
	 * @param plantsDTO the plantsDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated plantsDTO, or with status {@code 400 (Bad Request)} if
	 *         the plantsDTO is not valid, or with status {@code 404 (Not Found)} if
	 *         the plantsDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the plantsDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/plants/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<PlantsDTO> partialUpdatePlants(@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody PlantsDTO plantsDTO) throws URISyntaxException {
		log.debug("REST request to partial update Plants partially : {}, {}", id, plantsDTO);
		if (plantsDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, plantsDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!plantsRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<PlantsDTO> result = plantsService.partialUpdate(plantsDTO);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plantsDTO.getId().toString()));
	}

	/**
	 * {@code GET  /plants} : get all the plants.
	 *
	 * @param pageable the pagination information.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of plants in body.
	 */
	@GetMapping("/plants")
	public ResponseEntity<List<PlantsDTO>> getAllPlants(Pageable pageable) {
		log.debug("REST request to get a page of Plants");
		Page<PlantsDTO> page = plantsService.findAll(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /plants/:id} : get the "id" plants.
	 *
	 * @param id the id of the plantsDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the plantsDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/plants/{id}")
	public ResponseEntity<PlantsDTO> getPlants(@PathVariable Long id) {
		log.debug("REST request to get Plants : {}", id);
		Optional<PlantsDTO> plantsDTO = plantsService.findOne(id);
		return ResponseUtil.wrapOrNotFound(plantsDTO);
	}

	/**
	 * {@code DELETE  /plants/:id} : delete the "id" plants.
	 *
	 * @param id the id of the plantsDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/plants/{id}")
	public ResponseEntity<Void> deletePlants(@PathVariable Long id) {
		log.debug("REST request to delete Plants : {}", id);
		plantsService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}

	@GetMapping("/plants/topASC")
	public ResponseEntity<List<PlantsDTO>> getTopNplantsASC(Pageable pageable) {
			Page<PlantsDTO> page = plantsService.findAllTopAsc(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}
	
	@GetMapping("/plants/topDESC")
	public ResponseEntity<List<PlantsDTO>> getTopNplantsDESC(Pageable pageable) {
			Page<PlantsDTO> page = plantsService.findAllTopDesc(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}
	
	@GetMapping("/plants/bottomDESC")
	public ResponseEntity<List<PlantsDTO>> getBottomNplantsDESC(Pageable pageable) {
			Page<PlantsDTO> page = plantsService.findAllBottomDesc(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}
	
	@GetMapping("/plants/bottomASC")
	public ResponseEntity<List<PlantsDTO>> getBottomNplantsASC(Pageable pageable) {
			Page<PlantsDTO> page = plantsService.findAllBottomAsc(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}
	
	@GetMapping("/plants/getPercAndCount/{pname}")
	public ResponseEntity<?> getPercAndCount(@PathVariable String pname) {
			ObjectNode object =  plantsService.getPercAndCount(pname);
		return ResponseEntity.ok().body(object);
	}
	
    @GetMapping("/plants/filter")
    public ResponseEntity<List<PlantsDTO>> searchProjects(@SearchSpec Specification<Plants> specs,Pageable pageable) {
        Page<PlantsDTO> page = plantsService.getPlantsFilter(specs, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    @GetMapping("/filterAndCount")
    public ResponseEntity<Long> searchPlantsCount(@SearchSpec Specification<Plants> specs) {
        Long count = plantsService.getPlantsFilterCount(specs);
        return ResponseEntity.ok().body(count);
    }

}
