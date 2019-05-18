<template>
    <v-card>
        <v-card-title>
            {{ service }}
        </v-card-title>
        <v-card-text>
            <v-treeview :items="statsTree"></v-treeview>
        </v-card-text>
    </v-card>
</template>

<script>

    export default {
        props: {
            service: {
                type: String,
                required: true
            },

            stats: {
                type: Object,
                required: true,
                default: () => ({
                    available: false
                })
            }
        },

        computed: {
            statsTree: function () {
                return this.convertStatsToTree();
            }
        },

        data: () => ({}),

        mounted() {

        },

        methods: {
            convertStatsToTree: function () {
                return this.convertStatsObject(this.stats);
            },

            convertStatsObject: function (o, id) {
                if (!id) {
                    let idVar = 0;

                    id = () => idVar++;
                }

                const isPrimitive = v => v !== Object(v);

                return Object
                    .keys(o)
                    .map(key => {
                        let value = o[key];

                        if (isPrimitive(value)) {
                            return {
                                id: id(),
                                name: `${key}: ${value}`
                            }
                        } else {
                            return {
                                id: id(),
                                name: key,
                                children: this.convertStatsObject(value, id)
                            }
                        }
                    });
            }
        }
    }
</script>
