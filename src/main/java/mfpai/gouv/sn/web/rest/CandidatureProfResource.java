package mfpai.gouv.sn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureProf;
import mfpai.gouv.sn.repository.CandidatureProfRepository;
import mfpai.gouv.sn.service.CandidatureProfService;
import mfpai.gouv.sn.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mfpai.gouv.sn.domain.CandidatureProf}.
 */
@RestController
@RequestMapping("/api")
public class CandidatureProfResource {

    private final Logger log = LoggerFactory.getLogger(CandidatureProfResource.class);

    private static final String ENTITY_NAME = "candidatureProf";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CandidatureProfService candidatureProfService;

    private final CandidatureProfRepository candidatureProfRepository;

    public CandidatureProfResource(CandidatureProfService candidatureProfService, CandidatureProfRepository candidatureProfRepository) {
        this.candidatureProfService = candidatureProfService;
        this.candidatureProfRepository = candidatureProfRepository;
    }

    /**
     * {@code POST  /candidature-profs} : Create a new candidatureProf.
     *
     * @param candidatureProf the candidatureProf to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidatureProf, or with status {@code 400 (Bad Request)} if the candidatureProf has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/candidature-profs")
    public ResponseEntity<CandidatureProf> createCandidatureProf(@RequestBody CandidatureProf candidatureProf) throws URISyntaxException {
        log.debug("REST request to save CandidatureProf : {}", candidatureProf);
        if (candidatureProf.getId() != null) {
            throw new BadRequestAlertException("A new candidatureProf cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CandidatureProf result = candidatureProfService.save(candidatureProf);
        return ResponseEntity
            .created(new URI("/api/candidature-profs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /candidature-profs/:id} : Updates an existing candidatureProf.
     *
     * @param id the id of the candidatureProf to save.
     * @param candidatureProf the candidatureProf to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureProf,
     * or with status {@code 400 (Bad Request)} if the candidatureProf is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidatureProf couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/candidature-profs/{id}")
    public ResponseEntity<CandidatureProf> updateCandidatureProf(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureProf candidatureProf
    ) throws URISyntaxException {
        log.debug("REST request to update CandidatureProf : {}, {}", id, candidatureProf);
        if (candidatureProf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureProf.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureProfRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CandidatureProf result = candidatureProfService.save(candidatureProf);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureProf.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /candidature-profs/:id} : Partial updates given fields of an existing candidatureProf, field will ignore if it is null
     *
     * @param id the id of the candidatureProf to save.
     * @param candidatureProf the candidatureProf to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureProf,
     * or with status {@code 400 (Bad Request)} if the candidatureProf is not valid,
     * or with status {@code 404 (Not Found)} if the candidatureProf is not found,
     * or with status {@code 500 (Internal Server Error)} if the candidatureProf couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/candidature-profs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CandidatureProf> partialUpdateCandidatureProf(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureProf candidatureProf
    ) throws URISyntaxException {
        log.debug("REST request to partial update CandidatureProf partially : {}, {}", id, candidatureProf);
        if (candidatureProf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureProf.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureProfRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CandidatureProf> result = candidatureProfService.partialUpdate(candidatureProf);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureProf.getId().toString())
        );
    }

    /**
     * {@code GET  /candidature-profs} : get all the candidatureProfs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of candidatureProfs in body.
     */
    @GetMapping("/candidature-profs")
    public ResponseEntity<List<CandidatureProf>> getAllCandidatureProfs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CandidatureProfs");
        Page<CandidatureProf> page;
        if (eagerload) {
            page = candidatureProfService.findAllWithEagerRelationships(pageable);
        } else {
            page = candidatureProfService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /candidature-profs/:id} : get the "id" candidatureProf.
     *
     * @param id the id of the candidatureProf to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidatureProf, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/candidature-profs/{id}")
    public ResponseEntity<CandidatureProf> getCandidatureProf(@PathVariable Long id) {
        log.debug("REST request to get CandidatureProf : {}", id);
        Optional<CandidatureProf> candidatureProf = candidatureProfService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidatureProf);
    }

    /**
     * {@code DELETE  /candidature-profs/:id} : delete the "id" candidatureProf.
     *
     * @param id the id of the candidatureProf to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/candidature-profs/{id}")
    public ResponseEntity<Void> deleteCandidatureProf(@PathVariable Long id) {
        log.debug("REST request to delete CandidatureProf : {}", id);
        candidatureProfService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
