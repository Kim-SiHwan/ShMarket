<template>
  <v-app>
    <v-container>
      <v-row align = "center" class = "fill-height; mt-15" justify = "center">
        <div class = " elevation-15" style = "width: 600px; height: 600px">
          <h3 class = "text-center pt-15 py-3 black--text">
            로그인
          </h3>

          <v-form ref = "form" class = "pa-15 text-center">

            <v-text-field v-model = "username" class = " pl-3 pr-3" label = "아이디를 입력해주세요."
                          prepend-icon = "mdi-account" required type = "text">
            </v-text-field>

            <v-text-field v-model = "password" class = "pt-10 pl-3 pr-3" label = "비밀번호를 입력해주세요."
                          prepend-icon = "mdi-lock" required type = "password"
                          v-on:keyup.enter = "login">
            </v-text-field>

            <v-btn
                color = "primary"
                depressed
                large
                @click = "login">
              로그인
            </v-btn>
          </v-form>
        </div>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>

export default {
  name    : "Login",
  data() {
    return {
      username: '',
      password: '',
      loginDto: ''
    }
  },
  methods : {
    login() {
      this.loginDto = {
        username: this.username,
        password: this.password,
        fcmToken: localStorage.getItem('fcmToken')
      };
      this.$store.dispatch('REQUEST_LOGIN', this.loginDto);
    }
  },
  computed: {
    nickname() {
      return this.$store.state.memberStore.nickname;
    }
  },
  created() {
  },
  mounted() {
  }
}
</script>

<style scoped>

</style>