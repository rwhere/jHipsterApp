package com.cs499.app.web.rest;

import com.cs499.app.JHipsterApp;

import com.cs499.app.domain.Son;
import com.cs499.app.repository.SonRepository;
import com.cs499.app.service.dto.SonDTO;
import com.cs499.app.service.mapper.SonMapper;

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
 * Test class for the SonResource REST controller.
 *
 * @see SonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JHipsterApp.class)
public class SonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    @Inject
    private SonRepository sonRepository;

    @Inject
    private SonMapper sonMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSonMockMvc;

    private Son son;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SonResource sonResource = new SonResource();
        ReflectionTestUtils.setField(sonResource, "sonRepository", sonRepository);
        ReflectionTestUtils.setField(sonResource, "sonMapper", sonMapper);
        this.restSonMockMvc = MockMvcBuilders.standaloneSetup(sonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Son createEntity(EntityManager em) {
        Son son = new Son()
                .name(DEFAULT_NAME)
                .age(DEFAULT_AGE);
        return son;
    }

    @Before
    public void initTest() {
        son = createEntity(em);
    }

    @Test
    @Transactional
    public void createSon() throws Exception {
        int databaseSizeBeforeCreate = sonRepository.findAll().size();

        // Create the Son
        SonDTO sonDTO = sonMapper.sonToSonDTO(son);

        restSonMockMvc.perform(post("/api/sons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sonDTO)))
            .andExpect(status().isCreated());

        // Validate the Son in the database
        List<Son> sonList = sonRepository.findAll();
        assertThat(sonList).hasSize(databaseSizeBeforeCreate + 1);
        Son testSon = sonList.get(sonList.size() - 1);
        assertThat(testSon.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSon.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    public void createSonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sonRepository.findAll().size();

        // Create the Son with an existing ID
        Son existingSon = new Son();
        existingSon.setId(1L);
        SonDTO existingSonDTO = sonMapper.sonToSonDTO(existingSon);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSonMockMvc.perform(post("/api/sons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Son> sonList = sonRepository.findAll();
        assertThat(sonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSons() throws Exception {
        // Initialize the database
        sonRepository.saveAndFlush(son);

        // Get all the sonList
        restSonMockMvc.perform(get("/api/sons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(son.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }

    @Test
    @Transactional
    public void getSon() throws Exception {
        // Initialize the database
        sonRepository.saveAndFlush(son);

        // Get the son
        restSonMockMvc.perform(get("/api/sons/{id}", son.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(son.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }

    @Test
    @Transactional
    public void getNonExistingSon() throws Exception {
        // Get the son
        restSonMockMvc.perform(get("/api/sons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSon() throws Exception {
        // Initialize the database
        sonRepository.saveAndFlush(son);
        int databaseSizeBeforeUpdate = sonRepository.findAll().size();

        // Update the son
        Son updatedSon = sonRepository.findOne(son.getId());
        updatedSon
                .name(UPDATED_NAME)
                .age(UPDATED_AGE);
        SonDTO sonDTO = sonMapper.sonToSonDTO(updatedSon);

        restSonMockMvc.perform(put("/api/sons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sonDTO)))
            .andExpect(status().isOk());

        // Validate the Son in the database
        List<Son> sonList = sonRepository.findAll();
        assertThat(sonList).hasSize(databaseSizeBeforeUpdate);
        Son testSon = sonList.get(sonList.size() - 1);
        assertThat(testSon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSon.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    public void updateNonExistingSon() throws Exception {
        int databaseSizeBeforeUpdate = sonRepository.findAll().size();

        // Create the Son
        SonDTO sonDTO = sonMapper.sonToSonDTO(son);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSonMockMvc.perform(put("/api/sons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sonDTO)))
            .andExpect(status().isCreated());

        // Validate the Son in the database
        List<Son> sonList = sonRepository.findAll();
        assertThat(sonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSon() throws Exception {
        // Initialize the database
        sonRepository.saveAndFlush(son);
        int databaseSizeBeforeDelete = sonRepository.findAll().size();

        // Get the son
        restSonMockMvc.perform(delete("/api/sons/{id}", son.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Son> sonList = sonRepository.findAll();
        assertThat(sonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
