<template>
  <v-container>
    <v-icon color = "black" size = "50">mdi-bookmark-multiple-outline</v-icon>
    <strong>{{ nickname }}</strong>님의 모아보기
    <p style = "display: inline">최대 5개의 모아보기가 가능합니다</p>
    <p class = "ml-3" style = "display: inline">{{ followings.length }} / 5</p>


    <div v-for = "(list,index) in followings" :key = "index">
      <v-chip
          class = "ml-1 float-left"
          close
          color = "info"
          label
          @click = "goProfile(list)"
          @click:close = "unFollow(list)">
        {{ list }}
      </v-chip>
    </div>

    <div id = "followProductListDiv" class = "row justify-center mt-15">
      <ul v-for = "(list,index) in productList" :key = "index"
          style = "list-style: none">
        <li id = "followListDiv">
          <div class = "p-5 mb-5 rounded float-left"
               style = "width: 300px; height: 400px; border: 2px solid green">
            <div class = "card-body">
              <span class = "mt-3" style = "margin-left: 130px"><strong>{{ list.title }}</strong></span>
              <br>
              <span class = "float-left card-subtitle">
              <router-link :to = "{path:'/profile',query:{nickname:list.nickname}}"><span
                  class = "float-left ml-3 mt-1 mr-3"><small>작성자 : {{ list.nickname }}</small></span></router-link>
              <br>
              <span
                  class = "float-left ml-3 mt-1 mr-3"> <small>작성일 : {{ displayedAt(list.createDate) }}</small></span>
              <br>
              <span class = "float-left ml-3 mt-1 mr-3"><small>지역 : {{ list.area }}</small></span>
              </span>
              <br>

              <div id = "followProductListImgDiv" style = "height: 100%; width: 100%">
                <router-link
                    :to = "{
                    path:'/productDetail',
                    query:{productId:list.id}
                  }">
                  <v-img
                      :src = "list.thumbnail"
                      class = "mt-15 mr-3 ml-13 grey lighten-3"
                      height = "200"
                      width = "200">

                  </v-img>
                </router-link>
                <div
                    v-if = "list.tags.length ===0 "
                    style = "height: 34px">
                </div>

                <div v-else id = "followProductTagDiv" class = "mt-5">
                  <v-row align-content = "center" justify = "center">
                    <div v-for = "(tags,index) in list.tags" :key = "index" style = "list-style: none; display: inline">
                      <v-chip
                          class = "ml-0 mr-1 pr-2 pl-2"
                          color = "info"
                          label
                          small>
                        {{ tags.tag }}
                      </v-chip>
                    </div>
                  </v-row>
                </div>

                <div id = "followProductListIconDiv" class = "mt-8">
                  <v-row align-content = "center" justify = "center">
                    <v-icon
                        color = "blue darken-4">
                      mdi-message-text
                    </v-icon>
                    <span class = "mt-1">{{ list.readCount }}</span>

                    <v-icon
                        color = "green">
                      mdi-image-multiple
                    </v-icon>
                    <span class = "mt-1">{{ list.productAlbumCount }}</span>

                    <v-btn
                        v-if = "list.like"
                        color = "pink"
                        dark
                        icon
                        @click = "pushLike(list.id)">

                      <v-icon dark>mdi-heart</v-icon>

                    </v-btn>

                    <v-btn
                        v-else
                        color = "grey"
                        dark
                        icon
                        @click = "pushLike(list.id)">

                      <v-icon dark>mdi-heart</v-icon>

                    </v-btn>
                  </v-row>
                </div>
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
  name    : "Follow",
  data() {
    return {
      toMember: '',
    }
  },
  methods : {
    goProfile(toMemberNickname) {
      console.log(toMemberNickname);
      this.$router.push({
        path : '/profile',
        query: {nickname: toMemberNickname}
      });

    },
    unFollow(toMember) {
      let data = {
        fromMember: this.nickname,
        toMember  : toMember
      };
      this.$store.dispatch('REQUEST_CANCEL_FOLLOW', data);
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
    async pushLike(productId) {
      await this.$store.dispatch('REQUEST_PUSH_INTEREST', productId);
      await this.$store.dispatch('REQUEST_GET_FOLLOWINGS_PRODUCT', this.nickname);
    }
  },
  computed: {
    nickname() {
      return this.$store.state.memberStore.nickname;
    },
    productList() {
      return this.$store.state.productStore.productList;
    },
    followings() {
      return this.$store.state.followStore.followings;
    }
  },
  created() {
  },
  mounted() {
    this.$store.dispatch('REQUEST_GET_FOLLOWINGS_PRODUCT', this.nickname);
    this.$store.dispatch('REQUEST_GET_FOLLOWINGS', this.nickname);
  }
}
</script>

<style scoped>

</style>