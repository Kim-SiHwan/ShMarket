<template>
  <v-container>

    <div class = "float-left">
      <v-img
          v-if = "chatRoomDetail.productThumbnail"
          :src = "chatRoomDetail.productThumbnail"
          contain
          height = "150"
          style = "display: inline-block"
          width = "150">

      </v-img>
    </div>

    <div class = "ml-5">
      <p>판매자 : {{ chatRoomDetail.productSeller }}</p>
      <p>상품 : {{ chatRoomDetail.productTitle }}</p>
      <p>상품 가격 : {{ chatRoomDetail.productPrice }}</p>
    </div>

    <div style = "clear: both"></div>
    <hr>

    <div id = "chatDiv"
         style = "height: 550px; overflow: scroll">

      <div v-for = "(list,index) in chatRoomDetail.chatLogs" :key = "index">
        <div v-if = "list.sender === nickname">
          <div
              id = "myMsg"
              class = "float-right pt-3 pl-3 pr-3  rounded-lg text-center mt-5"
              style = "border-radius: 10px; background-color: orange; color:white; height: auto; overflow:hidden; word-break: break-all;width: auto; max-width: 40%">

            <p>{{ list.message }}</p>
          </div>
          <div style = "clear:both"></div>
          <p class = "float-right"><small>{{ list.createDate.substring(0, 16) }}</small></p>

        </div>

        <div v-else>
          <div>
            <span class = "float-left">{{ list.sender }}</span>
            <div style = "clear:both"></div>
            <div
                class = "float-left pt-3 pl-3 pr-3 rounded-lg text-center "
                style = "border-radius: 10px; background-color: darkgray;  height: auto; overflow:hidden; word-break: break-all;width: auto; max-width: 40%">
              <p>{{ list.message }}</p>

            </div>
          </div>
          <div style = "clear:both"></div>
          <p class = "float-left"><small>{{ list.createDate.substring(0, 16) }}</small></p>

        </div>


        <div style = "clear: both"></div>
      </div>

    </div>
    <div id = "bottomDiv">

      <v-row justify = "center">
        <div>
          <v-text-field
              id = "inputText"
              v-model = "msg"
              class = "mt-7"
              outlined
              style = "width: 300px; display: inline-block"
              @keyup.enter = "checkConnect">
          </v-text-field>

          <v-btn
              color = "orange"
              dark
              height = "57"
              @click = "checkConnect">전송
          </v-btn>
        </div>
      </v-row>
    </div>

  </v-container>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name    : "Chat",
  data() {
    return {
      productDetail : '',
      productInfo   : {
        title : '',
        price : '',
        seller: '',
        image : ''
      },
      roomId        : '',
      msg           : '',
      chatRequestDto: {
        message  : '',
        sender   : '',
        receiver : '',
        roomId   : '',
        productId: ''
      }
    }
  },
  methods : {
    getChatLogs() {
      let getLogData = {
        roomId  : this.$route.query.roomId,
        nickname: this.nickname
      };
      this.$store.dispatch('REQUEST_GET_CHAT_LOGS', getLogData).then(() => {
        this.$store.dispatch('REQUEST_GET_CHAT_COUNT', this.nickname);
      });
      document.getElementById("chatDiv").scrollTop = document.getElementById("chatDiv").scrollHeight;
    },
    sendMessage() {
      this.stompClient.send('/app/chat/msg', JSON.stringify({
        roomId   : this.chatRequestDto.roomId,
        productId: this.chatRoomDetail.productId,
        sender   : this.nickname,
        receiver : this.chatRequestDto.receiver,
        message  : this.msg
      }), {});
      this.msg = '';
      document.getElementById("chatDiv").scrollTop = document.getElementById("chatDiv").scrollHeight;
    },
    checkConnect() {
      console.log(this.chatRequestDto.sender);
      console.log(this.chatRequestDto.receiver);
      if (!this.connected) {
        this.connect();
        setTimeout(() => {
          this.sendMessage();
        }, 300);

      } else {
        console.log("노타임아웃")
        this.sendMessage();
      }

    },
    connect() {
      this.socket = new SockJS('http://localhost:8080/ws');
      let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
      this.stompClient = Stomp.over(this.socket, options);
      console.log(`소켓 연결을 시도합니다. 서버 주소: `);


      let headers = {Authorization: sessionStorage.getItem('access_token')};

      this.stompClient.connect(headers, (frame) => {
        this.connected = true;

        console.log('소켓 연결 성공', frame);
        console.log(frame);

        this.stompClient.subscribe('/exchange/chat-exchange/msg.' + this.chatRequestDto.roomId, (tick) => {
          // this.stompClient.subscribe('/topic/msg.' + this.chatRequestDto.roomId, (tick) => {
          console.log('구독 :/send/msg/' + this.chatRequestDto.roomId, tick);
          console.log(tick.body);
          this.chatRoomDetail.chatLogs.push(JSON.parse(tick.body));
          document.getElementById("chatDiv").scrollTop = document.getElementById("chatDiv").scrollHeight;
        })
      }, (error) => {
        console.log('연결실패');
        console.log(error)
        this.connected = false
      })

    },

  },
  computed: {
    chatRoomDetail() {
      return this.$store.state.chatStore.chatRoomDetail;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    },
    chatLogs() {
      return this.$store.state.chatStore.chatLog;
    }
  },

  created() {
    this.chatRequestDto.roomId = this.$route.query.roomId;
    this.chatRequestDto.productId = this.$route.query.productId;
    this.chatRequestDto.sender = this.nickname;
    this.chatRequestDto.receiver = this.$route.query.receiver;
    this.connect()
  },
  mounted() {
    this.getChatLogs();
  },
}
</script>

<style scoped>

</style>