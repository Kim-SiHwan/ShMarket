<template>
  <v-container>
    <v-icon color="orange" size="50">mdi-emoticon-happy-outline</v-icon>
    <strong>{{ paramNickname }}</strong>님의 매너평가
    <v-data-table
        v-if="manners"
        id="mannerTable"
        :items="manners"
        :items-per-page="itemsPerPage"
        :page.sync="page"
        class="elevation-1 mt-3"
        hide-default-footer
        hide-default-header
        no-data-text="첫 평가를 작성해보세요!"
        style="border: solid green 1px"
        @page-count="pageCount= $event">

      <template v-slot:item="manners">
        <tr align="left">
          <td>
          <div>
            <p class="mt-4">{{ manners.item.content }}</p>
          </div>
          </td>
        </tr>
      </template>
    </v-data-table>

    <v-pagination
        v-model="page"
        :length="pageCount">

    </v-pagination>

    <v-row justify="center">
      <v-dialog v-model="dialog" max-width="450" persistent>
        <v-card>
          <v-card-title class="headline"><small>매너평가</small></v-card-title>
          <v-select
              v-model="selectedType"
              :items="typeItems"
              class="mr-2 ml-2"
              outlined>

          </v-select>
          <v-select
              v-if="selectedType === '매너 평가'"
              v-model="selectedManner"
              :items="mannerItems"
              class="mr-2 ml-2"
              outlined>

          </v-select>
          <v-select
              v-else-if="selectedType === '비매너 평가'"
              v-model="selectedManner"
              :items="nonMannerItems"
              class="mr-2 ml-2"
              outlined>

          </v-select>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                color="primary"
                @click="addManner">평가하기
            </v-btn>
            <v-btn
                color="error"
                @click="dialog = false">취소하기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>

    <v-fab-transition>
      <v-btn
          v-if="paramNickname !== nickname"
          color="orange"
          dark
          fab
          fixed
          right
          small
          style="bottom: 80px;"
          @click="dialog=true">
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </v-fab-transition>
  </v-container>

</template>

<script>
export default {
  name: "Manner",
  data() {
    return {
      dialog: '',
      paramNickname: '',
      selectedManner: '',
      selectedType: '',
      itemsPerPage: 8,
      pageCount: 0,
      page: 1,
      typeItems: [
        '매너 평가',
        '비매너 평가'
      ],
      mannerItems: [
        '친절해요.',
        '매너가 좋아요.',
        '연락이 빨라요',
        '시간 약속을 잘 지켜요.',
        '상품상태가 설명과 같아요.',
        '좋은 상품을 저렴하게 판매해요.'
      ],
      nonMannerItems: [
        '불친절해요.',
        '매너가 안좋아요',
        '연락이 느려요',
        '시간 약속을 안지켜요.',
        '상품상태가 설명과 달라요',
        '상품상태가 좋지 않아요.'
      ],
    }
  },
  methods: {
    addManner() {
      let data = {
        nickname: this.paramNickname,
        type: this.selectedType,
        content: this.selectedManner
      }
      this.$store.dispatch('REQUEST_ADD_MANNER', data);
      this.selectedType = '';
      this.dialog = false;
    }
  },
  computed: {
    manners() {
      return this.$store.state.memberStore.manners;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    }
  },
  created() {
    this.paramNickname = this.$route.query.nickname;
  },
  mounted() {
    this.$store.dispatch('REQUEST_GET_MANNERS', this.paramNickname);
    console.log(this.nickname);
  }
}
</script>

<style scoped>

</style>