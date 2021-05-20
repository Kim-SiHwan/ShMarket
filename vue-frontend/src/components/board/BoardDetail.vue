<template>
  <v-container>

    <div style="text-align: center">
      <h3>{{ boardDetail.title }}</h3>
    </div>
    <div>
      <span><small>판매자 : {{ boardDetail.nickname }}</small></span><br>
      <span><small>지역 : {{ boardDetail.area }}</small></span><br>
      <span><small>작성일 : {{ time }}</small></span><br>

    </div>

    <v-carousel
        height="500"
        hide-delimiter-background>

      <v-carousel-item
          v-for="(item,i) in boardDetail.boardAlbums"
          :key="i">
        <v-img :src="item.url" contain max-height="500"></v-img>

      </v-carousel-item>
    </v-carousel>
    <div style="text-align: center; margin-top: 10px">

      <v-btn
          v-if="flag"
          color="orange"
          class="white--text"
          @click="changeShowImages">접어두기
      </v-btn>
      <v-btn
          v-else
          color="orange"
          class="white--text"
          @click="changeShowImages">펼쳐보기
      </v-btn>

    </div>
    <v-row
        v-if="flag"
        class="mt-5"
        justify="center">
      <v-col
          v-for="(file,index) in boardDetail.boardAlbums" :key="index"
          class="d-flex child-flex"
          cols="3">
        <div id="boardImgDiv">
          <v-img
              :src="file.url"
              aspect-ratio="1.2"
              contain
              width="500">
          </v-img>
        </div>
      </v-col>
    </v-row>
    <v-textarea
        v-if="boardDetail"
        background-color="white"
        class="mt-10"
        no-resize
        outlined
        readonly="readonly"
        v-bind:rows="boardDetail.content.length/5"
        v-bind:value="boardDetail.content"
    ></v-textarea>
  </v-container>

</template>

<script>
export default {
  name: "boardDetail",
  data() {
    return {
      flag: false,
      time: ''

    }
  },

  methods: {
    changeShowImages() {
      this.flag = !this.flag;
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
    }
  },
  computed: {
    boardDetail() {
      return this.$store.state.boardStore.boardDetail;
    },
  },
  created() {
    this.$store.dispatch('REQUEST_GET_BOARD', this.$route.query.boardId);
    this.time = this.displayedAt(this.boardDetail.updateDate);
  }
}
</script>

<style scoped>

</style>