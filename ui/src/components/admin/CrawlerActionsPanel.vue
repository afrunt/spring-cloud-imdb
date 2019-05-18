<template>
    <v-card>
        <v-card-title>Crawler Actions</v-card-title>
        <v-card-actions>
            <v-btn flat :disabled="!stats.available || stats.running || actionInProgress" @click="start">Start</v-btn>
            <v-btn flat :disabled="!stats.available || !stats.running || actionInProgress" @click="stop">Stop</v-btn>
        </v-card-actions>
    </v-card>
</template>
<script>

    import {crawlerService} from "../../services/CrawlerService";

    export default {
        props: {
            stats: {
                type: Object,
                required: true,
                default: () => ({
                    available: false,
                    running: false
                })
            }
        },

        data: () => ({
            actionInProgress: false
        }),

        methods: {
            start: function () {
                this.actionInProgress = true;

                crawlerService
                    .start()
                    .finally(() => {
                        this.actionInProgress = false;
                        this.$emit("start");
                    });
            },

            stop: function () {
                this.actionInProgress = true;
                crawlerService
                    .stop()
                    .finally(() => {
                        this.actionInProgress = false;
                        this.$emit("stop");
                    });
            }
        }


    }
</script>
