<template>
    <v-card>
        <v-card-title>Title Basics Actions</v-card-title>
        <v-card-actions>
            <v-btn flat
                   :disabled="!stats.available || (stats.titles === 0 && stats.genres === 0) || actionInProgress"
                   @click="clear">Clear
            </v-btn>
        </v-card-actions>
    </v-card>
</template>
<script>

    import {titleBasicsService} from "../../services/TitleBasicsService";

    export default {
        props: {
            stats: {
                type: Object,
                required: true,
                default: () => ({
                    available: false
                })
            }
        },

        data: () => ({
            actionInProgress: false
        }),

        methods: {
            clear: function () {
                this.actionInProgress = true;

                titleBasicsService
                    .clear()
                    .finally(() => {
                        this.actionInProgress = false;
                        this.$emit("clear");
                    });
            }
        }


    }
</script>
