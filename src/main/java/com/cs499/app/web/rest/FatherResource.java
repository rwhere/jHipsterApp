package com.cs499.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs499.app.domain.Father;

import com.cs499.app.repository.FatherRepository;
import com.cs499.app.web.rest.util.HeaderUtil;
import com.cs499.app.service.dto.FatherDTO;
import com.cs499.app.service.mapper.FatherMapper;

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
 * REST controller for managing Father.
 */
@RestController
@RequestMapping("/api")
public class FatherResource {

    private final Logger log = LoggerFactory.getLogger(FatherResource.class);
        
    @Inject
    private FatherRepository fatherRepository;

    @Inject
    private FatherMapper fatherMapper;

    /**
     * POST  /fathers : Create a new father.
     *
     * @param fatherDTO the fatherDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fatherDTO, or with status 400 (Bad Request) if the father has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fathers")
    @Timed
    public ResponseEntity<FatherDTO> createFather(@RequestBody FatherDTO fatherDTO) throws URISyntaxException {
        log.debug("REST request to save Father : {}", fatherDTO);
        if (fatherDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("father", "idexists", "A new father cannot already have an ID")).body(null);
        }
        Father father = fatherMapper.fatherDTOToFather(fatherDTO);
        father = fatherRepository.save(father);
        FatherDTO result = fatherMapper.fatherToFatherDTO(father);
        return ResponseEntity.created(new URI("/api/fathers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("father", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fathers : Updates an existing father.
     *
     * @param fatherDTO the fatherDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fatherDTO,
     * or with status 400 (Bad Request) if the fatherDTO is not valid,
     * or with status 500 (Internal Server Error) if the fatherDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fathers")
    @Timed
    public ResponseEntity<FatherDTO> updateFather(@RequestBody FatherDTO fatherDTO) throws URISyntaxException {
        log.debug("REST request to update Father : {}", fatherDTO);
        if (fatherDTO.getId() == null) {
            return createFather(fatherDTO);
        }
        Father father = fatherMapper.fatherDTOToFather(fatherDTO);
        father = fatherRepository.save(father);
        FatherDTO result = fatherMapper.fatherToFatherDTO(father);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("father", fatherDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fathers : get all the fathers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fathers in body
     */
    @GetMapping("/fathers")
    @Timed
    public List<FatherDTO> getAllFathers() {
        log.debug("REST request to get all Fathers");
        List<Father> fathers = fatherRepository.findAll();
        return fatherMapper.fathersToFatherDTOs(fathers);
    }

    /**
     * GET  /fathers/:id : get the "id" father.
     *
     * @param id the id of the fatherDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fatherDTO, or with status 404 (Not Found)
     */
    @GetMapping("/fathers/{id}")
    @Timed
    public ResponseEntity<FatherDTO> getFather(@PathVariable Long id) {
        log.debug("REST request to get Father : {}", id);
        Father father = fatherRepository.findOne(id);
        FatherDTO fatherDTO = fatherMapper.fatherToFatherDTO(father);
        return Optional.ofNullable(fatherDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fathers/:id : delete the "id" father.
     *
     * @param id the id of the fatherDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fathers/{id}")
    @Timed
    public ResponseEntity<Void> deleteFather(@PathVariable Long id) {
        log.debug("REST request to delete Father : {}", id);
        fatherRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("father", id.toString())).build();
    }

}
