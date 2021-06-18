<template>
  <v-container>
    <div v-for = "(item,index) in chatRooms" :key = "index" style = "width: 400px">

      <router-link
          :to = "{path:'/productDetail',query:{productId: item.productId}}"
          style = "text-decoration: none; color:black">
        <div id = "productInfoDiv">
          <v-avatar>
            <v-img
            :src="item.productThumbnail">
            </v-img>
          </v-avatar>
          <br>
          상품 제목 : {{ item.productTitle }}
        </div>
        <p v-if = "item.sender === nickname">{{ item.receiver }}님과의 대화</p>
        <p v-else>{{ item.sender }}님과의 대화</p>
      </router-link>

      <div
          id = "chatRoomInfo"
          @click = "getReceiver(item.sender, item.receiver, item.roomId, item.productId)">
        <v-badge
            v-if = "item.notRead !== 0"
            :content = "item.notRead"
            class = "float-right"
            color = "red"
            overlap>

        </v-badge>
        <v-text-field
            :value = "item.lastMessage"
            outlined
            readonly>

        </v-text-field>
      </div>

    </div>
  </v-container>
</template>

<script>
export default {
  name    : "ChatRooms",
  data() {
    return {
      receiver: ''
    }
  },
  methods : {
    getReceiver(name1, name2, roomId, productId) {
      if (this.nickname === name1) {
        this.receiver = name2;
      } else {
        this.receiver = name1;
      }

      this.goChatRoom(roomId, productId);
    },
    goChatRoom(roomId, productId) {
      this.$router.push({
        path : '/chat',
        query: {
          roomId   : roomId,
          productId: productId,
          sender   : this.nickname,
          receiver : this.receiver
        }
      });
    }
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