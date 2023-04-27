package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.PriseEnCharge;
import mfpai.gouv.sn.repository.PriseEnChargeRepository;
import mfpai.gouv.sn.service.PriseEnChargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PriseEnCharge}.
 */
@Service
@Transactional
public class PriseEnChargeServiceImpl implements PriseEnChargeService {

    private final Logger log = LoggerFactory.getLogger(PriseEnChargeServiceImpl.class);

    private final PriseEnChargeRepository priseEnChargeRepository;

    public PriseEnChargeServiceImpl(PriseEnChargeRepository priseEnChargeRepository) {
        this.priseEnChargeRepository = priseEnChargeRepository;
    }

    @Override
    public PriseEnCharge save(PriseEnCharge priseEnCharge) {
        log.debug("Request to save PriseEnCharge : {}", priseEnCharge);
        return priseEnChargeRepository.save(priseEnCharge);
    }

    @Override
    public Optional<PriseEnCharge> partialUpdate(PriseEnCharge priseEnCharge) {
        log.debug("Request to partially update PriseEnCharge : {}", priseEnCharge);

        return priseEnChargeRepository
            .findById(priseEnCharge.getId())
            .map(existingPriseEnCharge -> {
                if (priseEnCharge.getLibelle() != null) {
                    existingPriseEnCharge.setLibelle(priseEnCharge.getLibelle());
                }
                if (priseEnCharge.getMontantPC() != null) {
                    existingPriseEnCharge.setMontantPC(priseEnCharge.getMontantPC());
                }

                return existingPriseEnCharge;
            })
            .map(priseEnChargeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PriseEnCharge> findAll(Pageable pageable) {
        log.debug("Request to get all PriseEnCharges");
        return priseEnChargeRepository.findAll(pageable);
    }

    public Page<PriseEnCharge> findAllWithEagerRelationships(Pageable pageable) {
        return priseEnChargeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PriseEnCharge> findOne(Long id) {
        log.debug("Request to get PriseEnCharge : {}", id);
        return priseEnChargeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PriseEnCharge : {}", id);
        priseEnChargeRepository.deleteById(id);
    }
}
