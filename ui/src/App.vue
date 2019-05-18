<template>
  <v-app>
    <v-toolbar app>
      <v-toolbar-title class="headline">
        <span>IMDB</span>
        <span class="font-weight-light">DATASETS</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>

      <v-spacer></v-spacer>

      <v-toolbar-items>
        <v-menu bottom :disabled="!genresWithTitlesLoaded" offset-y >
          <template v-slot:activator="{ on }">
            <v-btn
                    flat
                    v-on="on"
            >
              Genres<v-icon>arrow_drop_down</v-icon>
            </v-btn>
          </template>
          <v-list class="scroll-y" style="max-height: 500px">
            <v-list-tile  v-for="item in genresWithTitlesArray"
                    @click="moveToGenre(item.genre)"
            >
              <v-list-tile-title>
                {{ item.genre }} ({{ item.titles }})
              </v-list-tile-title>
            </v-list-tile>
          </v-list>
        </v-menu>

        <v-btn flat to="/admin">
          ADMIN
        </v-btn>

        <v-btn icon to="/title-search">
          <v-icon>search</v-icon>
        </v-btn>

      </v-toolbar-items>
    </v-toolbar>

    <v-content>
      <router-view/>
    </v-content>
  </v-app>
</template>

<script>
  import HelloWorld from './components/LatestTitles'
  import {mapActions, mapState} from "vuex";

  export default {
    name: 'App',
    components: {
      HelloWorld
    },
    data() {
      return {
        //
      }
    },
    mounted() {
      this.loadGenresWithTitles();
    },

    computed: {
      ...mapState( {
        genresWithTitles: state => state.genresWithTitles
      }),

      genresWithTitlesArray: function () {
        let genresWithTitles = this.genresWithTitles;
        return Object
                .keys(genresWithTitles)
                .sort()
                .map(function (key) {
                  return {
                    genre: key,
                    titles: genresWithTitles[key]
                  };
                });
      },

      genresWithTitlesLoaded: function () {
        return Object.keys(this.genresWithTitles).length > 0;
      }
    },

    methods: {
      ...mapActions([
        'loadGenresWithTitles'
      ]),

      ...mapActions('titleSearch', [
        'searchGenre'
      ]),

      moveToGenre: function(genre) {
        this.searchGenre(genre);
        this.$router.push({
          name: 'title-search'
        })
      }
    }
  }
</script>
