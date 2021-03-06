package com.afrunt.scimdb.titlebasics.service.impl;

import com.afrunt.imdb.model.TitleBasics;
import com.afrunt.scimdb.dto.PageResponse;
import com.afrunt.scimdb.titlebasics.dto.TitleBasicsSearchRequest;
import com.afrunt.scimdb.titlebasics.model.TitleBasicsDocument;
import com.afrunt.scimdb.titlebasics.repository.TitleBasicsESRepository;
import com.afrunt.scimdb.titlebasics.service.api.TitleBasicsService;
import ma.glasnost.orika.MapperFacade;
import org.elasticsearch.action.search.SearchRequestBuilder;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.afrunt.scimdb.titlebasics.repository.TitleBasicsESRepository.*;

/**
 * @author Andrii Frunt
 */
@Service
public class TitleBasicsServiceES implements TitleBasicsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TitleBasicsServiceES.class);

    @Value("${spring.elasticsearch.rest.uris}")
    private String elasticSearchRestUrl;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private TitleBasicsESRepository titleBasicsESRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing Title Basics Service");
        initIndex();
    }

    @Override
    public PageResponse<TitleBasics> search(TitleBasicsSearchRequest titleBasicsSearchRequest) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();

        builder.mustNot(QueryBuilders.matchPhraseQuery("titleType", "tvEpisode"));

        if (!StringUtils.isEmpty(titleBasicsSearchRequest.getKeywords())) {
            builder.must(matchSearchTerm(titleBasicsSearchRequest.getKeywords()));
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

        Page<TitleBasics> page = titleBasicsESRepository.search(builder, pageRequest)
                .map(tbe -> tbe.mapTo(TitleBasics.class, mapperFacade));

        return new PageResponse<TitleBasics>()
                .setPage(page.getNumber())
                .setPerPage(page.getSize())
                .setPages(page.getTotalPages())
                .setTotal(page.getTotalElements())
                .setItems(page.getContent());
    }

    @Override
    public Optional<TitleBasics> findById(long id) {
        return titleBasicsESRepository.findById(id)
                .map(tbe -> tbe.mapTo(TitleBasics.class, mapperFacade));
    }

    @Override
    public long count() {
        return titleBasicsESRepository.count();
    }

    @Override
    public long allGenresCount() {
        return allGenres().size();
    }

    @Override
    public Set<String> allGenres() {
        return findGenresWithCountOfTitles().keySet().stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public List<TitleBasics> createTitlesBasics(Collection<TitleBasics> tbs) {
        LOGGER.info("{} Title Basics received for creation", tbs.size());
        StopWatch sw = new StopWatch();
        sw.start();

        List<TitleBasics> titleBasicsToSave = new ArrayList<>(tbs);
        StopWatch esSw = new StopWatch();
        esSw.start();

        List<TitleBasics> saved = new ArrayList<>();

        if (!titleBasicsToSave.isEmpty()) {

            Iterable<TitleBasicsDocument> titleBasicsES = titleBasicsESRepository.saveAll(titleBasicsToSave.stream()
                    .map(tb -> mapperFacade.map(tb, TitleBasicsDocument.class))
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

    @Override
    public Map<String, Long> findGenresWithCountOfTitles() {
        StopWatch esSw = new StopWatch();
        esSw.start();

        Client client = elasticsearchTemplate.getClient();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("title-basics")
                .setTypes("TitleBasics")
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(0)
                .addAggregation(
                        AggregationBuilders.terms("unique_genres")
                                .field("genres.keyword")
                                .size(100)
                );

        try {
            SearchResponse response = searchRequestBuilder
                    .execute()
                    .actionGet();

            Map<String, Aggregation> results = response.getAggregations().asMap();
            StringTerms uniqueGenres = (StringTerms) results.get("unique_genres");
            Map<String, Long> genresWithCountOfTitles = uniqueGenres.getBuckets().stream().collect(Collectors.toMap(StringTerms.Bucket::getKeyAsString, InternalTerms.Bucket::getDocCount));
            esSw.stop();
            LOGGER.debug("{} ES genres fetched in {}ms", genresWithCountOfTitles.size(), esSw.getTotalTimeMillis());

            return genresWithCountOfTitles;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    @Override
    public void deleteAll() {
        elasticsearchTemplate.deleteIndex("title-basics");
        initIndex();

    }

    private void fixMaxResultWindow() {
        String url = (elasticSearchRestUrl.startsWith("http") ? "" : "http://")
                + elasticSearchRestUrl + "/_all/_settings?preserve_existing=true";
        restTemplate
                .put(url, Map.of("index.max_result_window", "2147483647"));
    }

    private void initIndex() {
        if (!elasticsearchTemplate.indexExists("title-basics")) {
            elasticsearchTemplate.createIndex("title-basics");
            LOGGER.info("Index title-basics created");
        }

        fixMaxResultWindow();

        if (!elasticsearchTemplate.typeExists("title-basics", "TitleBasics")) {
            TitleBasicsDocument saved = titleBasicsESRepository.save(
                    new TitleBasicsDocument()
                            .setTitleId(0L)
                            .setGenres(Arrays.asList("temp-genre-1", "temp-genre-2"))
            );

            titleBasicsESRepository.delete(saved);
            LOGGER.info("TitleBasics type created");
        }
    }
}
