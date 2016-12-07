package ee.tlu.talvekool.repository.search;

import ee.tlu.talvekool.domain.Osaleja;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Osaleja entity.
 */
public interface OsalejaSearchRepository extends ElasticsearchRepository<Osaleja, Long> {
}
