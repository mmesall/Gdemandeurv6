package mfpai.gouv.sn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mfpai.gouv.sn.domain.Candidature;
import mfpai.gouv.sn.repository.CandidatureRepository;
import mfpai.gouv.sn.service.CandidatureService;
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
 * REST controller for managing {@link mfpai.gouv.sn.domain.Candidature}.
 */
@RestController
@RequestMapping("/api")
public class CandidatureResource {

    private final Logger log = LoggerFactory.getLogger(CandidatureResource.class);

    private static final String ENTITY_NAME = "candidature";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CandidatureService candidatureService;

    private final CandidatureRepository candidatureRepository;

    public CandidatureResource(CandidatureService candidatureService, CandidatureRepository candidatureRepository) {
        this.candidatureService = candidatureService;
        this.candidatureRepository = candidatureRepository;
    }

    /**
     * {@code POST  /candidatures} : Create a new candidature.
     *
     * @param candidature the candidature to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidature, or with status {@code 400 (Bad Request)} if the candidature has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/candidatures")
    public ResponseEntity<Candidature> createCandidature(@RequestBody Candidature candidature) throws URISyntaxException {
        log.debug("REST request to save Candidature : {}", candidature);
        if (candidature.getId() != null) {
            throw new BadRequestAlertException("A new candidature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Candidature result = candidatureService.save(candidature);
        return ResponseEntity
            .created(new URI("/api/candidatures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /candidatures/:id} : Updates an existing candidature.
     *
     * @param id the id of the candidature to save.
     * @param candidature the candidature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidature,
     * or with status {@code 400 (Bad Request)} if the candidature is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/candidatures/{id}")
    public ResponseEntity<Candidature> updateCandidature(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Candidature candidature
    ) throws URISyntaxException {
        log.debug("REST request to update Candidature : {}, {}", id, candidature);
        if (candidature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Candidature result = candidatureService.save(candidature);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidature.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /candidatures/:id} : Partial updates given fields of an existing candidature, field will ignore if it is null
     *
     * @param id the id of the candidature to save.
     * @param candidature the candidature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidature,
     * or with status {@code 400 (Bad Request)} if the candidature is not valid,
     * or with status {@code 404 (Not Found)} if the candidature is not found,
     * or with status {@code 500 (Internal Server Error)} if the candidature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/candidatures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Candidature> partialUpdateCandidature(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Candidature candidature
    ) throws URISyntaxException {
        log.debug("REST request to partial update Candidature partially : {}, {}", id, candidature);
        if (candidature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Candidature> result = candidatureService.partialUpdate(candidature);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidature.getId().toString())
        );
    }

    /**
     * {@code GET  /candidatures} : get all the candidatures.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of candidatures in body.
     */
    @GetMapping("/candidatures")
    public ResponseEntity<List<Candidature>> getAllCandidatures(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Candidatures");
        Page<Candidature> page;
        if (eagerload) {
            page = candidatureService.findAllWithEagerRelationships(pageable);
        } else {
            page = candidatureService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /candidatures/:id} : get the "id" candidature.
     *
     * @param id the id of the candidature to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidature, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/candidatures/{id}")
    public ResponseEntity<Candidature> getCandidature(@PathVariable Long id) {
        log.debug("REST request to get Candidature : {}", id);
        Optional<Candidature> candidature = candidatureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidature);
    }

    /**
     * {@code DELETE  /candidatures/:id} : delete the "id" candidature.
     *
     * @param id the id of the candidature to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/candidatures/{id}")
    public ResponseEntity<Void> deleteCandidature(@PathVariable Long id) {
        log.debug("REST request to delete Candidature : {}", id);
        candidatureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
