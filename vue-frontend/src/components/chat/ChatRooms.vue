<template>
  <v-container>
    <div v-for="(item,index) in chatRooms" :key="index" style="width: 400px">

      <router-link :to="{path:'/productDetail',query:item.productId}"
                   style="text-decoration: none; color:black">
        <div id="productInfoDiv">
          {{ item.productTitle }}
        </div>
        <p v-if="item.sender === nickname">{{ item.receiver }}님과의 대화</p>
        <p v-else>{{ item.sender }}님과의 대화</p></router-link>

      <router-link :to="{path:'/chat',query:{roomId:item.roomId,productId:item.productId}}">
        <v-badge
            v-if="item.notRead !== 0"
            :content="item.notRead"
            class="float-right"
            color="red"
            overlap>

        </v-badge>
        <v-text-field
            :value="item.lastMessage"
            outlined
            readonly>

        </v-text-field>
      </router-link>
    </div>
  </v-container>
</template>

<script>
export default {
  name: "ChatRooms",
  data() {
    return {}
  },
  computed: {
    chatRooms() {
      return this.$store.state.chatStore.chatRooms;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    }
  },
  mounted() {
    this.$store.dispatch('REQUEST_GET_CHAT_ROOMS', this.nickname);
  }
}
</script>

<style scoped>

</style>