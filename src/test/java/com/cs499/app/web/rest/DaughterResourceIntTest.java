package com.cs499.app.web.rest;

import com.cs499.app.JHipsterApp;

import com.cs499.app.domain.Daughter;
import com.cs499.app.repository.DaughterRepository;
import com.cs499.app.service.dto.DaughterDTO;
import com.cs499.app.service.mapper.DaughterMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DaughterResource REST controller.
 *
 * @see DaughterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JHipsterApp.class)
public class DaughterResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    @Inject
    private DaughterRepository daughterRepository;

    @Inject
    private DaughterMapper daughterMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDaughterMockMvc;

    private Daughter daughter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DaughterResource daughterResource = new DaughterResource();
        ReflectionTestUtils.setField(daughterResource, "daughterRepository", daughterRepository);
        ReflectionTestUtils.setField(daughterResource, "daughterMapper", daughterMapper);
        this.restDaughterMockMvc = MockMvcBuilders.standaloneSetup(daughterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Daughter createEntity(EntityManager em) {
        Daughter daughter = new Daughter()
                .name(DEFAULT_NAME)
                .age(DEFAULT_AGE);
        return daughter;
    }

    @Before
    public void initTest() {
        daughter = createEntity(em);
    }

    @Test
    @Transactional
    public void createDaughter() throws Exception {
        int databaseSizeBeforeCreate = daughterRepository.findAll().size();

        // Create the Daughter
        DaughterDTO daughterDTO = daughterMapper.daughterToDaughterDTO(daughter);

        restDaughterMockMvc.perform(post("/api/daughters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(daughterDTO)))
            .andExpect(status().isCreated());

        // Validate the Daughter in the database
        List<Daughter> daughterList = daughterRepository.findAll();
        assertThat(daughterList).hasSize(databaseSizeBeforeCreate + 1);
        Daughter testDaughter = daughterList.get(daughterList.size() - 1);
        assertThat(testDaughter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDaughter.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    public void createDaughterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = daughterRepository.findAll().size();

        // Create the Daughter with an existing ID
        Daughter existingDaughter = new Daughter();
        existingDaughter.setId(1L);
        DaughterDTO existingDaughterDTO = daughterMapper.daughterToDaughterDTO(existingDaughter);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDaughterMockMvc.perform(post("/api/daughters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDaughterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Daughter> daughterList = daughterRepository.findAll();
        assertThat(daughterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDaughters() throws Exception {
        // Initialize the database
        daughterRepository.saveAndFlush(daughter);

        // Get all the daughterList
        restDaughterMockMvc.perform(get("/api/daughters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(daughter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }

    @Test
    @Transactional
    public void getDaughter() throws Exception {
        // Initialize the database
        daughterRepository.saveAndFlush(daughter);

        // Get the daughter
        restDaughterMockMvc.perform(get("/api/daughters/{id}", daughter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(daughter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }

    @Test
    @Transactional
    public void getNonExistingDaughter() throws Exception {
        // Get the daughter
        restDaughterMockMvc.perform(get("/api/daughters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDaughter() throws Exception {
        // Initialize the database
        daughterRepository.saveAndFlush(daughter);
        int databaseSizeBeforeUpdate = daughterRepository.findAll().size();

        // Update the daughter
        Daughter updatedDaughter = daughterRepository.findOne(daughter.getId());
        updatedDaughter
                .name(UPDATED_NAME)
                .age(UPDATED_AGE);
        DaughterDTO daughterDTO = daughterMapper.daughterToDaughterDTO(updatedDaughter);

        restDaughterMockMvc.perform(put("/api/daughters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(daughterDTO)))
            .andExpect(status().isOk());

        // Validate the Daughter in the database
        List<Daughter> daughterList = daughterRepository.findAll();
        assertThat(daughterList).hasSize(databaseSizeBeforeUpdate);
        Daughter testDaughter = daughterList.get(daughterList.size() - 1);
        assertThat(testDaughter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDaughter.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    public void updateNonExistingDaughter() throws Exception {
        int databaseSizeBeforeUpdate = daughterRepository.findAll().size();

        // Create the Daughter
        DaughterDTO daughterDTO = daughterMapper.daughterToDaughterDTO(daughter);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDaughterMockMvc.perform(put("/api/daughters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(daughterDTO)))
            .andExpect(status().isCreated());

        // Validate the Daughter in the database
        List<Daughter> daughterList = daughterRepository.findAll();
        assertThat(daughterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDaughter() throws Exception {
        // Initialize the database
        daughterRepository.saveAndFlush(daughter);
        int databaseSizeBeforeDelete = daughterRepository.findAll().size();

        // Get the daughter
        restDaughterMockMvc.perform(delete("/api/daughters/{id}", daughter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Daughter> daughterList = daughterRepository.findAll();
        assertThat(daughterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
