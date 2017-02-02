package com.cs499.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs499.app.domain.Son;

import com.cs499.app.repository.SonRepository;
import com.cs499.app.web.rest.util.HeaderUtil;
import com.cs499.app.service.dto.SonDTO;
import com.cs499.app.service.mapper.SonMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Son.
 */
@RestController
@RequestMapping("/api")
public class SonResource {

    private final Logger log = LoggerFactory.getLogger(SonResource.class);
        
    @Inject
    private SonRepository sonRepository;

    @Inject
    private SonMapper sonMapper;

    /**
     * POST  /sons : Create a new son.
     *
     * @param sonDTO the sonDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sonDTO, or with status 400 (Bad Request) if the son has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sons")
    @Timed
    public ResponseEntity<SonDTO> createSon(@RequestBody SonDTO sonDTO) throws URISyntaxException {
        log.debug("REST request to save Son : {}", sonDTO);
        if (sonDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("son", "idexists", "A new son cannot already have an ID")).body(null);
        }
        Son son = sonMapper.sonDTOToSon(sonDTO);
        son = sonRepository.save(son);
        SonDTO result = sonMapper.sonToSonDTO(son);
        return ResponseEntity.created(new URI("/api/sons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("son", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sons : Updates an existing son.
     *
     * @param sonDTO the sonDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sonDTO,
     * or with status 400 (Bad Request) if the sonDTO is not valid,
     * or with status 500 (Internal Server Error) if the sonDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sons")
    @Timed
    public ResponseEntity<SonDTO> updateSon(@RequestBody SonDTO sonDTO) throws URISyntaxException {
        log.debug("REST request to update Son : {}", sonDTO);
        if (sonDTO.getId() == null) {
            return createSon(sonDTO);
        }
        Son son = sonMapper.sonDTOToSon(sonDTO);
        son = sonRepository.save(son);
        SonDTO result = sonMapper.sonToSonDTO(son);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("son", sonDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sons : get all the sons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sons in body
     */
    @GetMapping("/sons")
    @Timed
    public List<SonDTO> getAllSons() {
        log.debug("REST request to get all Sons");
        List<Son> sons = sonRepository.findAll();
        return sonMapper.sonsToSonDTOs(sons);
    }

    /**
     * GET  /sons/:id : get the "id" son.
     *
     * @param id the id of the sonDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sonDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sons/{id}")
    @Timed
    public ResponseEntity<SonDTO> getSon(@PathVariable Long id) {
        log.debug("REST request to get Son : {}", id);
        Son son = sonRepository.findOne(id);
        SonDTO sonDTO = sonMapper.sonToSonDTO(son);
        return Optional.ofNullable(sonDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sons/:id : delete the "id" son.
     *
     * @param id the id of the sonDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sons/{id}")
    @Timed
    public ResponseEntity<Void> deleteSon(@PathVariable Long id) {
        log.debug("REST request to delete Son : {}", id);
        sonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("son", id.toString())).build();
    }

}
