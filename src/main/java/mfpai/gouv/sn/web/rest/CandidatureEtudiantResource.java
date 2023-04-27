package mfpai.gouv.sn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureEtudiant;
import mfpai.gouv.sn.repository.CandidatureEtudiantRepository;
import mfpai.gouv.sn.service.CandidatureEtudiantService;
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
 * REST controller for managing {@link mfpai.gouv.sn.domain.CandidatureEtudiant}.
 */
@RestController
@RequestMapping("/api")
public class CandidatureEtudiantResource {

    private final Logger log = LoggerFactory.getLogger(CandidatureEtudiantResource.class);

    private static final String ENTITY_NAME = "candidatureEtudiant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CandidatureEtudiantService candidatureEtudiantService;

    private final CandidatureEtudiantRepository candidatureEtudiantRepository;

    public CandidatureEtudiantResource(
        CandidatureEtudiantService candidatureEtudiantService,
        CandidatureEtudiantRepository candidatureEtudiantRepository
    ) {
        this.candidatureEtudiantService = candidatureEtudiantService;
        this.candidatureEtudiantRepository = candidatureEtudiantRepository;
    }

    /**
     * {@code POST  /candidature-etudiants} : Create a new candidatureEtudiant.
     *
     * @param candidatureEtudiant the candidatureEtudiant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidatureEtudiant, or with status {@code 400 (Bad Request)} if the candidatureEtudiant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/candidature-etudiants")
    public ResponseEntity<CandidatureEtudiant> createCandidatureEtudiant(@RequestBody CandidatureEtudiant candidatureEtudiant)
        throws URISyntaxException {
        log.debug("REST request to save CandidatureEtudiant : {}", candidatureEtudiant);
        if (candidatureEtudiant.getId() != null) {
            throw new BadRequestAlertException("A new candidatureEtudiant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CandidatureEtudiant result = candidatureEtudiantService.save(candidatureEtudiant);
        return ResponseEntity
            .created(new URI("/api/candidature-etudiants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /candidature-etudiants/:id} : Updates an existing candidatureEtudiant.
     *
     * @param id the id of the candidatureEtudiant to save.
     * @param candidatureEtudiant the candidatureEtudiant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureEtudiant,
     * or with status {@code 400 (Bad Request)} if the candidatureEtudiant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidatureEtudiant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/candidature-etudiants/{id}")
    public ResponseEntity<CandidatureEtudiant> updateCandidatureEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureEtudiant candidatureEtudiant
    ) throws URISyntaxException {
        log.debug("REST request to update CandidatureEtudiant : {}, {}", id, candidatureEtudiant);
        if (candidatureEtudiant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureEtudiant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CandidatureEtudiant result = candidatureEtudiantService.save(candidatureEtudiant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureEtudiant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /candidature-etudiants/:id} : Partial updates given fields of an existing candidatureEtudiant, field will ignore if it is null
     *
     * @param id the id of the candidatureEtudiant to save.
     * @param candidatureEtudiant the candidatureEtudiant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidatureEtudiant,
     * or with status {@code 400 (Bad Request)} if the candidatureEtudiant is not valid,
     * or with status {@code 404 (Not Found)} if the candidatureEtudiant is not found,
     * or with status {@code 500 (Internal Server Error)} if the candidatureEtudiant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/candidature-etudiants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CandidatureEtudiant> partialUpdateCandidatureEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CandidatureEtudiant candidatureEtudiant
    ) throws URISyntaxException {
        log.debug("REST request to partial update CandidatureEtudiant partially : {}, {}", id, candidatureEtudiant);
        if (candidatureEtudiant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureEtudiant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CandidatureEtudiant> result = candidatureEtudiantService.partialUpdate(candidatureEtudiant);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidatureEtudiant.getId().toString())
        );
    }

    /**
     * {@code GET  /candidature-etudiants} : get all the candidatureEtudiants.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of candidatureEtudiants in body.
     */
    @GetMapping("/candidature-etudiants")
    public ResponseEntity<List<CandidatureEtudiant>> getAllCandidatureEtudiants(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CandidatureEtudiants");
        Page<CandidatureEtudiant> page;
        if (eagerload) {
            page = candidatureEtudiantService.findAllWithEagerRelationships(pageable);
        } else {
            page = candidatureEtudiantService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /candidature-etudiants/:id} : get the "id" candidatureEtudiant.
     *
     * @param id the id of the candidatureEtudiant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidatureEtudiant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/candidature-etudiants/{id}")
    public ResponseEntity<CandidatureEtudiant> getCandidatureEtudiant(@PathVariable Long id) {
        log.debug("REST request to get CandidatureEtudiant : {}", id);
        Optional<CandidatureEtudiant> candidatureEtudiant = candidatureEtudiantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidatureEtudiant);
    }

    /**
     * {@code DELETE  /candidature-etudiants/:id} : delete the "id" candidatureEtudiant.
     *
     * @param id the id of the candidatureEtudiant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/candidature-etudiants/{id}")
    public ResponseEntity<Void> deleteCandidatureEtudiant(@PathVariable Long id) {
        log.debug("REST request to delete CandidatureEtudiant : {}", id);
        candidatureEtudiantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
