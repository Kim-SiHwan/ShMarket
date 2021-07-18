<template>
  <v-container>
    <v-icon color = "blue" size = "50">mdi-lightbulb</v-icon>
    <strong>{{ nickname }}</strong>님의 키워드 알림<br>

    <p style = "display: inline"><strong>최대 10개의 키워드 설정이 가능합니다</strong></p>
    <p class = "ml-3" style = "display: inline">{{ keywords.length }} / 10</p>
    <div
        v-for = "(list,index) in keywords" :key = "index">
      <v-chip
          class = "mt-2"
          close
          color = "info"
          @click:close = "deleteKeyword(list.keyword)">
        {{ list.keyword }}
      </v-chip>
    </div>

    <v-text-field
        v-model = "keywordRequestDto.keyword"
        class = "mt-5"
        outlined
        v-on:keyup.enter = "addKeyword">

    </v-text-field>
    <v-btn
        class = "float-right"
        color = "success"
        @click = "addKeyword">키워드 추가
    </v-btn>

  </v-container>

</template>

<script>
export default {
  name    : "Keyword",
  data() {
    return {
      keywordRequestDto: {
        nickname: '',
        keyword : ''
      }
    }
  },
  methods : {
    addKeyword() {
      console.log(this.nickname);
      this.keywordRequestDto.nickname = this.nickname;
      this.$store.dispatch('REQUEST_ADD_KEYWORD', this.keywordRequestDto).then(() => {
        this.$store.dispatch('REQUEST_GET_KEYWORDS', this.nickname);
      });

      this.keywordRequestDto.keyword = '';
    },
    getKeywords() {
      this.$store.dispatch('REQUEST_GET_KEYWORDS', this.nickname);
    },
    deleteKeyword(keyword) {
      this.keywordRequestDto.nickname = this.nickname;
      this.keywordRequestDto.keyword = keyword;
      this.$store.dispatch('REQUEST_DELETE_KEYWORD', this.keywordRequestDto).then(() => {
        this.$store.dispatch('REQUEST_GET_KEYWORDS', this.nickname);
      });
      this.keywordRequestDto.keyword = '';
    }
  },
  computed: {
    keywords() {
      return this.$store.state.pushStore.keywordList;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    }
  },
  created() {},
  mounted() {
    this.getKeywords();
  }
}
</script>

<style scoped>

</style>