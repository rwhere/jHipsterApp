package com.cs499.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs499.app.domain.Daughter;

import com.cs499.app.repository.DaughterRepository;
import com.cs499.app.web.rest.util.HeaderUtil;
import com.cs499.app.service.dto.DaughterDTO;
import com.cs499.app.service.mapper.DaughterMapper;

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
 * REST controller for managing Daughter.
 */
@RestController
@RequestMapping("/api")
public class DaughterResource {

    private final Logger log = LoggerFactory.getLogger(DaughterResource.class);
        
    @Inject
    private DaughterRepository daughterRepository;

    @Inject
    private DaughterMapper daughterMapper;

    /**
     * POST  /daughters : Create a new daughter.
     *
     * @param daughterDTO the daughterDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new daughterDTO, or with status 400 (Bad Request) if the daughter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/daughters")
    @Timed
    public ResponseEntity<DaughterDTO> createDaughter(@RequestBody DaughterDTO daughterDTO) throws URISyntaxException {
        log.debug("REST request to save Daughter : {}", daughterDTO);
        if (daughterDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("daughter", "idexists", "A new daughter cannot already have an ID")).body(null);
        }
        Daughter daughter = daughterMapper.daughterDTOToDaughter(daughterDTO);
        daughter = daughterRepository.save(daughter);
        DaughterDTO result = daughterMapper.daughterToDaughterDTO(daughter);
        return ResponseEntity.created(new URI("/api/daughters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("daughter", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /daughters : Updates an existing daughter.
     *
     * @param daughterDTO the daughterDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated daughterDTO,
     * or with status 400 (Bad Request) if the daughterDTO is not valid,
     * or with status 500 (Internal Server Error) if the daughterDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/daughters")
    @Timed
    public ResponseEntity<DaughterDTO> updateDaughter(@RequestBody DaughterDTO daughterDTO) throws URISyntaxException {
        log.debug("REST request to update Daughter : {}", daughterDTO);
        if (daughterDTO.getId() == null) {
            return createDaughter(daughterDTO);
        }
        Daughter daughter = daughterMapper.daughterDTOToDaughter(daughterDTO);
        daughter = daughterRepository.save(daughter);
        DaughterDTO result = daughterMapper.daughterToDaughterDTO(daughter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("daughter", daughterDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /daughters : get all the daughters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of daughters in body
     */
    @GetMapping("/daughters")
    @Timed
    public List<DaughterDTO> getAllDaughters() {
        log.debug("REST request to get all Daughters");
        List<Daughter> daughters = daughterRepository.findAll();
        return daughterMapper.daughtersToDaughterDTOs(daughters);
    }

    /**
     * GET  /daughters/:id : get the "id" daughter.
     *
     * @param id the id of the daughterDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the daughterDTO, or with status 404 (Not Found)
     */
    @GetMapping("/daughters/{id}")
    @Timed
    public ResponseEntity<DaughterDTO> getDaughter(@PathVariable Long id) {
        log.debug("REST request to get Daughter : {}", id);
        Daughter daughter = daughterRepository.findOne(id);
        DaughterDTO daughterDTO = daughterMapper.daughterToDaughterDTO(daughter);
        return Optional.ofNullable(daughterDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /daughters/:id : delete the "id" daughter.
     *
     * @param id the id of the daughterDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/daughters/{id}")
    @Timed
    public ResponseEntity<Void> deleteDaughter(@PathVariable Long id) {
        log.debug("REST request to delete Daughter : {}", id);
        daughterRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("daughter", id.toString())).build();
    }

}
