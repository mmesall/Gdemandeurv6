package mfpai.gouv.sn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureP;
import mfpai.gouv.sn.repository.CandidaturePRepository;
import mfpai.gouv.sn.service.CandidaturePService;
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
 * REST controller for managing {@link mfpai.gouv.sn.domain.CandidatureP}.
 */
@RestController
@RequestMapping("/api")
public class CandidaturePResource {

    private final Logger log = LoggerFactory.getLogger(CandidaturePResource.class);

    private static final String ENTITY_NAME = "candidatureP";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CandidaturePService candidaturePService;

    private final CandidaturePRepository candidaturePRepository;

    public CandidaturePResource(CandidaturePService candidaturePService, CandidaturePRepository candidaturePRepository) {
        this.candidaturePService = candidaturePService;
        this.candidaturePRepository = candidaturePRepository;
    }

    /**
     * {@code POST  /candidature-ps} : Create a new candidatureP.
     *
     * @param candidatureP the candidatureP to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidatureP, or with status {@code 400 (Bad Request)} if the candidatureP has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/candidature-ps")
    public ResponseEntity<CandidatureP> createCandidatureP(@RequestBody CandidatureP candidatureP) throws URISyntaxException {
        log.debug("REST request to save CandidatureP : {}", candidatureP);
        if (candidatureP.getId() != null) {
            throw new BadRequestAlertException("A new candidatureP cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CandidatureP result = candidaturePService.save(candidatureP);
        return ResponseEntity
            .created(new URI("/api/candidature-ps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /candidature-ps/:id} : Updates an existing candidatureP.
     *
     * @param id the id of the candidatureP to save.
     * @param candidatureP the candidatureP to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureP,
     * or with status {@code 400 (Bad Request)} if the candidatureP is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidatureP couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/candidature-ps/{id}")
    public ResponseEntity<CandidatureP> updateCandidatureP(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureP candidatureP
    ) throws URISyntaxException {
        log.debug("REST request to update CandidatureP : {}, {}", id, candidatureP);
        if (candidatureP.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureP.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidaturePRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CandidatureP result = candidaturePService.update(candidatureP);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureP.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /candidature-ps/:id} : Partial updates given fields of an existing candidatureP, field will ignore if it is null
     *
     * @param id the id of the candidatureP to save.
     * @param candidatureP the candidatureP to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureP,
     * or with status {@code 400 (Bad Request)} if the candidatureP is not valid,
     * or with status {@code 404 (Not Found)} if the candidatureP is not found,
     * or with status {@code 500 (Internal Server Error)} if the candidatureP couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/candidature-ps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CandidatureP> partialUpdateCandidatureP(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureP candidatureP
    ) throws URISyntaxException {
        log.debug("REST request to partial update CandidatureP partially : {}, {}", id, candidatureP);
        if (candidatureP.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureP.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidaturePRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CandidatureP> result = candidaturePService.partialUpdate(candidatureP);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureP.getId().toString())
        );
    }

    /**
     * {@code GET  /candidature-ps} : get all the candidaturePS.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of candidaturePS in body.
     */
    @GetMapping("/candidature-ps")
    public ResponseEntity<List<CandidatureP>> getAllCandidaturePS(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of CandidaturePS");
        Page<CandidatureP> page;
        if (eagerload) {
            page = candidaturePService.findAllWithEagerRelationships(pageable);
        } else {
            page = candidaturePService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /candidature-ps/:id} : get the "id" candidatureP.
     *
     * @param id the id of the candidatureP to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidatureP, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/candidature-ps/{id}")
    public ResponseEntity<CandidatureP> getCandidatureP(@PathVariable Long id) {
        log.debug("REST request to get CandidatureP : {}", id);
        Optional<CandidatureP> candidatureP = candidaturePService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidatureP);
    }

    /**
     * {@code DELETE  /candidature-ps/:id} : delete the "id" candidatureP.
     *
     * @param id the id of the candidatureP to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/candidature-ps/{id}")
    public ResponseEntity<Void> deleteCandidatureP(@PathVariable Long id) {
        log.debug("REST request to delete CandidatureP : {}", id);
        candidaturePService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
