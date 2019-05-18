import {statsService} from "../../services/StatsService";

export default {
    namespaced: true,

    state: {
        stats: {}
    },

    mutations: {
        ADD_STATS: function (state, payload) {
            let stats = {...state.stats};

            stats[payload.serviceId] = payload.stats;

            state.stats = stats;
        }
    },

    actions: {
        loadStats: function ({commit}, serviceId) {
            statsService
                .fetchStats(serviceId)
                .then(stats => commit("ADD_STATS", {
                    serviceId: serviceId,
                    stats: stats
                }));
        }
    }
}
