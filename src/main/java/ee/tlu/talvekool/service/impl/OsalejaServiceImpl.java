package ee.tlu.talvekool.service.impl;

import ee.tlu.talvekool.service.OsalejaService;
import ee.tlu.talvekool.domain.Osaleja;
import ee.tlu.talvekool.repository.OsalejaRepository;
import ee.tlu.talvekool.repository.search.OsalejaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Osaleja.
 */
@Service
@Transactional
public class OsalejaServiceImpl implements OsalejaService{

    private final Logger log = LoggerFactory.getLogger(OsalejaServiceImpl.class);
    
    @Inject
    private OsalejaRepository osalejaRepository;

    @Inject
    private OsalejaSearchRepository osalejaSearchRepository;

    /**
     * Save a osaleja.
     *
     * @param osaleja the entity to save
     * @return the persisted entity
     */
    public Osaleja save(Osaleja osaleja) {
        log.debug("Request to save Osaleja : {}", osaleja);
        Osaleja result = osalejaRepository.save(osaleja);
        osalejaSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the osalejas.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Osaleja> findAll(Pageable pageable) {
        log.debug("Request to get all Osalejas");
        Page<Osaleja> result = osalejaRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one osaleja by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Osaleja findOne(Long id) {
        log.debug("Request to get Osaleja : {}", id);
        Osaleja osaleja = osalejaRepository.findOne(id);
        return osaleja;
    }

    /**
     *  Delete the  osaleja by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Osaleja : {}", id);
        osalejaRepository.delete(id);
        osalejaSearchRepository.delete(id);
    }

    /**
     * Search for the osaleja corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Osaleja> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Osalejas for query {}", query);
        Page<Osaleja> result = osalejaSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
