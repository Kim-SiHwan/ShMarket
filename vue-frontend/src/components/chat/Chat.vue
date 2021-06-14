<template>
  <v-container>

    <div class="float-left">
      <v-img
          v-if="!productDetail.hasImages"
          :src="productDetail.productAlbums[0].url"
          contain
          height="150"
          style="display: inline-block"
          width="150">

      </v-img>
    </div>

    <div class="ml-5">
      <p>{{ productDetail.title }}</p>
      <p>{{ productDetail.price }}</p>
      <p>{{ productDetail.nickname }}</p>

    </div>
    <div style="clear: both"></div>
    <hr>

    <div v-for="(list,index) in chatLogs" :key="index">
      <div v-if="list.sender === nickname"
           id="myMsg"
           class="float-right pt-3 pl-3 pr-3  rounded-lg text-center mt-5"
           style="border-radius: 10px; background-color: orange; color:white; height: auto; overflow:hidden; word-break: break-all;width: auto; max-width: 40%">

        <p>{{ list.message }}</p>

      </div>

      <div v-else>

        <span class="float-left">{{list.sender}}</span>
        <div style="clear:both"></div>
        <div
            class="float-left pt-3 pl-3 pr-3 rounded-lg text-center "
            style="border-radius: 10px; background-color: darkgray;  height: auto; overflow:hidden; word-break: break-all;width: auto; max-width: 40%">
        <p>{{ list.message }}</p>
        </div>
      </div>
      <div style="clear: both"></div>
    </div>

    <div id="bottomDiv">

    </div>

    <v-row justify="center" align-content="center">

    <div
        id="chatDiv"
        style="bottom: 50px; position: fixed;">
      <v-text-field
          id="inputText"
          v-model="msg"
          outlined
          style="display: inline-block; width: 500px"
          @keyup.enter="send">
      </v-text-field>

      <v-btn
          color="orange"
          dark
          height="57"
          @click="send">전송
      </v-btn>
    </div>    </v-row>

  </v-container>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: "Chat",
  data() {
    return {
      roomId: '',
      msg: '',
      chatRequestDto: {
        message: '',
        sender: '',
        receiver: '',
        roomId: '',
        productId: ''
      }
    }
  },
  created() {
    this.chatRequestDto.roomId = this.$route.query.roomId;
    this.chatRequestDto.productId = this.$route.query.productId;
    this.chatRequestDto.sender = this.nickname;
    this.chatRequestDto.receiver = this.$route.query.sender;
    this.$store.dispatch('REQUEST_GET_PRODUCT', this.chatRequestDto.productId);
    this.connect()
  },
  mounted() {
    if (!this.chatRequestDto.receiver) {
      this.chatRequestDto.receiver = this.productDetail.nickname;
    }
    if(this.chatRequestDto.sender  === this.chatRequestDto.receiver){
      if(this.chatLogs[0].sender === this.chatRequestDto.sender){
        this.chatRequestDto.receiver = this.chatLogs[0].receiver;
      }else{
        this.chatRequestDto.receiver = this.chatLogs[0].sender;
      }
    }
    let getLogData={
      roomId : this.$route.query.roomId,
      nickname : this.nickname
    };
    this.$store.dispatch('REQUEST_GET_CHAT_LOGS', getLogData);
    this.$vuetify.goTo('#bottomDiv');
    this.$store.dispatch('REQUEST_GET_CHAT_COUNT', this.nickname);
  },
  methods: {
    send() {
      console.log(this.chatRequestDto.sender);
      console.log(this.chatRequestDto.receiver);

      this.stompClient.send('/app/chat/msg', JSON.stringify({
        roomId: this.chatRequestDto.roomId,
        productId: this.productDetail.id,
        sender: this.chatRequestDto.sender,
        receiver: this.chatRequestDto.receiver,
        message: this.msg
      }), {});
      this.msg = '';
      this.$vuetify.goTo('#bottomDiv');
    },
    connect() {
      this.socket = new SockJS('http://localhost:8080/ws')
      let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
      this.stompClient = Stomp.over(this.socket, options)
      console.log(`소켓 연결을 시도합니다. 서버 주소: `)
      this.stompClient.connect({}, (frame) => {
        this.connected = true
        console.log('소켓 연결 성공', frame);
        console.log(frame)
        this.stompClient.subscribe('/exchange/chat-exchange/msg.' + this.chatRequestDto.roomId, (tick) => {
          // this.stompClient.subscribe('/topic/msg.' + this.chatRequestDto.roomId, (tick) => {
          console.log('구독 :/send/msg/' + this.chatRequestDto.roomId, tick);
          console.log(tick.body);
          this.chatLogs.push(JSON.parse(tick.body));
        })
      }, (error) => {
        console.log('연결실패');
        console.log(error)
        this.connected = false
      })
    },
  },
  computed: {
    productDetail() {
      return this.$store.state.productStore.productDetail;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    },
    chatLogs() {
      return this.$store.state.chatStore.chatLog;
    }
  }
}
</script>

<style scoped>

</style>