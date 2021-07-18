<template>
  <v-container>
    <v-icon color = "gray" size = "50">mdi-clipboard-text-multiple-outline</v-icon>
    <strong>{{ paramNickname }}</strong>님의 동네생활<br>

    <div id = "myBoardListDiv" class = "row justify-center mt-15">
      <ul v-for = "(list,index) in myBoardList" :key = "index"
          style = "list-style: none">
        <li id = "myBoardList">
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
                        color = "info"
                        label
                        small>
                      {{ list.category }}
                    </v-chip>
                  </small>
                </span>
                <br>
              </span>

              <div style = "clear: both"></div>

              <div id = "myBoardListImgDiv" class = "mt-2 ml-1">
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

              <div id = "myBoardInfoDiv" style = "margin-top: -20px">
                <router-link :to = "{path:'/profile',query:{nickname:list.nickname}}">
                  <span class = "float-left ml-3 mr-3" style = "color: darkgreen">
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
              <div id = "myBoardListIconDiv" class = "mt-1 ml-2">
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
        :length = "myBoardTotalPage"
        total-visible = "10"
        @input = "showMyBoardPage(page)">

    </v-pagination>

  </v-container>
</template>

<script>
import {mixinData} from "@/mixin/mixins";

export default {
  name    : "MyBoard",
  mixins  : [mixinData],
  data() {
    return {
      paramNickname: '',
      page         : 1,
    }
  },
  methods : {
    showBoardByNickname() {
      let data = {
        page    : this.myBoardCurrentPage - 1,
        nickname: this.paramNickname
      }
      this.$store.dispatch('REQUEST_GET_MY_BOARD', data);
    },
    showMyBoardPage(page) {
      console.log("page : " + page);
      this.$store.commit('SET_MY_BOARD_CURRENT_PAGE', page);
      let data = {
        page    : page - 1,
        nickname: this.paramNickname
      };
      this.$store.dispatch('REQUEST_GET_MY_BOARD', data);
    }
  },
  computed: {
    myBoardList() {
      return this.$store.state.boardStore.myBoardList;
    },
    myBoardCurrentPage() {
      return this.$store.state.boardStore.myBoardCurrentPage;
    },
    myBoardTotalPage() {
      return this.$store.state.boardStore.myBoardTotalPage;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    }
  },
  created() {
    this.paramNickname = this.$route.query.nickname;
    this.page = this.myBoardCurrentPage;

  },
  mounted() {
    this.showBoardByNickname();
  }
}
</script>

<style scoped>

</style>