package com.afrunt.scimdb.web.controllers;

import com.afrunt.scimdb.web.clients.TitleBasicsServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Andrii Frunt
 */
@Component
public class CommonDataInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TitleBasicsServiceClient titleBasicsServiceClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        loadCommonData(modelAndView);

        super.postHandle(request, response, handler, modelAndView);
    }

    private void loadCommonData(ModelAndView modelAndView) {
        if (modelAndView == null) {
            return;
        }
        Map<String, Long> genresWithTitlesOriginal = titleBasicsServiceClient.findGenresWithTitleCount().getBody();

        Map<String, Long> sortedGenresWithTitles = new TreeMap<>();

        genresWithTitlesOriginal
                .keySet()
                .stream()
                .sorted()
                .forEach(k -> sortedGenresWithTitles.put(k, genresWithTitlesOriginal.get(k)));

        modelAndView.addObject("genresWithTitles", sortedGenresWithTitles);
    }
}
