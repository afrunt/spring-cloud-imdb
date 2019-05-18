<template>
    <v-card v-if="titleBasics">
        <v-card-title primary-title>
            <div>
                <h3 class="headline mb-0">
                    {{ titleBasics.primaryTitle }}
                    <template
                            v-if="titleBasics.originalTitle && titleBasics.originalTitle !== titleBasics.primaryTitle">
                        ({{titleBasics.originalTitle}})
                    </template>
                </h3>
                <div>
                    {{ titleBasics.startYear }} {{titleBasics.endYear ? ' - ' + titleBasics.endYear : ''}}<i>{{titleBasics.genres.join(",")}}</i>
                </div>
            </div>
        </v-card-title>
    </v-card>
</template>
<script>
    import {titleBasicsService} from "../services/TitleBasicsService";

    export default {
        data: () => ({
            titleBasics: null
        }),

        mounted() {
            titleBasicsService
                .fetchTitleBasicsById(this.$route.params.id)
                .then(titleBasics => this.titleBasics = titleBasics)
                .catch(error=>this.$router.push({
                    name: '404', params: {
                        message: `Title ${this.$route.params.id} not found`
                    }
                }))

            ;
        }
    }
</script>
