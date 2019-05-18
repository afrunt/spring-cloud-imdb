<template>
    <div>
        <v-card>
            <TitleSearchForm @search="searchFirstPage" :search-request="searchRequest" :can-search="!searchInProgress"></TitleSearchForm>
        </v-card>

        <v-list v-if="page.items && page.items.length > 0">
            <template v-for="(item, index) in page.items">
                <v-list-tile
                        :key="item.titleId"
                        @click="moveToTitle(item.titleId)"
                >

                    <v-list-tile-content>
                        <v-list-tile-title>

                                {{ item.primaryTitle }}
                                <template v-if="item.originalTitle && item.primaryTitle !== item.originalTitle">
                                    ({{ item.originalTitle }})
                                </template>


                        </v-list-tile-title>
                        <v-list-tile-sub-title>{{ item.startYear }} {{item.endYear ? ' - ' + item.endYear : ''}}<i>{{
                            item.genres.join(",") }}</i>
                        </v-list-tile-sub-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-divider v-if="index < page.items.length - 1"></v-divider>
            </template>
        </v-list>

        <div class="text-xs-center" v-if="page.pages > 1">
            <v-pagination
                    :value="currentPage"
                    @input="changePage"
                    :length="page.pages"
                    total-visible="5"
                    :disabled="searchInProgress"

            ></v-pagination>
        </div>

    </div>
</template>
<script>
    import TitleSearchForm from '../components/TitleSearchForm';
    import {titleBasicsService} from "../services/TitleBasicsService";
    import {mapActions, mapState} from "vuex";

    export default {
        components: {
            TitleSearchForm
        },

        computed: {
            ...mapState('titleSearch', {
                searchRequest: state => state.searchRequest,
                pageRequest: state => state.pageRequest,
                page: state => state.page,
                searchInProgress: state => state.searchInProgress,
                currentPage: state => state.currentPage
            })
        },

        data: () => ({

        }),

        methods: {
            ...mapActions('titleSearch', [
                'search', 'changePage', 'searchFirstPage'
            ]),

            moveToTitle: function(id) {
                this.$router.push(`/title/${id}`);
            }
        }
    }
</script>
