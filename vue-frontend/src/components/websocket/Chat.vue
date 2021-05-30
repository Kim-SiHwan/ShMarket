<template>
  <v-container>

    <div class="float-left">
      <v-img
          :src="productDetail.productAlbums[0].url"
          width="150"
          height="150"
          contain
          style="display: inline-block"
      >
      </v-img>
    </div>

    <div class="ml-5">
      <p>{{productDetail.title}}</p>
      <p>{{productDetail.price}}</p>
      <p>{{productDetail.nickname}}</p>


    </div>
    <div style="clear: both"></div>
    <hr>

    <div v-for="(list,index) in list" :key="index">
      <div v-if="list.sender === nickname"
           id="myMsg"
           class="float-right pt-3 pl-3 pr-3  rounded-lg text-center mt-5"
           style="border-radius: 10px; background-color: orange; color:white; height: auto; overflow:hidden; word-break: break-all;width: auto; max-width: 40%"
      >
        <p>{{ list.message }}</p>
      </div>

      <div
          v-else
          class="float-left pt-3 pl-3 pr-3 rounded-lg text-center mt-5 "
          style="border-radius: 10px; background-color: darkgray;  height: auto; overflow:hidden; word-break: break-all;width: auto; max-width: 40%"
      >
        <p>{{ list.message }}</p>
      </div>
      <div style="clear: both"></div>

    </div>
    <v-text-field
        id="inputText"
        v-model="msg" style="position: fixed; right: 50px; bottom: 100px"
        @keyup.enter="send"
    >

    </v-text-field>

  </v-container>

</template>

<script>
import axios from 'axios';
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: "Chat",
  data() {
    return {
      list: [],
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
    this.chatRequestDto.receiver = this.$route.query.sender;
    this.$store.dispatch('REQUEST_GET_PRODUCT', this.chatRequestDto.productId);
    this.connect()
  },
  mounted() {
    if (!this.chatRequestDto.receiver) {
      this.chatRequestDto.receiver = this.productDetail.nickname;
    }

    axios.get('/api/chat/' + this.chatRequestDto.roomId)
        .then(res => {
          console.log(res.data);
          this.list = res.data;
        });

  },
  methods: {

    send() {
      this.chatRequestDto.sender = this.nickname;
      this.stompClient.send('/app/chat/msg', JSON.stringify({
        roomId: this.chatRequestDto.roomId,
        productId: this.productDetail.id,
        sender: this.chatRequestDto.sender,
        receiver: this.chatRequestDto.receiver,
        message: this.msg
      }), {});
      this.msg = '';

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

        this.stompClient.subscribe('/topic/msg.' + this.chatRequestDto.roomId, (tick) => {
          console.log('구독 :/send/msg/' + this.chatRequestDto.roomId, tick);
          console.log(tick.body);
          this.list.push(JSON.parse(tick.body));
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
    }
  }
}
</script>

<style scoped>

</style>