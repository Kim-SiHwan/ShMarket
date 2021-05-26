<template>
  <v-container>
    <v-btn @click="test">test</v-btn><br>
    <v-btn @click="test2">test2</v-btn><br>
    <v-btn @click="test3">test3</v-btn><br>
    <v-btn @click="login1">Login Admin</v-btn>
    <v-btn @click="login2">Login User</v-btn>
    <v-btn @click="login3">Login Guest</v-btn>
    <router-link to="/join">join</router-link><br>
    <router-link to="/login">login</router-link><br>
    <router-link to="/addProduct">addProduct</router-link><br>
    <router-link to="/productList">productList</router-link><br>
    <router-link to="/addBoard">addBoard</router-link><br>
    <router-link to="/boardList">boardList</router-link><br>
    <router-link to="/keyword">keyword</router-link><br>

  </v-container>
</template>

<script>

export default {
  name: "Main",
  methods:{
    test(){
      localStorage.removeItem('access_token');
      console.log(localStorage.getItem('access_token'));
      console.log(sessionStorage.getItem('access_token'));
      console.log(this.$store.getters.isAuthenticated);
      console.log(this.$store.state.memberStore.nickname);
      console.log(sessionStorage.getItem('nickname'));
      console.log(localStorage.getItem('categories'));

      let c= JSON.parse(localStorage.getItem('categories'));
      for(let key in c){
        console.log(key);
      }
    },
    test2(){


      this.$store.commit('INIT_CATEGORIES');
    },
    test3(){
      localStorage.clear();
    },
    login1(){
      let loginDto = {
        username: 'admin',
        password: 'admin',
        fcmToken: this.fcmToken

      }
      this.$store.dispatch('REQUEST_LOGIN',loginDto);
    },
    login2(){
      let loginDto = {
        username: 'user',
        password: 'user',
        fcmToken: this.fcmToken
      }
      this.$store.dispatch('REQUEST_LOGIN',loginDto);
    },
    login3(){
      let loginDto = {
        username: 'guest',
        password: 'guest',
        fcmToken: this.fcmToken
      }
      this.$store.dispatch('REQUEST_LOGIN',loginDto);
    }
  },
  computed:{
    fcmToken(){
      return this.$store.state.memberStore.fcmToken;
    }
  }

}
</script>

<style scoped>

</style>