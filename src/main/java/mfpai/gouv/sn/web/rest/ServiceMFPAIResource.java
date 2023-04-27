package mfpai.gouv.sn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mfpai.gouv.sn.domain.ServiceMFPAI;
import mfpai.gouv.sn.repository.ServiceMFPAIRepository;
import mfpai.gouv.sn.service.ServiceMFPAIService;
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
 * REST controller for managing {@link mfpai.gouv.sn.domain.ServiceMFPAI}.
 */
@RestController
@RequestMapping("/api")
public class ServiceMFPAIResource {

    private final Logger log = LoggerFactory.getLogger(ServiceMFPAIResource.class);

    private static final String ENTITY_NAME = "serviceMFPAI";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceMFPAIService serviceMFPAIService;

    private final ServiceMFPAIRepository serviceMFPAIRepository;

    public ServiceMFPAIResource(ServiceMFPAIService serviceMFPAIService, ServiceMFPAIRepository serviceMFPAIRepository) {
        this.serviceMFPAIService = serviceMFPAIService;
        this.serviceMFPAIRepository = serviceMFPAIRepository;
    }

    /**
     * {@code POST  /service-mfpais} : Create a new serviceMFPAI.
     *
     * @param serviceMFPAI the serviceMFPAI to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceMFPAI, or with status {@code 400 (Bad Request)} if the serviceMFPAI has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-mfpais")
    public ResponseEntity<ServiceMFPAI> createServiceMFPAI(@Valid @RequestBody ServiceMFPAI serviceMFPAI) throws URISyntaxException {
        log.debug("REST request to save ServiceMFPAI : {}", serviceMFPAI);
        if (serviceMFPAI.getId() != null) {
            throw new BadRequestAlertException("A new serviceMFPAI cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceMFPAI result = serviceMFPAIService.save(serviceMFPAI);
        return ResponseEntity
            .created(new URI("/api/service-mfpais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-mfpais/:id} : Updates an existing serviceMFPAI.
     *
     * @param id the id of the serviceMFPAI to save.
     * @param serviceMFPAI the serviceMFPAI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceMFPAI,
     * or with status {@code 400 (Bad Request)} if the serviceMFPAI is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceMFPAI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-mfpais/{id}")
    public ResponseEntity<ServiceMFPAI> updateServiceMFPAI(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceMFPAI serviceMFPAI
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceMFPAI : {}, {}", id, serviceMFPAI);
        if (serviceMFPAI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceMFPAI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceMFPAIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceMFPAI result = serviceMFPAIService.save(serviceMFPAI);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceMFPAI.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-mfpais/:id} : Partial updates given fields of an existing serviceMFPAI, field will ignore if it is null
     *
     * @param id the id of the serviceMFPAI to save.
     * @param serviceMFPAI the serviceMFPAI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceMFPAI,
     * or with status {@code 400 (Bad Request)} if the serviceMFPAI is not valid,
     * or with status {@code 404 (Not Found)} if the serviceMFPAI is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceMFPAI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-mfpais/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceMFPAI> partialUpdateServiceMFPAI(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceMFPAI serviceMFPAI
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceMFPAI partially : {}, {}", id, serviceMFPAI);
        if (serviceMFPAI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceMFPAI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceMFPAIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceMFPAI> result = serviceMFPAIService.partialUpdate(serviceMFPAI);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceMFPAI.getId().toString())
        );
    }

    /**
     * {@code GET  /service-mfpais} : get all the serviceMFPAIS.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceMFPAIS in body.
     */
    @GetMapping("/service-mfpais")
    public ResponseEntity<List<ServiceMFPAI>> getAllServiceMFPAIS(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("agent-is-null".equals(filter)) {
            log.debug("REST request to get all ServiceMFPAIs where agent is null");
            return new ResponseEntity<>(serviceMFPAIService.findAllWhereAgentIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of ServiceMFPAIS");
        Page<ServiceMFPAI> page = serviceMFPAIService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-mfpais/:id} : get the "id" serviceMFPAI.
     *
     * @param id the id of the serviceMFPAI to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceMFPAI, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-mfpais/{id}")
    public ResponseEntity<ServiceMFPAI> getServiceMFPAI(@PathVariable Long id) {
        log.debug("REST request to get ServiceMFPAI : {}", id);
        Optional<ServiceMFPAI> serviceMFPAI = serviceMFPAIService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceMFPAI);
    }

    /**
     * {@code DELETE  /service-mfpais/:id} : delete the "id" serviceMFPAI.
     *
     * @param id the id of the serviceMFPAI to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-mfpais/{id}")
    public ResponseEntity<Void> deleteServiceMFPAI(@PathVariable Long id) {
        log.debug("REST request to delete ServiceMFPAI : {}", id);
        serviceMFPAIService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
