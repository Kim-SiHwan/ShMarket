<template>
  <v-container>
    <div style = "text-align: center">
      <h3>{{ qnaDetail.title }}</h3>
      <hr>
      <p>작성자 : {{ qnaDetail.nickname }}</p>
      <p>작성일 : {{ qnaDetail.createDate.substring(0, 10) }}</p>
      <v-textarea
          :value = "qnaDetail.content"
          no-resize
          outlined>

      </v-textarea>
      <v-btn
          v-if = "qnaDetail.nickname === nickname"
          color = "error"
          style = "margin-top: -20px"
          @click = "deleteQna">삭제하기
      </v-btn>
    </div>

    <hr class = "mt-3">
    <div class = "mt-3" style = "text-align: center">
      <h3 v-if = "!qnaDetail.answered"> 아직 답변이 없습니다.</h3>
      <v-textarea
          v-else
          :value = "qnaDetail.answer"
          outlined>

      </v-textarea>
    </div>

  </v-container>
</template>

<script>
export default {
  name    : "QnaDetail",
  data() {
    return {
      qnaId: ''
    }
  },
  methods : {
    deleteQna() {
      this.$store.dispatch('REQUEST_DELETE_QNA', this.qnaId);
    }
  },
  computed: {
    qnaDetail() {
      return this.$store.state.qnaStore.qnaDetail;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    }
  },
  created() {
    this.qnaId = this.$route.query.qnaId;
  },
  mounted() {
    this.$store.dispatch('REQUEST_GET_QNA', this.qnaId);
  }
}
</script>

<style scoped>

</style>