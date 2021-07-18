<template>
  <v-container>
    <h3>{{ this.nickname }}님의 채팅 목록입니다.</h3>
    <hr>
    <div
        v-for = "(item,index) in chatRooms"
        :key = "index"
        class = "mt-5"
        style = "width: 400px">

      <router-link
          :to = "{path:'/productDetail',query:{productId: item.productId}}"
          style = "text-decoration: none; color:black">
        <div id = "productInfoDiv" class = "float-left">
          <v-avatar size = "90" class="mt-1">
            <v-img
                :src = "item.productThumbnail">
            </v-img>
          </v-avatar>
          <div class = "float-right">
            <p v-if = "item.sender === nickname">{{ item.receiver }}님과의 대화</p>
            <p v-else><b>{{ item.sender }}</b>님과의 대화</p>
            <p>상품 제목 : {{ item.productTitle }}</p>
            <small>
              {{ item.updateDate.substring(0, 4) }}년
              {{ item.updateDate.substring(5, 7) }}월
              {{ item.updateDate.substring(8, 10) }}일
              {{ item.updateDate.substring(11, 13) }}시
              {{ item.updateDate.substring(14, 16) }}분
            </small>
          </div>

        </div>

      </router-link>
      <div style = "clear: both"></div>


      <div
          id = "chatRoomInfo"
          class = "mt-3"
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
  created() {
  },
  mounted() {
    this.$store.dispatch('REQUEST_GET_CHAT_ROOMS', this.nickname);
  }
}
</script>

<style scoped>

</style>