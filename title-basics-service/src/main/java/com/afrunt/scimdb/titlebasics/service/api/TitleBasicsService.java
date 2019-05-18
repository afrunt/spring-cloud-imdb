package com.afrunt.scimdb.titlebasics.service.api;

import com.afrunt.imdb.model.TitleBasics;
import com.afrunt.scimdb.dto.PageResponse;
import com.afrunt.scimdb.titlebasics.dto.TitleBasicsSearchRequest;

import java.util.*;

/**
 * @author Andrii Frunt
 */
public interface TitleBasicsService {
    PageResponse<TitleBasics> search(TitleBasicsSearchRequest titleBasicsSearchRequest);

    Optional<TitleBasics> findById(long id);

    long count();

    long allGenresCount();

    Set<String> allGenres();

    List<TitleBasics> createTitlesBasics(Collection<TitleBasics> tbs);

    Map<String, Long> findGenresWithCountOfTitles();

    void deleteAll();
}
