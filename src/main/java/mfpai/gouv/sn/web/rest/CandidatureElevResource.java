package mfpai.gouv.sn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureElev;
import mfpai.gouv.sn.repository.CandidatureElevRepository;
import mfpai.gouv.sn.service.CandidatureElevService;
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
 * REST controller for managing {@link mfpai.gouv.sn.domain.CandidatureElev}.
 */
@RestController
@RequestMapping("/api")
public class CandidatureElevResource {

    private final Logger log = LoggerFactory.getLogger(CandidatureElevResource.class);

    private static final String ENTITY_NAME = "candidatureElev";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CandidatureElevService candidatureElevService;

    private final CandidatureElevRepository candidatureElevRepository;

    public CandidatureElevResource(CandidatureElevService candidatureElevService, CandidatureElevRepository candidatureElevRepository) {
        this.candidatureElevService = candidatureElevService;
        this.candidatureElevRepository = candidatureElevRepository;
    }

    /**
     * {@code POST  /candidature-elevs} : Create a new candidatureElev.
     *
     * @param candidatureElev the candidatureElev to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidatureElev, or with status {@code 400 (Bad Request)} if the candidatureElev has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/candidature-elevs")
    public ResponseEntity<CandidatureElev> createCandidatureElev(@RequestBody CandidatureElev candidatureElev) throws URISyntaxException {
        log.debug("REST request to save CandidatureElev : {}", candidatureElev);
        if (candidatureElev.getId() != null) {
            throw new BadRequestAlertException("A new candidatureElev cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CandidatureElev result = candidatureElevService.save(candidatureElev);
        return ResponseEntity
            .created(new URI("/api/candidature-elevs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /candidature-elevs/:id} : Updates an existing candidatureElev.
     *
     * @param id the id of the candidatureElev to save.
     * @param candidatureElev the candidatureElev to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureElev,
     * or with status {@code 400 (Bad Request)} if the candidatureElev is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidatureElev couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/candidature-elevs/{id}")
    public ResponseEntity<CandidatureElev> updateCandidatureElev(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureElev candidatureElev
    ) throws URISyntaxException {
        log.debug("REST request to update CandidatureElev : {}, {}", id, candidatureElev);
        if (candidatureElev.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureElev.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureElevRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CandidatureElev result = candidatureElevService.save(candidatureElev);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureElev.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /candidature-elevs/:id} : Partial updates given fields of an existing candidatureElev, field will ignore if it is null
     *
     * @param id the id of the candidatureElev to save.
     * @param candidatureElev the candidatureElev to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureElev,
     * or with status {@code 400 (Bad Request)} if the candidatureElev is not valid,
     * or with status {@code 404 (Not Found)} if the candidatureElev is not found,
     * or with status {@code 500 (Internal Server Error)} if the candidatureElev couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/candidature-elevs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CandidatureElev> partialUpdateCandidatureElev(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureElev candidatureElev
    ) throws URISyntaxException {
        log.debug("REST request to partial update CandidatureElev partially : {}, {}", id, candidatureElev);
        if (candidatureElev.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureElev.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureElevRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CandidatureElev> result = candidatureElevService.partialUpdate(candidatureElev);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureElev.getId().toString())
        );
    }

    /**
     * {@code GET  /candidature-elevs} : get all the candidatureElevs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of candidatureElevs in body.
     */
    @GetMapping("/candidature-elevs")
    public ResponseEntity<List<CandidatureElev>> getAllCandidatureElevs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CandidatureElevs");
        Page<CandidatureElev> page;
        if (eagerload) {
            page = candidatureElevService.findAllWithEagerRelationships(pageable);
        } else {
            page = candidatureElevService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /candidature-elevs/:id} : get the "id" candidatureElev.
     *
     * @param id the id of the candidatureElev to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidatureElev, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/candidature-elevs/{id}")
    public ResponseEntity<CandidatureElev> getCandidatureElev(@PathVariable Long id) {
        log.debug("REST request to get CandidatureElev : {}", id);
        Optional<CandidatureElev> candidatureElev = candidatureElevService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidatureElev);
    }

    /**
     * {@code DELETE  /candidature-elevs/:id} : delete the "id" candidatureElev.
     *
     * @param id the id of the candidatureElev to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/candidature-elevs/{id}")
    public ResponseEntity<Void> deleteCandidatureElev(@PathVariable Long id) {
        log.debug("REST request to delete CandidatureElev : {}", id);
        candidatureElevService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
