<template>
  <v-container>

    <v-btn @click="chat">채팅하기</v-btn>

    <div style="text-align: center">
      <h3 v-if="!updateFlag">{{ productDetail.title }}</h3>
      <v-text-field
          v-else
          v-model="updateRequestData.title"></v-text-field>
    </div>
    <div>
      <span><small>판매자 : {{ productDetail.nickname }}</small></span><br>
      <span><small>지역 : {{ productDetail.area }}</small></span><br>
      <span><small>작성일 : {{ time }}</small></span><br>
      <span v-if="productDetail.tags">
        <small>관련 태그 :
            <div v-for="(tags,index) in productDetail.tags" :key="index"
                 style="list-style: none; display: inline">
               <v-chip
                   class="ml-0 mr-1 pr-2 pl-2"
                   color="orange"
                   label
                   small
                   text-color="white">
                 {{ tags.tag }}
              </v-chip>
           </div>
        </small>
      </span>
    </div>

    <div v-if="!productDetail.hasImages">

      <v-carousel
          height="500"
          hide-delimiter-background>

        <v-carousel-item
            v-for="(item,i) in productDetail.productAlbums"
            :key="i">
          <v-img :src="item.url" contain max-height="500"></v-img>

        </v-carousel-item>
      </v-carousel>
      <div style="text-align: center; margin-top: 10px">
        <v-btn
            v-if="productDetail.like"
            color="pink"
            dark
            icon
            @click="pushLike(productDetail.id)">
          <v-icon dark>mdi-heart</v-icon>
        </v-btn>
        <v-btn
            v-else
            color="grey"
            dark
            icon
            @click="pushLike(productDetail.id)">
          <v-icon dark>mdi-heart</v-icon>
        </v-btn>
        <v-btn
            v-if="flag"
            class="white--text"
            color="orange"
            @click="changeShowImages">접어두기
        </v-btn>
        <v-btn
            v-else
            class="white--text"
            color="orange"
            @click="changeShowImages">펼쳐보기
        </v-btn>

      </div>
      <v-row
          v-if="flag"
          class="mt-5"
          justify="center">
        <v-col
            v-for="(file,index) in productDetail.productAlbums" :key="index"
            class="d-flex child-flex"
            cols="3">
          <div id="productFileImgDiv">
            <v-img
                :src="file.url"
                aspect-ratio="1.2"
                contain
                width="500"
                @click="selectedImg(file.id,$event)">
              <p style="color: red; visibility: hidden">선택</p>
            </v-img>
          </div>
        </v-col>
      </v-row>
    </div>
    <v-textarea
        v-if="productDetail && !updateFlag"
        background-color="white"
        class="mt-10"
        no-resize
        outlined
        readonly="readonly"
        v-bind:rows="productDetail.content.length/5"
        v-bind:value="productDetail.content"
    ></v-textarea>
    <v-textarea
        v-else-if="updateFlag"
        v-model="updateRequestData.content"
        background-color="white"
        class="mt-10"
        label="수정할 내용을 입력해주세요."
        outlined
        v-bind:value="productDetail.content">
    </v-textarea>

    <div id="productDetailBtnDiv" class="float-right mt-2 ml-3">

      <v-btn
          v-if="!updateFlag"
          color="info"
          @click="clickUpdateBtn">
        수정
      </v-btn>

      <v-btn
          v-if="updateFlag"
          color="warning"
          @click="clickUpdateBtn">
        취소
      </v-btn>

      <v-btn
          class="mr-3 ml-3"
          color="error"
          @click="deleteBoard">
        삭제
      </v-btn>

      <v-btn
          v-if="updateFlag"
          class="mr-3"
          color="success" @click="updateBoard(fileData)">
        수정
      </v-btn>

    </div>


    <v-file-input
        v-if="updateFlag"
        class="mr-3"
        label="사진 추가"
        multiple
        outlined
        @change="selectedFile">
    </v-file-input>

  </v-container>

</template>

<script>
import axios from 'axios';

