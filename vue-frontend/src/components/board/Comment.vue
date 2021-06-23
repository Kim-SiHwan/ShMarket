<template>
  <v-app>
    <v-container id = "commentContainer">

      <v-icon
          color = "blue darken-2">
        mdi-message-text
      </v-icon>

      <p v-if = "commentList">
        {{ commentList.length }}개의 댓글이 달렸습니다
      </p>

      <v-data-table
          v-if = "commentList"
          id = "commentDiv"
          :items = "commentList"
          :items-per-page = "itemsPerPage"
          :page.sync = "page"
          class = "elevation-1"
          hide-default-footer
          hide-default-header
          no-data-text = "첫 댓글을 작성해보세요!"
          @page-count = "pageCount= $event">

        <template v-slot:item = "commentList">
          <tr>
            <div
                class = "rounded"
                style = "background-color: darkseagreen; clear:both; height: 25px">
              <p class = "float-left ml-3" style = "display: inline">{{ commentList.item.nickname }}</p>

              <p class = "float-right mr-3"><small>{{ displayedAt(commentList.item.updateDate) }}</small></p>
              <div
                  v-if = "commentList.item.nickname === nickname"
                  class = "float-right mr-2">
                <v-icon
                    class = "mr-2"
                    color = "blue"
                    small
                    @click = "updateCommentForm(
                      commentData={
                      id:commentList.item.id,
                      content:commentList.item.content,
                      nickname:commentList.item.nickname
                      }
                      )">
                  mdi-pencil
                </v-icon>

                <v-icon
                    color = "red"
                    small
                    @click = "deleteComment(commentList.item.id,commentList.item.nickname)">
                  mdi-delete
                </v-icon>
              </div>

            </div>
            <div style = " height: 30px; clear: both; margin-top: -10px ">
              <p class = "float-left ml-3" style = "white-space: pre-wrap; word-break: break-all">
                {{ commentList.item.content }}</p>
            </div>
          </tr>
        </template>
      </v-data-table>

      <v-pagination
          v-model = "page"
          :length = "pageCount">

      </v-pagination>


      <div style = "clear:both"></div>
      <div
          id = "addCommentDiv">
        <v-textarea
            v-model = "requestData.content"
            label = "Content"
            no-resize
            outlined
            rows = "3"
            style = "white-space: pre-wrap">

        </v-textarea>

        <v-btn
            class = "float-right"
            color = "primary"
            @click = "addComment">
          <v-icon dark>
            mdi-pencil
          </v-icon>
        </v-btn>
      </div>


      <v-row justify = "center">
        <v-dialog v-model = "dialog" max-width = "450" persistent>
          <v-card>
            <v-card-title class = "headline"><small>댓글수정</small></v-card-title>
            <v-textarea
                v-model = "updateContent"
                label = "수정할 내용을 입력해주세요."
                no-resize
                outlined
                rows = "4"
                style = "width: 90%; margin-left: 17px"
                v-bind:placeholder = "updateForm.content">

            </v-textarea>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                  color = "primary"
                  @click = "updateComment(commentId)">수정하기
              </v-btn>
              <v-btn
                  color = "error"
                  @click = "dialog = false">취소하기
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
export default {
  name    : "Comment",
  data() {
    return {
      requestData  : {
        boardId : '',
        nickname: '',
        content : ''
      },
      updateContent: '',
      dialog       : false,
      itemsPerPage : 8,
      pageCount    : 0,
      page         : 1,

      updateForm: {
        id     : '',
        content: '',
      },
      commentId : ''
    }
  },
  methods : {

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
    deleteComment(commentId, nickname) {
      if (this.username !== 'admin4166' && this.nickname !== nickname) {
        this.$store.commit('SET_SNACK_BAR',
            {msg: '작성자만 삭제 할 수 있습니다.', color: 'error'}
        );
        return false;
      }
      let deleteData = {
        commentId: commentId,
        boardId  : this.boardDetail.id
      };
      this.$store.dispatch('REQUEST_DELETE_COMMENT', deleteData);
    },
    updateCommentForm(commentData) {
      if (this.nickname !== commentData.nickname) {
        this.$store.commit('SET_SNACK_BAR',
            {msg: '작성자만 수정 할 수 있습니다.', color: 'error'}
        );
        return false;
      }
      this.commentId = commentData.id;
      this.dialog = true;
    },
    updateComment(commentId) {
      this.updateForm = {
        id     : commentId,
        content: this.updateContent,
        boardId: this.boardDetail.id
      };
      this.$store.dispatch('REQUEST_UPDATE_COMMENT', this.updateForm);
      this.dialog = false;

    },
    getComments() {
      this.$store.dispatch('REQUEST_GET_COMMENTS_BY_BOARD_ID', this.$route.query.boardId);
    },
    addComment() {
      if (!this.isAuthenticated) {
        this.$store.commit('SET_SNACK_BAR',
            {msg: '로그인이 필요합니다.', color: 'error'}
        );
        return false;
      }
      this.requestData.nickname = this.nickname;
      this.requestData.boardId = this.boardDetail.id;
      this.$store.dispatch('REQUEST_ADD_COMMENT', this.requestData);
      this.requestData.content = '';

    }
  },
  created() {
  },
  mounted() {
    this.getComments();
  },
  computed: {
    boardDetail() {
      return this.$store.state.boardStore.boardDetail;
    },
    commentList() {
      return this.$store.state.commentStore.commentList;
    },
    nickname() {
      return sessionStorage.getItem('nickname');
    },
    isAuthenticated() {
      return this.$store.getters.isAuthenticated;
    },

  }
}
</script>

<style scoped>
#commentContainer {
  overflow: auto;
}

</style>