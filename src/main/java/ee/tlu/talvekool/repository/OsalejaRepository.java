package ee.tlu.talvekool.repository;

import ee.tlu.talvekool.domain.Osaleja;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Osaleja entity.
 */
@SuppressWarnings("unused")
public interface OsalejaRepository extends JpaRepository<Osaleja,Long> {

}
