<template>
    <v-alert style="margin: 20px"
            :value="true"
            color="error"
            icon="warning"
            outline
    >
        <template v-if="$route.params.message">
            {{$route.params.message}}
        </template>

        <template v-else>
            404
        </template>
    </v-alert>
</template>
