package com.afrunt.scimdb.titlebasics.service;

import com.afrunt.imdb.model.TitleBasics;
import com.afrunt.scimdb.dto.titlebasics.TitleBasicsSearchRequest;
import com.afrunt.scimdb.titlebasics.model.TitleBasicsES;
import com.afrunt.scimdb.titlebasics.repository.TitleBasicsESRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import ma.glasnost.orika.MapperFacade;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.afrunt.scimdb.titlebasics.repository.TitleBasicsESRepository.*;

/**
 * @author Andrii Frunt
 */
@Service
public class TitleBasicsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TitleBasicsService.class);

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private TitleBasicsESRepository titleBasicsESRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    public Page<TitleBasics> search(TitleBasicsSearchRequest titleBasicsSearchRequest) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();

        builder.mustNot(QueryBuilders.matchPhraseQuery("titleType", "tvEpisode"));

        if (!StringUtils.isEmpty(titleBasicsSearchRequest.getTerm())) {
            builder.must(matchSearchTerm(titleBasicsSearchRequest.getTerm()));
        }

        if (titleBasicsSearchRequest.hasGenres()) {
            builder.must(beOfGenre(titleBasicsSearchRequest.getGenres().get(0)));
        }

        if (titleBasicsSearchRequest.getStartYear() != null) {
            builder.must(beOfYear(titleBasicsSearchRequest.getStartYear()));
        }

        PageRequest pageRequest = PageRequest.of(
                titleBasicsSearchRequest.getPage(),
                titleBasicsSearchRequest.getPerPage(),
                Sort.by(Sort.Order.desc("startYear").nullsLast())
        );

        return titleBasicsESRepository.search(builder, pageRequest)
                .map(tbe -> tbe.mapTo(TitleBasics.class, mapperFacade));
    }

    public Optional<TitleBasics> findById(long id) {

        return titleBasicsESRepository.findById(id)
                .map(tbe -> tbe.mapTo(TitleBasics.class, mapperFacade));
    }

    public long count() {
        return titleBasicsESRepository.count();
    }

    public long allGenresCount() {
        return allGenres().size();
    }

    public Set<String> allGenres() {
        return findGenresWithCountOfTitles().keySet().stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional
    public List<TitleBasics> createTitlesBasics(Collection<TitleBasics> tbs) {
        LOGGER.info("{} Title Basics received for creation", tbs.size());
        StopWatch sw = new StopWatch();
        sw.start();

        List<TitleBasics> titleBasicsToSave = onlyTitlesToSave(tbs);
        StopWatch esSw = new StopWatch();
        esSw.start();

        List<TitleBasics> saved = new ArrayList<>();

        if (!titleBasicsToSave.isEmpty()) {

            Iterable<TitleBasicsES> titleBasicsES = titleBasicsESRepository.saveAll(titleBasicsToSave.stream()
                    .map(tb -> mapperFacade.map(tb, TitleBasicsES.class))
                    .collect(Collectors.toList())
            );

            saved = StreamSupport.stream(titleBasicsES.spliterator(), false)
                    .map(tb -> tb.mapTo(TitleBasics.class, mapperFacade))
                    .collect(Collectors.toList());
        }

        esSw.stop();
        LOGGER.info("{} of {} ES title basics created  {}ms", titleBasicsToSave.size(), tbs.size(), esSw.getTotalTimeMillis());

        return saved;
    }

    public Map<String, Long> findGenresWithCountOfTitles() {
        StopWatch esSw = new StopWatch();
        esSw.start();

        Client client = elasticsearchTemplate.getClient();

        SearchResponse response = client.prepareSearch("title-basics")
                .setTypes("TitleBasics")
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(0)
                .addAggregation(
                        AggregationBuilders.terms("unique_genres")
                                .field("genres.keyword")
                                .size(100)
                )
                .execute()
                .actionGet();

        Map<String, Aggregation> results = response.getAggregations().asMap();
        StringTerms uniqueGenres = (StringTerms) results.get("unique_genres");
        Map<String, Long> genresWithCountOfTitles = uniqueGenres.getBuckets().stream().collect(Collectors.toMap(StringTerms.Bucket::getKeyAsString, InternalTerms.Bucket::getDocCount));
        esSw.stop();
        LOGGER.info("{} ES genres fetched in {}ms", genresWithCountOfTitles.size(), esSw.getTotalTimeMillis());

        return genresWithCountOfTitles;
    }

    private List<TitleBasics> onlyTitlesToSave(Collection<TitleBasics> tbs) {
        return new ArrayList<>(tbs);
    }

    public void deleteAll() {
        elasticsearchTemplate.deleteIndex("title-basics");
        elasticsearchTemplate.createIndex("title-basics");
    }
}
