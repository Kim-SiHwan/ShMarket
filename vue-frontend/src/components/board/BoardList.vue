<template>
  <v-app id = "headerDiv">
    <v-switch
        class = "ml-15"
        color = "green"
        hide-details
        label = "카테고리 필터"
        v-on:change = "changeCategoryFlag">

    </v-switch>

    <div v-if = "showCategoryFlag">
      <div v-for = "(flag,name) in categories" :key = "name">

        <v-checkbox
            v-if = "flag"
            :label = "name"
            class = "ml-15"
            color = "green"
            hide-details
            input-value = "true"
            v-on:change = "checked(name)">

        </v-checkbox>

        <v-checkbox
            v-else
            :label = "name"
            class = "ml-15"
            color = "green"
            hide-details
            v-on:change = "checked(name)">

        </v-checkbox>
      </div>
    </div>

    <div style = "margin: auto">
      <v-chip
          v-if = "clickTagFlag"
          close
          color = "info"
          label
          style = "width: 320px"
          @click:close = "closeTag">
        {{ tagName }} 카테고리 게시글만 출력합니다
      </v-chip>
    </div>


    <div id = "boardListDiv" class = "row justify-center mt-15">
      <ul v-for = "(list,index) in boardList" :key = "index"
          style = "list-style: none">
        <li id = "listDiv">
          <div
              class = "p-5 mb-5 rounded float-left"
              style = "width: 400px; height: 210px; border: 2px solid green">
            <div class = "card-body">

              <v-row justify = "center">
                <span class = "mt-3"><strong>{{ list.title }}</strong></span>
              </v-row>

              <span class = "float-left card-subtitle">
                <span class = "float-left ml-1 mt-1 ">
                  <small>
                    <v-chip
                        dark
                        color = "grey"
                        label
                        small
                        @click = "clickTag(list.category)">
                      {{ list.category }}
                    </v-chip>
                  </small>
                </span>
                <br>
              </span>

              <div style = "clear: both"></div>

              <div id = "boardListImgDiv" class = "mt-2 ml-1">
                <router-link :to = "{path:'/boardDetail',query:{boardId:list.id}}">
                  <v-img
                      :src = "list.thumbnail"
                      class = "grey lighten-3 float-left"
                      height = "94"
                      width = "100">
                  </v-img>
                  <v-textarea
                      class = "ml-2 float-left"
                      no-resize
                      outlined
                      readonly = "readonly"
                      rows = "3"
                      style = "width: 280px; height: 100px"
                      v-bind:value = "list.content">
                  </v-textarea>
                </router-link>
              </div>

              <div id = "boardInfoDiv" style = "margin-top: -20px">
                <router-link :to = "{path:'/profile',query:{nickname:list.nickname}}">
                  <span class = "float-left ml-3 mr-3" style="color: darkgreen">
                      {{ list.nickname }}
                  </span>
                </router-link>

                <span class = "float-left ml-3 mr-3">
                  <small>
                    {{ list.area }}
                  </small>
                </span>

                <span class = "float-right ml-3 mr-3">
                  <small>
                   {{ displayedAt(list.updateDate) }}
                  </small>
                </span>

              </div>

              <div style = "clear: both"></div>

              <hr>
              <div id = "boardListIconDiv" class = "mt-1 ml-2">
                <v-icon
                    color = "orange">
                  mdi-message-text
                </v-icon>
                {{ list.commentCount }}

                <v-icon
                    class = "ml-4"
                    color = "green">
                  mdi-image-multiple
                </v-icon>
                {{ list.boardAlbumCount }}

              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <v-pagination
        v-model = "page"
        :length = "totalPage"
        total-visible = "10"
        @input = "showBoardPage(page)">

    </v-pagination>

    <v-fab-transition>
      <v-btn
          bottom
          color = "green"
          dark
          fab
          fixed
          right
          small
          @click = "$vuetify.goTo('#headerDiv')">
        <v-icon>mdi-chevron-double-up</v-icon>
      </v-btn>
    </v-fab-transition>

    <v-fab-transition>
      <v-btn
          color = "green"
          dark
          fab
          fixed
          right
          small
          style = "bottom: 80px;"
          @click = "addBoard">
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </v-fab-transition>
  </v-app>
</template>

<script>
import {mixinData} from "@/mixin/mixins";

export default {
  name      : "boardList",
  mixins:[mixinData],
  data() {
    return {
      showCategoryFlag: false,
      clickTagFlag    : false,
      tagName         : '',
      page            : 1
    }
  },
  methods   : {
    addBoard() {
      this.$router.push('/addBoard');
    },
    showBoardList() {
      let data = {
        page    : this.currentPage,
        category: localStorage.getItem('boardCategories')
      };
      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_PAGES', data);
    },
    showBoardPage(page) {
      console.log("page : " + page);
      this.$store.commit('SET_BOARD_CURRENT_PAGE', page);
      let data = {
        page    : page,
        category: localStorage.getItem('boardCategories')
      };
      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_PAGES', data);

    },
    checked(name) {
      this.categories[name] = !this.categories[name];
      this.page = 1;
      this.changeCategories('board', this.categories);
   /*   localStorage.setItem('boardCategories', JSON.stringify(this.categories));
      this.$store.commit('SET_BOARD_CURRENT_PAGE', 1);
      let data = {
        page    : 1,
        category: localStorage.getItem('boardCategories')
      }
      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_PAGES', data);*/
    },
    changeCategoryFlag() {
      this.showCategoryFlag = !this.showCategoryFlag;
    },
    clickTag(name) {
      this.tagName = name;
      this.clickTagFlag = true;
      let toJson = {};
      toJson[name] = true;
      this.$store.commit('SET_BOARD_CURRENT_PAGE', 1);
      this.page = 1;
      let data = {
        page    : 1,
        category: JSON.stringify(toJson)
      };

      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_PAGES', data);
    },
    closeTag() {
      this.clickTagFlag = false;
      this.$store.commit('SET_BOARD_CURRENT_PAGE', 1);
      this.page = 1;
      this.showBoardList();
    },
  },
  computed  : {
    boardList() {
      return this.$store.state.boardStore.boardList;
    },
    categories() {
      return JSON.parse(localStorage.getItem('boardCategories'));
    },
    totalPage() {
      return this.$store.state.boardStore.boardTotalPage;
    },
    currentPage() {
      return this.$store.state.boardStore.boardCurrentPage;
    }
  },
  created() {
    this.page = this.currentPage;
  },
  mounted() {
    this.showBoardList();
  }
}
</script>

<style scoped>

small {
  color: black;
}

</style>