<template>
  <v-container>
    <v-icon color = "yellow" size = "50">mdi-lock-question</v-icon>
    문의사항
    <br>
    <v-btn color = "info" @click = "getMyQna">내것만 보기</v-btn>
    <v-btn color = "secondary" @click = "getAllQna">전체 보기</v-btn>
    <v-data-table
        id = "qnaTable"
        :headers = "headers"
        :items = "qnaList"
        :items-per-page = "itemsPerPage"
        :page.sync = "page"
        class = "elevation-1 mt-3"
        hide-default-footer
        no-data-text = "첫 문의를 작성해보세요!"
        style = "border: solid green 1px"
        @page-count = "pageCount= $event">

      <template v-slot:item = "qnaList">
        <tr>
          <td width = "150">{{ qnaList.item.nickname }}</td>
          <td @click = "getQnaDetail(qnaList.item.id)">{{ qnaList.item.title }}</td>
          <td width = "150">{{ qnaList.item.createDate.substring(0, 10) }}</td>
          <td v-if = "qnaList.item.answered" width = "70">O</td>
          <td v-else width = "70">X</td>
        </tr>
      </template>
    </v-data-table>

    <v-pagination
        v-model = "page"
        :length = "pageCount">

    </v-pagination>


    <v-row justify = "center">
      <v-dialog v-model = "dialog" max-width = "450" persistent>
        <v-card>
          <v-card-title class = "headline"><small>문의 남기기</small></v-card-title>
          <v-text-field
              v-model = "requestData.title"
              class = "ml-3 mr-3"
              label = "제목을 입력하세요."
              outlined>

          </v-text-field>
          <v-textarea
              v-model = "requestData.content"
              class = "ml-3 mr-3"
              label = "질문을 입력하세요."
              outlined>

          </v-textarea>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                color = "primary"
                @click = "addQna">질문하기
            </v-btn>
            <v-btn
                color = "error"
                @click = "dialog = false">취소하기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>

    <v-fab-transition>
      <v-btn
          color = "yellow"
          dark
          fab
          fixed
          right
          small
          style = "bottom: 80px;"
          @click = "dialog=true">
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </v-fab-transition>


  </v-container>

</template>

<script>
import {mixinData} from "@/mixin/mixins";

export default {
  name    : "Qna",
  mixins  : [mixinData],
  data() {
    return {
      dialog      : false,
      requestData : {
        title   : '',
        content : '',
        nickname: ''
      },
      itemsPerPage: 8,
      pageCount   : 0,
      page        : 1,
      headers     : [{
        text    : '작성자',
        align   : 'start',
        sortable: false,
        value   : 'name'

      },
        {text: '제목'},
        {text: '작성일'},
        {text: '답변'},
      ]
    }
  },
  methods : {
    addQna() {
      this.dialog = false;
      this.requestData.nickname = this.nickname;
      this.$store.dispatch('REQUEST_ADD_QNA', this.requestData)
          .then(() => {
            this.$store.dispatch('REQUEST_GET_ALL_QNA');
          });
    },
    getMyQna() {
      this.$store.dispatch('REQUEST_GET_ALL_MY_QNA', this.nickname);
    },
    getAllQna() {
      this.$store.dispatch('REQUEST_GET_ALL_QNA');
    },
    getQnaDetail(qnaId) {
      this.$router.push({
        path : '/qnaDetail',
        query: {qnaId: qnaId}
      })
    }
  },
  computed: {
    nickname() {
      return this.$store.state.memberStore.nickname;
    },
    qnaList() {
      return this.$store.state.qnaStore.qnaList;
    }
  },
  created() {
  },
  mounted() {
    this.$store.dispatch('REQUEST_GET_ALL_QNA');
  }
}
</script>

<style scoped>

</style>