<template>
  <v-app id = "headerDiv">
    <v-switch
        class = "ml-15"
        color = "orange"
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
            color = "orange"
            hide-details
            input-value = "true"
            v-on:change = "checked(name)">

        </v-checkbox>

        <v-checkbox
            v-else
            :label = "name"
            class = "ml-15"
            color = "orange"
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
          <div class = "p-5 mb-5 rounded float-left"
               style = "width: 300px; height: 500px; border: 2px solid green">
            <div class = "card-body">
              <span class = "mt-3" style = "margin-left: 130px"><strong>{{ list.title }}</strong></span>
              <br>
              <span class = "float-left card-subtitle">
              <router-link :to = "{path:'/profile',query:{nickname:list.nickname}}"><span
                  class = "float-left ml-3 mt-1 mr-3"><small>작성자 : {{ list.nickname }}</small></span></router-link>
              <br>
              <span class = "float-left ml-3 mt-1 mr-3">
                <small>
                작성일 : {{ displayedAt(list.updateDate) }}
                </small>
              </span>
              <br>
              <span class = "float-left ml-3 mt-1 mr-3"><small>지역 : {{ list.area }}</small></span>
              </span>
              <br>

              <div id = "boardListImgDiv" style = "height: 100%; width: 100%">
                <router-link :to = "{path:'/boardDetail',query:{boardId:list.id}}">
                  <v-img
                      :src = "list.thumbnail"
                      class = "mt-15 mr-3 ml-13 grey lighten-3"
                      height = "200"
                      width = "200">

                  </v-img>
                  <v-textarea

                      background-color = "white"
                      class = "ml-3 mr-3 mt-5"
                      no-resize
                      outlined
                      readonly = "readonly"
                      rows = "2"
                      v-bind:value = "list.content">

                  </v-textarea>
                </router-link>

              </div>
              <div class = "mt-3">
                <v-row align-content = "center" justify = "center">

                  <v-chip
                      class = "ml-0 mr-1 pr-2 pl-2"
                      color = "info"
                      label
                      small
                      @click = "clickTag(list.category)">
                    {{ list.category }}
                  </v-chip>
                </v-row>
              </div>

              <div id = "boardListIconDiv" class = "mt-8">
                <v-row align-content = "center" justify = "center">

                  <v-icon
                      color = "green">
                    mdi-image-multiple
                  </v-icon>
                  {{ list.boardAlbumCount }}

                  <v-icon
                      color = "red">
                    mdi-message-text
                  </v-icon>

                  {{ list.commentCount }}
                </v-row>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <v-pagination
        v-model = "page"
        @input="showBoardPage(page)"
        total-visible="10"
        :length = "totalPage">

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

export default {
  name      : "boardList",
  components: {
  },
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
    showBoardPage(page){
      console.log("page : "+page);
      this.$store.commit('SET_BOARD_CURRENT_PAGE', page);
      let data = {
        page    : page,
        category: localStorage.getItem('boardCategories')
      };
      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_PAGES', data);

    },
    checked(name) {
      this.categories[name] = !this.categories[name];
      localStorage.setItem('boardCategories', JSON.stringify(this.categories));
      this.$store.commit('SET_BOARD_CURRENT_PAGE',1);
      this.page=1;
      let data = {
        page    : 1,
        category: localStorage.getItem('boardCategories')
      }
      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_PAGES', data);
    },
    changeCategoryFlag() {
      this.showCategoryFlag = !this.showCategoryFlag;
    },
    clickTag(name) {
      this.tagName = name;
      this.clickTagFlag = true;
      let toJson = {};
      toJson[name] = true;
      this.$store.commit('SET_BOARD_CURRENT_PAGE',1);
      this.page=1;
      let data = {
        page    : 1,
        category: JSON.stringify(toJson)
      };

      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_PAGES', data);
    },
    closeTag() {
      this.clickTagFlag = false;
      this.$store.commit('SET_BOARD_CURRENT_PAGE',1);
      this.page=1;
      this.showBoardList();
    },
    displayedAt(createdAt) {
      createdAt = new Date(createdAt);
      const milliSeconds = new Date() - createdAt
      const seconds = milliSeconds / 1000
      if (seconds < 60) {
        return `방금 전`
      }
      const minutes = seconds / 60
      if (minutes < 60) {
        return `${Math.floor(minutes)}분 전`
      }
      const hours = minutes / 60
      if (hours < 24) {
        return `${Math.floor(hours)}시간 전`
      }
      const days = hours / 24
      if (days < 7) {
        return `${Math.floor(days)}일 전`
      }
      const weeks = days / 7
      if (weeks < 5) {
        return `${Math.floor(weeks)}주 전`
      }
      const months = days / 30
      if (months < 12) {
        return `${Math.floor(months)}개월 전`
      }
      const years = days / 365
      return `${Math.floor(years)}년 전`
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