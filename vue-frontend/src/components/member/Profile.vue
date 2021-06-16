<template>
  <v-container>
    {{ clickFlag }}

    <v-btn
        v-if="originNickname === paramNickname">프로필 수정
    </v-btn>
    <hr>

    <div>
      <v-row align-content="center" justify="center">

        <router-link
            :to="{path:'/profile/myProduct',query:{nickname:paramNickname}}"
            style="text-decoration: none; color:black"
            @click.native="clickFlag=true">
          <v-icon color="green" size="60"> mdi-shopping</v-icon>
          <br>판매상품
        </router-link>

        <router-link
            :to="{path:'/profile/myLikeProduct',query:{nickname:paramNickname}}"
            style="text-decoration: none; color:black"
            @click.native="clickFlag=true">
          <v-icon color="pink" size="60">mdi-heart</v-icon>
          <br>관심상품
        </router-link>

        <router-link
            :to="{path:'/profile/board',query:{nickname:paramNickname}}"
            style="text-decoration: none; color:black"
            @click.native="clickFlag=true">
          <v-icon color="gray" size="60">mdi-clipboard-text-multiple-outline</v-icon>
          <br>동네생활
        </router-link>

        <router-link
            v-if="paramNickname === nickname"
            style="text-decoration: none; color:black"
            to="/profile/follow"
            @click.native="clickFlag=true">
          <v-icon color="black" size="60">mdi-bookmark-multiple-outline</v-icon>
          <br>모아보기
        </router-link>
        <v-btn
            v-else
            large
            icon>
          <v-icon color="black" class="mt-3" size="70" @click="addFollow">mdi-plus</v-icon>
        </v-btn>

        <router-link
            style="text-decoration: none; color:black"
            to="/profile/keyword"
            @click.native="clickFlag=true">
          <v-icon color="blue" size="60">mdi-lightbulb</v-icon>
          <br>키워드알림
        </router-link>

        <router-link
            :to="{path:'/profile/manner',query:{nickname:paramNickname}}"
            style="text-decoration: none; color:black"
            @click.native="clickFlag=true">
          <v-icon color="orange" size="60">mdi-face</v-icon>
          <br>매너평가
        </router-link>

        <router-link
            :to="{path:'/profile/review',query:{nickname:paramNickname}}"
            style="text-decoration: none; color:black"
            @click.native="clickFlag=true">
          <v-icon color="error" size="60">mdi-clipboard-multiple-outline</v-icon>
          <br>거래후기
        </router-link>

        <router-link
            :to="{path:'/qna',query:{nickname:paramNickname}}"
            style="text-decoration: none; color:black"
            @click.native="clickFlag=true">
          <v-icon color="yellow" size="60">mdi-lock-question</v-icon>
          <br>문의사항
        </router-link>
      </v-row>

    </div>

    <router-link
        v-if="clickFlag"
        :to="{path:'/profile',query:{nickname:paramNickname}}"
        style="text-decoration: none; color:black"
        @click.native="clickFlag=false">
      <v-icon color="error">mdi-close-circle</v-icon>
    </router-link>
    <router-view :key="$route.fullPath"></router-view>
  </v-container>

</template>

<script>
export default {
  name: "Profile",
  data() {
    return {
      paramNickname: '',
      originNickname: '',
      clickFlag: false
    }
  },
  methods: {
    addFollow() {
      let data = {
        fromMember: this.nickname,
        toMember: this.paramNickname
      };
      console.log(data.fromMember+" "+data.toMember)
      this.$store.dispatch('REQUEST_ADD_FOLLOW', data);
    }
  },
  computed: {
    nickname() {
      return this.$store.state.memberStore.nickname;
    }
  },
  created() {
    this.paramNickname = this.$route.query.nickname;
    this.originNickname = this.$store.state.memberStore.nickname;

    console.log(this.paramNickname+" "+this.originNickname);
  }
}
</script>

<style scoped>

</style>