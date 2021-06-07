<template>
  <v-app>
    <v-app-bar
        app
        color="primary"
        dark
    >
      <div class="d-flex align-center">
        <v-img
            alt="Vuetify Logo"
            class="shrink mr-2"
            contain
            src="https://cdn.vuetifyjs.com/images/logos/vuetify-logo-dark.png"
            transition="scale-transition"
            width="40"
        />

        <v-img
            alt="Vuetify Name"
            class="shrink mt-1 hidden-sm-and-down"
            contain
            min-width="100"
            src="https://cdn.vuetifyjs.com/images/logos/vuetify-name-dark.png"
            width="100"
        />
      </div>

      <v-spacer></v-spacer>

      <v-btn
          href="https://github.com/vuetifyjs/vuetify/releases/latest"
          target="_blank"
          text
      >
        <span class="mr-2">Latest Release</span>
        <v-icon>mdi-open-in-new</v-icon>
      </v-btn>
    </v-app-bar>

    <v-main>
      {{ fcmToken }}
      <router-view></router-view>
    </v-main>

    <v-snackbar
        v-model="snackBarInfo.open"
        :color="snackBarInfo.color"
        :right="snackBarInfo.right"
        :timeout="snackBarInfo.timeout"
        top>
      {{ snackBarInfo.text }}
      <v-btn
          v-if="snackBarInfo.right"
          color="blue"
          small
          @click="clickSnackBar"
      >
        이동
      </v-btn>
    </v-snackbar>

  </v-app>
</template>

<script>

import fcm from './util/fcm'

export default {
  name: 'App',

  components: {},
  methods: {
    clickSnackBar() {
      console.log("?!@?#~!~");

      if (this.snackBarInfo.type === 1) {
        console.log('타입 1')
        this.$router.push({
          path: '/productDetail',
          query: {productId: this.snackBarInfo.productId}
        });
      } else {
        console.log('타입2 ')
        this.$router.push({
          path: '/chat',
          query: {
            roomId: this.snackBarInfo.roomId,
            productId: this.snackBarInfo.productId,
            sender: this.snackBarInfo.sender,
            receiver: this.snackBarInfo.receiver
          }
        });
      }

    }
  },
  computed: {
    snackBarInfo() {
      return this.$store.state.commonStore.snackBar;
    },
    fcmToken() {
      return this.$store.state.memberStore.fcmToken;
    }
  },
  mounted() {
    console.log(fcm.getData());
  },
  data: () => ({
    //
  }),
};
</script>
