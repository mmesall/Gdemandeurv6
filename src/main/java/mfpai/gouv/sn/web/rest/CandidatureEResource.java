package mfpai.gouv.sn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureE;
import mfpai.gouv.sn.repository.CandidatureERepository;
import mfpai.gouv.sn.service.CandidatureEService;
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
 * REST controller for managing {@link mfpai.gouv.sn.domain.CandidatureE}.
 */
@RestController
@RequestMapping("/api")
public class CandidatureEResource {

    private final Logger log = LoggerFactory.getLogger(CandidatureEResource.class);

    private static final String ENTITY_NAME = "candidatureE";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CandidatureEService candidatureEService;

    private final CandidatureERepository candidatureERepository;

    public CandidatureEResource(CandidatureEService candidatureEService, CandidatureERepository candidatureERepository) {
        this.candidatureEService = candidatureEService;
        this.candidatureERepository = candidatureERepository;
    }

    /**
     * {@code POST  /candidature-es} : Create a new candidatureE.
     *
     * @param candidatureE the candidatureE to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidatureE, or with status {@code 400 (Bad Request)} if the candidatureE has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/candidature-es")
    public ResponseEntity<CandidatureE> createCandidatureE(@RequestBody CandidatureE candidatureE) throws URISyntaxException {
        log.debug("REST request to save CandidatureE : {}", candidatureE);
        if (candidatureE.getId() != null) {
            throw new BadRequestAlertException("A new candidatureE cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CandidatureE result = candidatureEService.save(candidatureE);
        return ResponseEntity
            .created(new URI("/api/candidature-es/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /candidature-es/:id} : Updates an existing candidatureE.
     *
     * @param id the id of the candidatureE to save.
     * @param candidatureE the candidatureE to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureE,
     * or with status {@code 400 (Bad Request)} if the candidatureE is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidatureE couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/candidature-es/{id}")
    public ResponseEntity<CandidatureE> updateCandidatureE(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureE candidatureE
    ) throws URISyntaxException {
        log.debug("REST request to update CandidatureE : {}, {}", id, candidatureE);
        if (candidatureE.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureE.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureERepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CandidatureE result = candidatureEService.update(candidatureE);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureE.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /candidature-es/:id} : Partial updates given fields of an existing candidatureE, field will ignore if it is null
     *
     * @param id the id of the candidatureE to save.
     * @param candidatureE the candidatureE to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureE,
     * or with status {@code 400 (Bad Request)} if the candidatureE is not valid,
     * or with status {@code 404 (Not Found)} if the candidatureE is not found,
     * or with status {@code 500 (Internal Server Error)} if the candidatureE couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/candidature-es/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CandidatureE> partialUpdateCandidatureE(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureE candidatureE
    ) throws URISyntaxException {
        log.debug("REST request to partial update CandidatureE partially : {}, {}", id, candidatureE);
        if (candidatureE.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureE.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureERepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CandidatureE> result = candidatureEService.partialUpdate(candidatureE);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureE.getId().toString())
        );
    }

    /**
     * {@code GET  /candidature-es} : get all the candidatureES.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of candidatureES in body.
     */
    @GetMapping("/candidature-es")
    public ResponseEntity<List<CandidatureE>> getAllCandidatureES(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of CandidatureES");
        Page<CandidatureE> page;
        if (eagerload) {
            page = candidatureEService.findAllWithEagerRelationships(pageable);
        } else {
            page = candidatureEService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /candidature-es/:id} : get the "id" candidatureE.
     *
     * @param id the id of the candidatureE to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidatureE, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/candidature-es/{id}")
    public ResponseEntity<CandidatureE> getCandidatureE(@PathVariable Long id) {
        log.debug("REST request to get CandidatureE : {}", id);
        Optional<CandidatureE> candidatureE = candidatureEService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidatureE);
    }

    /**
     * {@code DELETE  /candidature-es/:id} : delete the "id" candidatureE.
     *
     * @param id the id of the candidatureE to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/candidature-es/{id}")
    public ResponseEntity<Void> deleteCandidatureE(@PathVariable Long id) {
        log.debug("REST request to delete CandidatureE : {}", id);
        candidatureEService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
