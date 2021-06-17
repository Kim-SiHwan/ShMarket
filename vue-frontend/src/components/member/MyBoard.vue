<template>
  <v-container>
    <v-icon color="gray" size="50">mdi-clipboard-text-multiple-outline</v-icon>
    <strong>{{ paramNickname }}</strong>님의 동네생활<br>

    <p><small v-if = "boardList">{{ boardList.length }}개의 동네생활이 있습니다</small></p>

    <div id="boardListDiv" class="row justify-center mt-15">
      <ul v-for="(list,index) in boardList" :key="index"
          style="list-style: none">
        <li id="listDiv">
          <div class="p-5 mb-5 rounded float-left"
               style="width: 300px; height: 500px; border: 2px solid green">
            <div class="card-body">
              <span class="mt-3" style="margin-left: 130px"><strong>{{ list.title }}</strong></span>
              <br>
              <span class="float-left card-subtitle">
              <router-link :to="{path:'/profile',query:{nickname:list.nickname}}"><span
                  class="float-left ml-3 mt-1 mr-3"><small>작성자 : {{ list.nickname }}</small></span></router-link>
              <br>
              <span class="float-left ml-3 mt-1 mr-3"> <small>작성일 : {{
                  displayedAt(list.updateDate)
                }}</small></span>
              <br>
              <span class="float-left ml-3 mt-1 mr-3"><small>지역 : {{ list.area }}</small></span>
              </span>
              <br>

              <div id="boardListImgDiv" style="height: 100%; width: 100%">
                <router-link :to="{path:'/boardDetail',query:{boardId:list.id}}">
                  <v-img
                      :src="list.thumbnail"
                      class="mt-15 mr-3 ml-13 grey lighten-3"
                      height="200"
                      width="200">

                  </v-img>
                  <v-textarea
                      background-color="white"
                      class="ml-3 mr-3 mt-5"
                      no-resize
                      outlined
                      readonly="readonly"
                      rows="2"
                      v-bind:value="list.content.substring(0,15)">

                  </v-textarea>
                </router-link>

              </div>
              <div class="mt-3">
                <v-row align-content="center" justify="center">

                  <v-chip
                      class="ml-0 mr-1 pr-2 pl-2"
                      color="info"
                      label
                      small>
                    {{ list.category }}
                  </v-chip>
                </v-row>
              </div>

              <div id="boardListIconDiv" class="mt-8">
                <v-row align-content="center" justify="center">
                  <v-icon
                      color="blue darken-4">
                    mdi-message-text
                  </v-icon>
                  {{ list.readCount }}

                  <v-icon
                      color="green">
                    mdi-image-multiple
                  </v-icon>
                  {{ list.boardAlbumCount }}

                  <v-icon
                      color="red">
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
  </v-container>
</template>

<script>
export default {
  name: "MyBoard",
  data() {
    return {
      paramNickname: ''
    }
  },
  methods: {
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
  computed: {
    boardList() {
      return this.$store.state.boardStore.boardList;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    }
  },
  created() {
    this.paramNickname = this.$route.query.nickname;
  },
  mounted() {
    this.$store.dispatch('REQUEST_GET_MY_BOARD', this.paramNickname);
  }
}
</script>

<style scoped>

</style>