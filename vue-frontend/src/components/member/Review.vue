<template>
  <v-container class = "mt-7">
    <v-icon color = "red" size = "50">mdi-clipboard-multiple-outline</v-icon>
    <strong>{{ paramNickname }}</strong>님의 거래 후기
    <v-data-table
        v-if = "reviews"
        id = "reviewTable"
        :items = "reviews"
        :items-per-page = "itemsPerPage"
        :page.sync = "page"
        class = "elevation-1"
        hide-default-footer
        hide-default-header
        no-data-text = "첫 후기를 작성해보세요!"
        @page-count = "pageCount= $event">

      <template v-slot:item = "reviews">
        <tr>
          <div
              class = "rounded"
              style = "background-color: darkseagreen; clear:both; height: 25px">
            <p class = "float-left ml-3" style = "display: inline">{{ reviews.item.buyer }}</p>
          </div>
          <div style = " height: 30px; clear: both;">
            <p class = "float-left ml-3" style = "white-space: pre-wrap; word-break: break-all">
              {{ reviews.item.content }}</p>
          </div>
        </tr>
      </template>
    </v-data-table>

    <v-pagination
        v-model = "page"
        :length = "pageCount">

    </v-pagination>
    <v-row justify = "center">
      <v-dialog v-model = "dialog" max-width = "450" persistent>
        <v-card>
          <v-card-title class = "headline"><small>거래후기</small></v-card-title>

          <v-textarea
              v-model = "content"
              class = "ml-3 mr-3"
              label = "내용 입력"
              no-resize
              outlined
              style = "white-space: pre-wrap">

          </v-textarea>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                color = "primary"
                @click = "addReview">후기등록
            </v-btn>
            <v-btn
                color = "error"
                @click = "dialog = false">취소하기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>

    <v-fab-transition>
      <v-btn
          v-if = "paramNickname !== nickname"
          color = "error"
          dark
          fab
          fixed
          right
          small
          style = "bottom: 80px;"
          @click = "dialog=true">
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </v-fab-transition>
  </v-container>

</template>

<script>
export default {
  name    : "Review",
  data() {
    return {
      paramNickname: '',
      dialog       : '',
      itemsPerPage : 8,
      pageCount    : 0,
      page         : 1,
      content      : ''
    }
  },
  methods : {
    addReview() {
      let requestData = {
        nickname: this.paramNickname,
        buyer   : this.nickname,
        content : this.content
      };

      this.$store.dispatch('REQUEST_ADD_REVIEW', requestData).then(()=>{
        this.$store.dispatch('REQUEST_GET_REVIEWS', this.nickname);
      });
      this.content = '';
      this.dialog = false;
    }
  },
  computed: {
    reviews() {
      return this.$store.state.memberStore.reviews;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    }
  },
  created() {
    this.paramNickname = this.$route.query.nickname;
  },
  mounted() {
    this.$store.dispatch('REQUEST_GET_REVIEWS', this.paramNickname);
  }
}
</script>

<style scoped>

</style>