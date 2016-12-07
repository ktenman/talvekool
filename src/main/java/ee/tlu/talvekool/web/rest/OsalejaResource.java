package ee.tlu.talvekool.web.rest;

import com.codahale.metrics.annotation.Timed;
import ee.tlu.talvekool.domain.Osaleja;
import ee.tlu.talvekool.service.OsalejaService;
import ee.tlu.talvekool.web.rest.util.HeaderUtil;
import ee.tlu.talvekool.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Osaleja.
 */
@RestController
@RequestMapping("/api")
public class OsalejaResource {

    private final Logger log = LoggerFactory.getLogger(OsalejaResource.class);
        
    @Inject
    private OsalejaService osalejaService;

    /**
     * POST  /osalejas : Create a new osaleja.
     *
     * @param osaleja the osaleja to create
     * @return the ResponseEntity with status 201 (Created) and with body the new osaleja, or with status 400 (Bad Request) if the osaleja has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/osalejas")
    @Timed
    public ResponseEntity<Osaleja> createOsaleja(@Valid @RequestBody Osaleja osaleja) throws URISyntaxException {
        log.debug("REST request to save Osaleja : {}", osaleja);
        if (osaleja.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("osaleja", "idexists", "A new osaleja cannot already have an ID")).body(null);
        }
        Osaleja result = osalejaService.save(osaleja);
        return ResponseEntity.created(new URI("/api/osalejas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("osaleja", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /osalejas : Updates an existing osaleja.
     *
     * @param osaleja the osaleja to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated osaleja,
     * or with status 400 (Bad Request) if the osaleja is not valid,
     * or with status 500 (Internal Server Error) if the osaleja couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/osalejas")
    @Timed
    public ResponseEntity<Osaleja> updateOsaleja(@Valid @RequestBody Osaleja osaleja) throws URISyntaxException {
        log.debug("REST request to update Osaleja : {}", osaleja);
        if (osaleja.getId() == null) {
            return createOsaleja(osaleja);
        }
        Osaleja result = osalejaService.save(osaleja);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("osaleja", osaleja.getId().toString()))
            .body(result);
    }

    /**
     * GET  /osalejas : get all the osalejas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of osalejas in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/osalejas")
    @Timed
    public ResponseEntity<List<Osaleja>> getAllOsalejas(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Osalejas");
        Page<Osaleja> page = osalejaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/osalejas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /osalejas/:id : get the "id" osaleja.
     *
     * @param id the id of the osaleja to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the osaleja, or with status 404 (Not Found)
     */
    @GetMapping("/osalejas/{id}")
    @Timed
    public ResponseEntity<Osaleja> getOsaleja(@PathVariable Long id) {
        log.debug("REST request to get Osaleja : {}", id);
        Osaleja osaleja = osalejaService.findOne(id);
        return Optional.ofNullable(osaleja)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /osalejas/:id : delete the "id" osaleja.
     *
     * @param id the id of the osaleja to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/osalejas/{id}")
    @Timed
    public ResponseEntity<Void> deleteOsaleja(@PathVariable Long id) {
        log.debug("REST request to delete Osaleja : {}", id);
        osalejaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("osaleja", id.toString())).build();
    }

    /**
     * SEARCH  /_search/osalejas?query=:query : search for the osaleja corresponding
     * to the query.
     *
     * @param query the query of the osaleja search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/osalejas")
    @Timed
    public ResponseEntity<List<Osaleja>> searchOsalejas(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Osalejas for query {}", query);
        Page<Osaleja> page = osalejaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/osalejas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
