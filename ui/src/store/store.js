import Vue from 'vue'
import Vuex from 'vuex'
import titleSearchModule from './modules/TitleSearch';
import statsModule from './modules/Stats';
import {titleBasicsService} from "../services/TitleBasicsService";

Vue.use(Vuex);

export default new Vuex.Store({

    modules: {
        titleSearch: titleSearchModule,
        stats: statsModule
    },

    state: {
        title: 'imdb-datasets',
        genresWithTitles: {}
    },

    mutations: {
        SET_GENRES_WITH_TITLES: function (state, genresWithTitles) {
            state.genresWithTitles = genresWithTitles;
        }
    },

    actions: {
        loadGenresWithTitles: function ({state, commit}) {
            if (Object.keys(state.genresWithTitles).length < 1) {
                return titleBasicsService
                    .fetchGenresWithTitles()
                    .then(genresWithTitles => commit("SET_GENRES_WITH_TITLES", genresWithTitles));
            }
        }
    },

    getters: {
        genres: (state) => {
            return Object
                .keys(state.genresWithTitles)
                .sort();
        }
    }

})
