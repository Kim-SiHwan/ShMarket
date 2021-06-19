<template>
  <v-container>
    <v-icon color = "black" size = "50">mdi-bookmark-multiple-outline</v-icon>
    <strong>{{ nickname }}</strong>님의 차단목록
    <p style = "display: inline">사용자 차단은 최대 5명까지 가능합니다.</p>
    <p class = "ml-3" style = "display: inline">{{ blocks.length }} / 5</p>


    <div v-for = "(list,index) in blocks" :key = "index">
      <v-chip
          class = "ml-1 float-left"
          close
          color = "info"
          label
          @click = "goProfile(list)"
          @click:close = "deleteBlock(list.id)">
        {{ list.toNickname }}
      </v-chip>
    </div>

  </v-container>
</template>

<script>
export default {
  name: "Block",
  data(){
    return{

    }
  },
  methods:{
    goProfile(toNickname){
      this.$router.push({
        path : '/profile',
        query: {nickname: toNickname}
      });

    },
    deleteBlock(blockId){
      this.$store.dispatch('REQUEST_DELETE_BLOCK', blockId);
    }
  },
  computed:{
    nickname(){
      return this.$store.state.memberStore.nickname;
    },
    blocks(){
      return this.$store.state.memberStore.blocks;
    }
  },
  created() {
  },
  mounted() {
    this.$store.dispatch('REQUEST_GET_BLOCKS', this.nickname);
  }
}
</script>

<style scoped>

</style>