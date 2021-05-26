<template>
  <v-container>

    {{keywords}}
    <ul v-for="(list) in keywords" :key="list">
      <li>{{list}}</li>
    </ul>

    <v-text-field v-model="keywordRequestDto.keyword"></v-text-field>
    <v-btn @click="addKeyword">키워드 추가</v-btn>

  </v-container>

</template>

<script>
export default {
  name: "Keyword",
  data(){
    return{
      keywordRequestDto:{
        nickname:'',
        keyword:''
      }
    }
  },
  methods:{
    addKeyword(){
      this.keywordRequestDto.nickname = sessionStorage.getItem('nickname');

      this.$store.dispatch('REQUEST_ADD_KEYWORD',this.keywordRequestDto);
      this.getKeywords();
    },
    getKeywords(){
      this.$store.dispatch('REQUEST_GET_KEYWORDS');
    }
  },
  mounted() {
    this.getKeywords();
  },
  computed:{
    keywords(){
      return this.$store.state.pushStore.keywordList;
    }
  }
}
</script>

<style scoped>

</style>