package ee.tlu.talvekool.service;

import ee.tlu.talvekool.domain.Osaleja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Osaleja.
 */
public interface OsalejaService {

    /**
     * Save a osaleja.
     *
     * @param osaleja the entity to save
     * @return the persisted entity
     */
    Osaleja save(Osaleja osaleja);

    /**
     *  Get all the osalejas.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Osaleja> findAll(Pageable pageable);

    /**
     *  Get the "id" osaleja.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Osaleja findOne(Long id);

    /**
     *  Delete the "id" osaleja.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the osaleja corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Osaleja> search(String query, Pageable pageable);
}
