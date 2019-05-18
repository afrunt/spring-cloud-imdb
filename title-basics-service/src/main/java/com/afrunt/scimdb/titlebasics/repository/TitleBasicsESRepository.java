package com.afrunt.scimdb.titlebasics.repository;

import com.afrunt.scimdb.titlebasics.model.TitleBasicsES;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * @author Andrii Frunt
 */
@Repository
public interface TitleBasicsESRepository extends ElasticsearchRepository<TitleBasicsES, Long> {
    static QueryBuilder matchSearchTerm(String searchTerm) {
        return QueryBuilders.matchPhraseQuery("primaryTitle", searchTerm);
    }

    static QueryBuilder beOfYear(Integer startYear) {
        return termQuery("startYear", startYear);
    }

    static QueryBuilder beOfGenre(String genre) {
        return QueryBuilders.matchQuery("genres", genre);
    }

}