export default {
  name: "ProductDetail",
  data() {
    return {
      flag: false,
      updateFlag: '',
      time: '',
      fileData: '',
      updateRequestData: {
        id: '',
        title: '',
        content: '',
        ids: []
      }

    }
  },

  methods: {
    chat() {
      let dto = {
        sender: sessionStorage.getItem('nickname'),
        receiver: this.productDetail.nickname
      };
      axios.request({
        url: '/api/chat',
        method: 'POST',
        data: dto
      }).then(res => {
        console.log(res.data);
        this.$router.push({
          path: '/chat',
          query: {roomId: res.data}
        });
      })


    },
    changeShowImages() {
      this.flag = !this.flag;
    },
    pushLike(productId) {
      console.log(productId);
      this.$store.dispatch('REQUEST_PUSH_INTEREST', productId);
    },
    displayedAt(createdAt) {
      createdAt = new Date(createdAt);
      const milliSeconds = new Date() - createdAt
      const seconds = milliSeconds / 1000
      if (seconds < 60) {
        return `방금 전`
      }
      const minutes = seconds / 60
      if (minutes < 60) {
        return `${Math.floor(minutes)}분 전`
      }
      const hours = minutes / 60
      if (hours < 24) {
        return `${Math.floor(hours)}시간 전`
      }
      const days = hours / 24
      if (days < 7) {
        return `${Math.floor(days)}일 전`
      }
      const weeks = days / 7
      if (weeks < 5) {
        return `${Math.floor(weeks)}주 전`
      }
      const months = days / 30
      if (months < 12) {
        return `${Math.floor(months)}개월 전`
      }
      const years = days / 365
      return `${Math.floor(years)}년 전`
    },
    deleteBoard() {
      if (this.checkAuthority()) {
        this.$store.dispatch('REQUEST_DELETE_PRODUCT', this.productDetail.id);
      }
    },
    clickUpdateBtn() {
      if (this.checkAuthority()) {
        this.updateFlag = !this.updateFlag;
      }
    },
    checkAuthority() {
      if (this.nickname !== this.productDetail.nickname) {
        this.$store.commit('SET_SNACK_BAR', {
          msg: '작성자만 수정/삭제가 가능합니다.', color: 'error'
        });
        return false;
      }
      return true;
    },
    selectedImg(fileId, event) {
      if (!this.updateFlag)
        return false;
      let targetImg = this.updateRequestData.ids.indexOf(fileId);
      let current = event.target.childNodes[0];
      if (targetImg !== -1) {
        current.style.visibility = 'hidden';
        this.updateRequestData.ids.splice(targetImg, 1);
      } else {
        current.style.visibility = 'visible';
        this.updateRequestData.ids.push(fileId);
      }
      console.log(this.updateRequestData.ids);
    },
    selectedFile(event) {
      const files = event;
      let formData = new FormData;
      for (let file in files) {
        formData.append('files', files[file]);
      }
      formData.set('hasImages', "yes");
      this.fileData = formData;
    },

    updateBoard(formData) {
      if (formData === '') {
        formData = new FormData;
        formData.set('hasImages', "no");
      }
      if (this.updateRequestData.title === '')
        this.updateRequestData.title = this.productDetail.title;
      if (this.updateRequestData.content === '')
        this.updateRequestData.content = this.productDetail.content;

      formData.set('id', this.updateRequestData.id);
      formData.set('title', this.updateRequestData.title);
      formData.set('content', this.updateRequestData.content);
      formData.set('ids', this.updateRequestData.ids);

      this.$store.dispatch('REQUEST_UPDATE_PRODUCT', formData);
      this.updateFlag = false;
      this.fileData = '';
      this.updateRequestData.ids = [];
    },
  },
  computed: {
    productDetail() {
      return this.$store.state.productStore.productDetail;
    },
    nickname() {
      return sessionStorage.getItem('nickname');
    }
  },
  created() {
    this.$store.dispatch('REQUEST_GET_PRODUCT', this.$route.query.productId);
    this.time = this.displayedAt(this.productDetail.createDate);
  },
  mounted() {
    this.updateRequestData.id = this.$route.query.productId;
    this.updateRequestData.title = this.productDetail.title;
    this.updateRequestData.content = this.productDetail.content;
  }
}
</script>

<style scoped>

</style>