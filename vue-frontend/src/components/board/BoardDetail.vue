<template>
  <v-container>
    <div id="top"></div>

    <div style="text-align: center">
      <h3 v-if="!updateFlag">{{ boardDetail.title }}</h3>
      <v-text-field
          v-else
          v-model="updateRequestData.title"></v-text-field>
    </div>
    <hr>
    <div>
      <span><small>작성자 : {{ boardDetail.nickname }}</small></span><br>
      <span><small>지역 : {{ boardDetail.area }}</small></span><br>
      <span><small>작성일 : {{ displayedAt(boardDetail.updateDate) }}</small></span><br>

    </div>

    <div v-if="!boardDetail.hasImages">

      <v-carousel
          height="500"
          hide-delimiter-background>

        <v-carousel-item
            v-for="(item,i) in boardDetail.boardAlbums"
            :key="i">
          <v-img :src="item.url" contain max-height="500"></v-img>

        </v-carousel-item>
      </v-carousel>
      <div
          class="mt-2"
          style="text-align: center;">

        <v-btn
            v-if="flag"
            color="green"
            dark
            @click="changeShowImages">접어두기
        </v-btn>
        <v-btn
            v-else
            color="green"
            dark
            @click="changeShowImages">펼쳐보기
        </v-btn>

      </div>
      <v-row
          v-if="flag"
          class="mt-5"
          justify="center">
        <v-col
            v-for="(file,index) in boardDetail.boardAlbums" :key="index"
            class="d-flex child-flex"
            cols="3">
          <div id="boardImgDiv">
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
        v-if="boardDetail && !updateFlag"
        background-color="white"
        class="mt-10"
        no-resize
        outlined
        readonly="readonly"
        v-bind:value="boardDetail.content">
    </v-textarea>

    <v-textarea
        v-else-if="updateFlag"
        v-model="updateRequestData.content"
        background-color="white"
        class="mt-10"
        label="수정할 내용을 입력해주세요."
        outlined
        v-bind:value="boardDetail.content">
    </v-textarea>

    <div id="boardDetailBtnDiv" class="float-right mt-2 ml-3">

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
    <br><br><br>
    <comment></comment>

  </v-container>

</template>

<script>
import Comment from "./Comment";

export default {
  name: "boardDetail",
  components: {
    'comment': Comment,
  },
  data() {
    return {
      flag: false,
      time: '',
      updateFlag: false,
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
    changeShowImages() {
      this.flag = !this.flag;
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
        this.$store.dispatch('REQUEST_DELETE_BOARD', this.boardDetail.id);
      }
    },
    clickUpdateBtn() {
      if (this.checkAuthority()) {
        this.updateFlag = !this.updateFlag;
      }
    },
    checkAuthority() {
      if (this.nickname !== this.boardDetail.nickname) {
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
        this.updateRequestData.title = this.boardDetail.title;
      if (this.updateRequestData.content === '')
        this.updateRequestData.content = this.boardDetail.content;

      formData.set('id', this.updateRequestData.id);
      formData.set('title', this.updateRequestData.title);
      formData.set('content', this.updateRequestData.content);
      formData.set('ids', this.updateRequestData.ids);

      this.$store.dispatch('REQUEST_UPDATE_BOARD', formData);
      this.updateFlag = false;
      this.fileData = '';
      this.updateRequestData.ids = [];
    },
  },
  computed: {
    boardDetail() {
      return this.$store.state.boardStore.boardDetail;
    },
    nickname() {
      return sessionStorage.getItem('nickname');
    },
  },
  created() {
    this.$store.dispatch('REQUEST_GET_BOARD', this.$route.query.boardId);
  },
  mounted() {
    this.$vuetify.goTo('.mainBar');
    this.updateRequestData.id = this.$route.query.boardId;
    this.updateRequestData.title = this.boardDetail.title;
    this.updateRequestData.content = this.boardDetail.content;
  }
}
</script>

<style scoped>

</style>