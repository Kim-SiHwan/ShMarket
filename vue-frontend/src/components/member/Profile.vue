<template>
  <v-container>

    <v-row justify = "center">
      <v-avatar
          size = "100">
        <v-img
            contain
            height = "300"
            src = "http://localhost:8080/api/board/download?fileName=default.png">
        </v-img>
      </v-avatar>
    </v-row>

    <v-row justify = "center">
      <div class = "mt-5">
        <p v-if = "paramNickname === nickname"> 아이디 : {{ username }}</p>
        <p> 닉네임 : {{ nickname }}</p>
        <v-btn
            v-if = "paramNickname === nickname"
            color = "green"
            dark
            rounded>
          프로필 수정
        </v-btn>
      </div>
    </v-row>

    <br>
    <hr>
    <div class = "mt-7">
      <v-row align-content = "center" justify = "center">
        <router-link
            :to = "{path:'/profile/myProduct',query:{nickname:paramNickname}}"
            class = "profileLink"
            style = "text-decoration: none; color:black">
          <v-icon color = "green" size = "60"> mdi-shopping</v-icon>
          <br>판매상품
        </router-link>

        <router-link
            v-if = "paramNickname === nickname"
            :to = "{path:'/profile/myLikeProduct',query:{nickname:paramNickname}}"
            class = "profileLink"
            style = "text-decoration: none; color:black">
          <v-icon color = "pink" size = "60">mdi-heart</v-icon>
          <br>관심상품
        </router-link>

        <router-link
            :to = "{path:'/profile/myBoard',query:{nickname:paramNickname}}"
            class = "profileLink"
            style = "text-decoration: none; color:black">
          <v-icon color = "gray" size = "60">mdi-clipboard-text-multiple-outline</v-icon>
          <br>동네생활
        </router-link>

        <router-link
            v-if = "paramNickname === nickname"
            :to = "{path:'/profile/follow',query:{nickname:nickname}}"
            class = "profileLink"
            style = "text-decoration: none; color:black">
          <v-icon color = "black" size = "60">mdi-bookmark-multiple-outline</v-icon>
          <br>모아보기
        </router-link>
        <router-link
            v-else
            :to = "{path:'/profile',query:{nickname:paramNickname}}"
            class = "profileLink"
            style = "text-decoration: none; color:black"
            @click.native = "addFollow">
          <v-icon color = "black" size = "60">mdi-plus</v-icon>
          <br>추가하기
        </router-link>


        <router-link
            v-if = "paramNickname === nickname"
            :to = "{path:'/profile/keyword',query:{nickname:nickname}}"
            class = "profileLink"
            style = "text-decoration: none; color:black">
          <v-icon color = "blue" size = "60">mdi-lightbulb</v-icon>
          <br>알림설정
        </router-link>

        <router-link
            :to = "{path:'/profile/manner',query:{nickname:paramNickname}}"
            class = "profileLink"
            style = "text-decoration: none; color:black">
          <v-icon color = "orange" size = "60">mdi-face</v-icon>
          <br>매너평가
        </router-link>

        <router-link
            :to = "{path:'/profile/review',query:{nickname:paramNickname}}"
            class = "profileLink"
            style = "text-decoration: none; color:black">
          <v-icon color = "error" size = "60">mdi-clipboard-multiple-outline</v-icon>
          <br>거래후기
        </router-link>

        <router-link
            :to = "{path:'/profile/qna',query:{nickname:paramNickname}}"
            class = "profileLink"
            style = "text-decoration: none; color:black">
          <v-icon color = "yellow" size = "60">mdi-lock-question</v-icon>
          <br>문의사항
        </router-link>
      </v-row>
    </div>

    <router-view></router-view>
  </v-container>

</template>

<script>
export default {
  name    : "Profile",
  data() {
    return {
      paramNickname : '',
      originNickname: '',
    }
  },
  methods : {
    addFollow() {
      let data = {
        fromMember: this.nickname,
        toMember  : this.paramNickname
      };
      this.$store.dispatch('REQUEST_ADD_FOLLOW', data);
    }
  },
  computed: {
    nickname() {
      return this.$store.state.memberStore.nickname;
    },
    username() {
      return this.$store.state.memberStore.username;
    }
  },
  created() {
    this.paramNickname = this.$route.query.nickname;
    this.originNickname = this.$store.state.memberStore.nickname;
  }
}
</script>

<style scoped>
.profileLink {
  padding: 15px;
}
</style>