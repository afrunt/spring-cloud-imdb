<template>
    <v-form v-model="valid">
        <v-container fluid>
            <v-layout row wrap>
                <v-flex
                        xs12
                        md4
                >
                    <v-text-field
                            v-model="searchRequest.keywords"
                            label="Keywords"
                            @keyup.enter="search"
                            clearable
                    ></v-text-field>
                </v-flex>

                <v-flex
                        xs12
                        md4
                >
                    <v-select
                            v-model="searchRequest.genre"
                            :items="genres"
                            :disabled="genres.length < 1"

                            clearable
                            label="Genre"
                    ></v-select>
                </v-flex>

                <v-flex
                        xs12
                        md4
                >
                    <v-text-field
                            v-model="searchRequest.startYear"
                            type="number"
                            label="Year"
                            clearable
                            @keyup.enter="search"
                    ></v-text-field>
                </v-flex>

                <v-flex xs12>
                    <v-btn color="success" :disabled="genres.length <= 0 || !canSearch"
                           @click="search">Search
                    </v-btn>
                </v-flex>
            </v-layout>
        </v-container>
    </v-form>
</template>

<script>
    import {mapGetters} from "vuex";

    export default {
        props: {
            searchRequest: {
                type: Object,
                default: () => ({})
            },

            canSearch: {
                type: Boolean,
                default: true
            }
        },

        data: () => ({
            valid: true
        }),

        methods: {
            search: function() {
                this.$emit("search", this.searchRequest);
            }
        },

        computed: {
            ...mapGetters([
                'genres'
            ])
        },

    }
</script>
