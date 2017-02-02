package com.cs499.app.web.rest;

import com.cs499.app.JHipsterApp;

import com.cs499.app.domain.Father;
import com.cs499.app.repository.FatherRepository;
import com.cs499.app.service.dto.FatherDTO;
import com.cs499.app.service.mapper.FatherMapper;

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
 * Test class for the FatherResource REST controller.
 *
 * @see FatherResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JHipsterApp.class)
public class FatherResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    @Inject
    private FatherRepository fatherRepository;

    @Inject
    private FatherMapper fatherMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFatherMockMvc;

    private Father father;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FatherResource fatherResource = new FatherResource();
        ReflectionTestUtils.setField(fatherResource, "fatherRepository", fatherRepository);
        ReflectionTestUtils.setField(fatherResource, "fatherMapper", fatherMapper);
        this.restFatherMockMvc = MockMvcBuilders.standaloneSetup(fatherResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Father createEntity(EntityManager em) {
        Father father = new Father()
                .name(DEFAULT_NAME)
                .age(DEFAULT_AGE);
        return father;
    }

    @Before
    public void initTest() {
        father = createEntity(em);
    }

    @Test
    @Transactional
    public void createFather() throws Exception {
        int databaseSizeBeforeCreate = fatherRepository.findAll().size();

        // Create the Father
        FatherDTO fatherDTO = fatherMapper.fatherToFatherDTO(father);

        restFatherMockMvc.perform(post("/api/fathers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fatherDTO)))
            .andExpect(status().isCreated());

        // Validate the Father in the database
        List<Father> fatherList = fatherRepository.findAll();
        assertThat(fatherList).hasSize(databaseSizeBeforeCreate + 1);
        Father testFather = fatherList.get(fatherList.size() - 1);
        assertThat(testFather.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFather.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    public void createFatherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fatherRepository.findAll().size();

        // Create the Father with an existing ID
        Father existingFather = new Father();
        existingFather.setId(1L);
        FatherDTO existingFatherDTO = fatherMapper.fatherToFatherDTO(existingFather);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFatherMockMvc.perform(post("/api/fathers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFatherDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Father> fatherList = fatherRepository.findAll();
        assertThat(fatherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFathers() throws Exception {
        // Initialize the database
        fatherRepository.saveAndFlush(father);

        // Get all the fatherList
        restFatherMockMvc.perform(get("/api/fathers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(father.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }

    @Test
    @Transactional
    public void getFather() throws Exception {
        // Initialize the database
        fatherRepository.saveAndFlush(father);

        // Get the father
        restFatherMockMvc.perform(get("/api/fathers/{id}", father.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(father.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }

    @Test
    @Transactional
    public void getNonExistingFather() throws Exception {
        // Get the father
        restFatherMockMvc.perform(get("/api/fathers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFather() throws Exception {
        // Initialize the database
        fatherRepository.saveAndFlush(father);
        int databaseSizeBeforeUpdate = fatherRepository.findAll().size();

        // Update the father
        Father updatedFather = fatherRepository.findOne(father.getId());
        updatedFather
                .name(UPDATED_NAME)
                .age(UPDATED_AGE);
        FatherDTO fatherDTO = fatherMapper.fatherToFatherDTO(updatedFather);

        restFatherMockMvc.perform(put("/api/fathers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fatherDTO)))
            .andExpect(status().isOk());

        // Validate the Father in the database
        List<Father> fatherList = fatherRepository.findAll();
        assertThat(fatherList).hasSize(databaseSizeBeforeUpdate);
        Father testFather = fatherList.get(fatherList.size() - 1);
        assertThat(testFather.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFather.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    public void updateNonExistingFather() throws Exception {
        int databaseSizeBeforeUpdate = fatherRepository.findAll().size();

        // Create the Father
        FatherDTO fatherDTO = fatherMapper.fatherToFatherDTO(father);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFatherMockMvc.perform(put("/api/fathers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fatherDTO)))
            .andExpect(status().isCreated());

        // Validate the Father in the database
        List<Father> fatherList = fatherRepository.findAll();
        assertThat(fatherList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFather() throws Exception {
        // Initialize the database
        fatherRepository.saveAndFlush(father);
        int databaseSizeBeforeDelete = fatherRepository.findAll().size();

        // Get the father
        restFatherMockMvc.perform(delete("/api/fathers/{id}", father.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Father> fatherList = fatherRepository.findAll();
        assertThat(fatherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
