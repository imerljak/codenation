package challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private QuoteRepository repository;

    @Override
    public Quote getQuote() {

        final int randomIndex = getRandomIndex(repository.count());

        final Page<Quote> page = repository.findAll(PageRequest.of(randomIndex, 1));

        if (page.hasContent()) {
            return page.getContent().get(0);
        }

        return null;
    }

    @Override
    public Quote getQuoteByActor(String actor) {

        int randomIndex = getRandomIndex(repository.countByActor(actor));

        final Page<Quote> page = repository.findAllByActor(actor, PageRequest.of(randomIndex, 1));

        if (page.hasContent()) {
            return page.getContent().get(0);
        }

        return null;
    }

    private int getRandomIndex(long max) {
        return (int) (Math.random() * max);
    }

}
