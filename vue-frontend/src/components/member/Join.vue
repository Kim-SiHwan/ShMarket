<template>
  <v-app>
    <v-container>
      <v-row align = "center" class = "fill-height; mt-15" justify = "center">
        <div class = " elevation-15" style = "width: 600px; height: 800px">
          <h3 class = "text-center pt-15 py-3 black--text">
            회 원 가 입
          </h3>

          <v-form ref = "form" class = "pa-15 text-center">

            <v-text-field v-model = "member.username" :rules = "rules.nameRules" class = "pt-0 pl-3 pr-3"
                          label = "아이디를 입력해주세요."
                          prepend-icon = "mdi-account" required type = "text">
            </v-text-field>

            <v-text-field v-model = "member.nickname" :rules = "rules.nameRules" class = "pt-10 pl-3 pr-3"
                          label = "닉네임을 입력해주세요."
                          prepend-icon = "mdi-account" required type = "text">
            </v-text-field>

            <v-text-field v-model = "member.password" :rules = "rules.passwordRules" class = "pt-10 pl-3 pr-3"
                          label = "비밀번호를 입력해주세요."
                          prepend-icon = "mdi-lock" required type = "password">
            </v-text-field>

            <v-text-field :rules = "checkPasswordRules" class = "pt-10 pl-3 pr-3" label = "비밀번호를 다시 입력해주세요."
                          prepend-icon = "mdi-lock"
                          required type = "password"
                          @keyup.enter = "requestJoin">
            </v-text-field>


            <v-text-field v-model = "selectedArea" class = "pt-10 pl-3 pr-3" label = "거주지 ( 동 )을 입력해주세요."
                          prepend-icon = "mdi-home"
                          v-on:keyup.enter = "findArea">
            </v-text-field>
            <v-btn
                class = "float-right"
                color = "info"
                x-small>검색
            </v-btn>
            <v-select
                v-if = "areaFlag"
                v-model = "member.area"
                :items = "areasInfo"
                class = "pt-3 pl-3 pr-3 "
                dense
                hide-details
                item-text = "address"
                label = "거주지 ( 동 )"
                no-data-text = "거주지를 입력해주세요."
                style = "width: 500px">

            </v-select>
            <br>
            <v-btn
                class = "mt-5"
                color = "primary"
                depressed
                large
                @click = "requestJoin">
              가입하기
            </v-btn>
          </v-form>
        </div>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
export default {
  name    : "Join",
  data() {
    return {
      areaFlag    : false,
      member      : {
        username: '',
        nickname: '',
        password: '',
        area    : ''

      },
      selectedArea: '',
      rules       : {
        emailRules   : [],
        nameRules    : [],
        passwordRules: []

      },

      checkPasswordRules: [
        v => v === this.member.password || '비밀번호가 다릅니다.'
      ]
    }
  },
  methods : {
    requestJoin() {
      this.$store.dispatch('REQUEST_JOIN', this.member);
    },
    findArea() {
      this.areaFlag = true;
      this.$store.dispatch('REQUEST_AREAS', this.selectedArea);
    },
    setRules() {

      this.rules.passwordRules = [
        v => !!v || '비밀번호를 입력해주세요.',
        v => v.length <= 13 || '12자 이내로 작성해주세요.',
        v => v.search(/\s/) === -1 || '공백을 제거해주세요.'
      ],
          this.rules.nameRules = [
            v => !!v || '아이디를 입력해주세요.',
            v => v.length <= 13 || '12자 이내로 작성해주세요.',
            v => v.search(/\s/) === -1 || '공백을 제거해주세요.'

          ]
    }
  },
  computed: {
    areasInfo() {
      return this.$store.state.areaStore.areas;
    }
  },
  created() {
    this.setRules();
  },
  mounted() {}

}
</script>

<style scoped>

</style>