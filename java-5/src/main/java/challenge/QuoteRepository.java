package challenge;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {

    Page<Quote> findAllByActor(String actor, Pageable pageable);

    Long countByActor(String actor);

}
