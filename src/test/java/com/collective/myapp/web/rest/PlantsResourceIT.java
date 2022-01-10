package com.collective.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.collective.myapp.IntegrationTest;
import com.collective.myapp.domain.Plants;
import com.collective.myapp.repository.PlantsRepository;
import com.collective.myapp.service.dto.PlantsDTO;
import com.collective.myapp.service.mapper.PlantsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlantsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlantsResourceIT {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String DEFAULT_PSTATABB = "AAAAAAAAAA";
    private static final String UPDATED_PSTATABB = "BBBBBBBBBB";

    private static final String DEFAULT_PNAME = "AAAAAAAAAA";
    private static final String UPDATED_PNAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENID = "AAAAAAAAAA";
    private static final String UPDATED_GENID = "BBBBBBBBBB";

    private static final String DEFAULT_GENSTAT = "AAAAAAAAAA";
    private static final String UPDATED_GENSTAT = "BBBBBBBBBB";

    private static final Integer DEFAULT_GENNTAN = 1;
    private static final Integer UPDATED_GENNTAN = 2;

    private static final String ENTITY_API_URL = "/api/plants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlantsRepository plantsRepository;

    @Autowired
    private PlantsMapper plantsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlantsMockMvc;

    private Plants plants;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plants createEntity(EntityManager em) {
        Plants plants = new Plants()
            .year(DEFAULT_YEAR)
            .pstatabb(DEFAULT_PSTATABB)
            .pname(DEFAULT_PNAME)
            .genid(DEFAULT_GENID)
            .genstat(DEFAULT_GENSTAT)
            .genntan(DEFAULT_GENNTAN);
        return plants;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plants createUpdatedEntity(EntityManager em) {
        Plants plants = new Plants()
            .year(UPDATED_YEAR)
            .pstatabb(UPDATED_PSTATABB)
            .pname(UPDATED_PNAME)
            .genid(UPDATED_GENID)
            .genstat(UPDATED_GENSTAT)
            .genntan(UPDATED_GENNTAN);
        return plants;
    }

    @BeforeEach
    public void initTest() {
        plants = createEntity(em);
    }

    @Test
    @Transactional
    void createPlants() throws Exception {
        int databaseSizeBeforeCreate = plantsRepository.findAll().size();
        // Create the Plants
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);
        restPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantsDTO)))
            .andExpect(status().isCreated());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeCreate + 1);
        Plants testPlants = plantsList.get(plantsList.size() - 1);
        assertThat(testPlants.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPlants.getPstatabb()).isEqualTo(DEFAULT_PSTATABB);
        assertThat(testPlants.getPname()).isEqualTo(DEFAULT_PNAME);
        assertThat(testPlants.getGenid()).isEqualTo(DEFAULT_GENID);
        assertThat(testPlants.getGenstat()).isEqualTo(DEFAULT_GENSTAT);
        assertThat(testPlants.getGenntan()).isEqualTo(DEFAULT_GENNTAN);
    }

    @Test
    @Transactional
    void createPlantsWithExistingId() throws Exception {
        // Create the Plants with an existing ID
        plants.setId(1L);
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        int databaseSizeBeforeCreate = plantsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantsRepository.findAll().size();
        // set the field null
        plants.setYear(null);

        // Create the Plants, which fails.
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        restPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantsDTO)))
            .andExpect(status().isBadRequest());

        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPstatabbIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantsRepository.findAll().size();
        // set the field null
        plants.setPstatabb(null);

        // Create the Plants, which fails.
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        restPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantsDTO)))
            .andExpect(status().isBadRequest());

        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantsRepository.findAll().size();
        // set the field null
        plants.setPname(null);

        // Create the Plants, which fails.
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        restPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantsDTO)))
            .andExpect(status().isBadRequest());

        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenidIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantsRepository.findAll().size();
        // set the field null
        plants.setGenid(null);

        // Create the Plants, which fails.
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        restPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantsDTO)))
            .andExpect(status().isBadRequest());

        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenstatIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantsRepository.findAll().size();
        // set the field null
        plants.setGenstat(null);

        // Create the Plants, which fails.
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        restPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantsDTO)))
            .andExpect(status().isBadRequest());

        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlants() throws Exception {
        // Initialize the database
        plantsRepository.saveAndFlush(plants);

        // Get all the plantsList
        restPlantsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plants.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].pstatabb").value(hasItem(DEFAULT_PSTATABB)))
            .andExpect(jsonPath("$.[*].pname").value(hasItem(DEFAULT_PNAME)))
            .andExpect(jsonPath("$.[*].genid").value(hasItem(DEFAULT_GENID)))
            .andExpect(jsonPath("$.[*].genstat").value(hasItem(DEFAULT_GENSTAT)))
            .andExpect(jsonPath("$.[*].genntan").value(hasItem(DEFAULT_GENNTAN)));
    }

    @Test
    @Transactional
    void getPlants() throws Exception {
        // Initialize the database
        plantsRepository.saveAndFlush(plants);

        // Get the plants
        restPlantsMockMvc
            .perform(get(ENTITY_API_URL_ID, plants.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plants.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.pstatabb").value(DEFAULT_PSTATABB))
            .andExpect(jsonPath("$.pname").value(DEFAULT_PNAME))
            .andExpect(jsonPath("$.genid").value(DEFAULT_GENID))
            .andExpect(jsonPath("$.genstat").value(DEFAULT_GENSTAT))
            .andExpect(jsonPath("$.genntan").value(DEFAULT_GENNTAN));
    }

    @Test
    @Transactional
    void getNonExistingPlants() throws Exception {
        // Get the plants
        restPlantsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlants() throws Exception {
        // Initialize the database
        plantsRepository.saveAndFlush(plants);

        int databaseSizeBeforeUpdate = plantsRepository.findAll().size();

        // Update the plants
        Plants updatedPlants = plantsRepository.findById(plants.getId()).get();
        // Disconnect from session so that the updates on updatedPlants are not directly saved in db
        em.detach(updatedPlants);
        updatedPlants
            .year(UPDATED_YEAR)
            .pstatabb(UPDATED_PSTATABB)
            .pname(UPDATED_PNAME)
            .genid(UPDATED_GENID)
            .genstat(UPDATED_GENSTAT)
            .genntan(UPDATED_GENNTAN);
        PlantsDTO plantsDTO = plantsMapper.toDto(updatedPlants);

        restPlantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plantsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeUpdate);
        Plants testPlants = plantsList.get(plantsList.size() - 1);
        assertThat(testPlants.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPlants.getPstatabb()).isEqualTo(UPDATED_PSTATABB);
        assertThat(testPlants.getPname()).isEqualTo(UPDATED_PNAME);
        assertThat(testPlants.getGenid()).isEqualTo(UPDATED_GENID);
        assertThat(testPlants.getGenstat()).isEqualTo(UPDATED_GENSTAT);
        assertThat(testPlants.getGenntan()).isEqualTo(UPDATED_GENNTAN);
    }

    @Test
    @Transactional
    void putNonExistingPlants() throws Exception {
        int databaseSizeBeforeUpdate = plantsRepository.findAll().size();
        plants.setId(count.incrementAndGet());

        // Create the Plants
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plantsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlants() throws Exception {
        int databaseSizeBeforeUpdate = plantsRepository.findAll().size();
        plants.setId(count.incrementAndGet());

        // Create the Plants
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlants() throws Exception {
        int databaseSizeBeforeUpdate = plantsRepository.findAll().size();
        plants.setId(count.incrementAndGet());

        // Create the Plants
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlantsWithPatch() throws Exception {
        // Initialize the database
        plantsRepository.saveAndFlush(plants);

        int databaseSizeBeforeUpdate = plantsRepository.findAll().size();

        // Update the plants using partial update
        Plants partialUpdatedPlants = new Plants();
        partialUpdatedPlants.setId(plants.getId());

        partialUpdatedPlants.year(UPDATED_YEAR).pname(UPDATED_PNAME).genid(UPDATED_GENID);

        restPlantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlants))
            )
            .andExpect(status().isOk());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeUpdate);
        Plants testPlants = plantsList.get(plantsList.size() - 1);
        assertThat(testPlants.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPlants.getPstatabb()).isEqualTo(DEFAULT_PSTATABB);
        assertThat(testPlants.getPname()).isEqualTo(UPDATED_PNAME);
        assertThat(testPlants.getGenid()).isEqualTo(UPDATED_GENID);
        assertThat(testPlants.getGenstat()).isEqualTo(DEFAULT_GENSTAT);
        assertThat(testPlants.getGenntan()).isEqualTo(DEFAULT_GENNTAN);
    }

    @Test
    @Transactional
    void fullUpdatePlantsWithPatch() throws Exception {
        // Initialize the database
        plantsRepository.saveAndFlush(plants);

        int databaseSizeBeforeUpdate = plantsRepository.findAll().size();

        // Update the plants using partial update
        Plants partialUpdatedPlants = new Plants();
        partialUpdatedPlants.setId(plants.getId());

        partialUpdatedPlants
            .year(UPDATED_YEAR)
            .pstatabb(UPDATED_PSTATABB)
            .pname(UPDATED_PNAME)
            .genid(UPDATED_GENID)
            .genstat(UPDATED_GENSTAT)
            .genntan(UPDATED_GENNTAN);

        restPlantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlants))
            )
            .andExpect(status().isOk());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeUpdate);
        Plants testPlants = plantsList.get(plantsList.size() - 1);
        assertThat(testPlants.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPlants.getPstatabb()).isEqualTo(UPDATED_PSTATABB);
        assertThat(testPlants.getPname()).isEqualTo(UPDATED_PNAME);
        assertThat(testPlants.getGenid()).isEqualTo(UPDATED_GENID);
        assertThat(testPlants.getGenstat()).isEqualTo(UPDATED_GENSTAT);
        assertThat(testPlants.getGenntan()).isEqualTo(UPDATED_GENNTAN);
    }

    @Test
    @Transactional
    void patchNonExistingPlants() throws Exception {
        int databaseSizeBeforeUpdate = plantsRepository.findAll().size();
        plants.setId(count.incrementAndGet());

        // Create the Plants
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plantsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlants() throws Exception {
        int databaseSizeBeforeUpdate = plantsRepository.findAll().size();
        plants.setId(count.incrementAndGet());

        // Create the Plants
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlants() throws Exception {
        int databaseSizeBeforeUpdate = plantsRepository.findAll().size();
        plants.setId(count.incrementAndGet());

        // Create the Plants
        PlantsDTO plantsDTO = plantsMapper.toDto(plants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plantsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plants in the database
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlants() throws Exception {
        // Initialize the database
        plantsRepository.saveAndFlush(plants);

        int databaseSizeBeforeDelete = plantsRepository.findAll().size();

        // Delete the plants
        restPlantsMockMvc
            .perform(delete(ENTITY_API_URL_ID, plants.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plants> plantsList = plantsRepository.findAll();
        assertThat(plantsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
