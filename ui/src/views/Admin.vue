<template>
    <v-container fluid>
        <v-layout row wrap>

            <v-flex xs12>
                <v-subheader class="pl-0">Refresh Timeout</v-subheader>
                <v-slider
                        v-model="interval"
                        thumb-label="always"
                        max="30"
                ></v-slider>
            </v-flex>

            <template v-for="service in services">
                <v-flex xs12 md6>
                    <ServiceStatsPanel :service="service" :stats="stats[service]" style="margin: 5px"/>
                </v-flex>
            </template>

            <v-flex xs12 md6>
                <TitleBasicsActionsPanel :stats="stats['title-basics']" @clear="updateStats" style="margin: 5px"/>
            </v-flex>

            <v-flex xs12 md6>
                <CrawlerActionsPanel :stats="stats['crawler-service']" @start="updateStats" @stop="updateStats"
                                     style="margin: 5px"/>
            </v-flex>

        </v-layout>
    </v-container>
</template>

<script>
    import ServiceStatsPanel from "../components/admin/ServiceStatsPanel";
    import CrawlerActionsPanel from "../components/admin/CrawlerActionsPanel";
    import TitleBasicsActionsPanel from "../components/admin/TitleBasicsActionsPanel";
    import {mapActions, mapState} from "vuex";

    export default {
        components: {
            ServiceStatsPanel, CrawlerActionsPanel, TitleBasicsActionsPanel
        },

        computed: {
            ...mapState("stats", [
                "stats"
            ])
        },

        watch: {
            interval: function (current) {
                if (current===0) {
                    this.destroyTimer();
                } else {
                    this.createTimer();
                }
            }
        },

        methods: {
            ...mapActions("stats", [
                "loadStats"
            ]),

            updateStats: function () {
                if (this.loading) {
                    return;
                }

                this.loading = true;
                let loaded = 0;

                const handleLoaded = () => {
                    if (++loaded === this.services.length) {
                        this.loading = false;
                    }
                };

                this.services
                    .map(service => this.loadStats(service))
                    .map(promise => promise.finally(() => handleLoaded()))
                ;
            },

            createTimer: function () {
                this.destroyTimer();

                this.timer = setInterval(this.updateStats, this.interval * 1000);
            },

            destroyTimer: function () {
                if (this.timer) {
                    clearInterval(this.timer);
                    this.timer = null;
                }
            }
        },

        data: () => ({
            services: ["title-basics", "crawler-service"],
            timer: null,
            loading: false,
            interval: 5

        }),

        mounted() {
            this.updateStats();
            this.createTimer();
        },

        beforeDestroy() {
            this.destroyTimer();
        }

    }
</script>
