import {titleBasicsService} from "../../services/TitleBasicsService";

let createDefaultPageRequest = () => ({
    page: 0,
    perPage: 10
});

export default {
    namespaced: true,

    state: {
        searchRequest: {},

        pageRequest: createDefaultPageRequest(),

        page: {
            page: 1,
            pages: 1,
            items: []
        },

        currentPage: 1,
        searchInProgress: false
    },

    mutations: {
        SET_SEARCH_REQUEST: function (state, searchRequest) {
            state.searchRequest = searchRequest;
        },

        SET_PAGE_REQUEST: function (state, pageRequest) {
            state.pageRequest = pageRequest;
        },

        SET_PAGE: function (state, page) {
            state.page = page;
        },

        SET_CURRENT_PAGE: function (state, currentPage) {
            state.currentPage = currentPage;
        },

        SET_SEARCH_IN_PROGRESS: function (state, searchInProgress) {
            state.searchInProgress = searchInProgress;
        }

    },

    actions: {
        searchFirstPage: function ({commit, state, dispatch}, searchRequest) {
            dispatch('search', {
                searchRequest,
                pageRequest: {
                    page: 0,
                    perPage: 10
                }
            });
        },

        search: function ({commit, state}, {searchRequest, pageRequest}) {
            commit('SET_SEARCH_IN_PROGRESS', true);
            let request = {...searchRequest, ...pageRequest};

            titleBasicsService
                .fetchSearchResults(request)
                .then(page => {
                    commit('SET_PAGE', page);
                    commit('SET_PAGE_REQUEST', {
                        page: page.page,
                        pages: page.pages,
                        perPage: page.perPage,
                        total: page.total
                    });

                    commit('SET_SEARCH_REQUEST', {...searchRequest});
                    commit('SET_CURRENT_PAGE', page.page + 1);
                })
                .finally(() => commit('SET_SEARCH_IN_PROGRESS', false));
        },

        changePage: function ({state, dispatch}, pageNumber) {

            dispatch('search', {
                searchRequest: state.searchRequest,
                pageRequest: {
                    ...state.pageRequest, ...{
                        page: pageNumber - 1
                    }
                }
            });
        },

        searchGenre: function ({dispatch}, genre) {
            dispatch('search', {
                searchRequest: {genre: genre},
                pageRequest: createDefaultPageRequest()
            });
        }
    }
};
