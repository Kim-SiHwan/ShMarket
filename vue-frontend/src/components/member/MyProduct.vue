<template>
  <v-container>

    <v-icon color = "green" size = "50">mdi-shopping</v-icon>
    <strong>{{ names.requestNickname }}</strong>님의 판매 상품
    <div id = "myProductListDiv" class = "row justify-center mt-15">
      <ul v-for = "(list,index) in myProductList" :key = "index"
          style = "list-style: none">
        <li id = "myListDiv">
          <div class = "p-5 mb-5 rounded float-left"
               style = "width: 300px; height: 400px; border: 2px solid green">
            <div class = "card-body">
              <span class = "mt-3" style = "margin-left: 130px"><strong>{{ list.title }}</strong></span>
              <br>
              <span class = "float-left card-subtitle">
              <router-link :to = "{path:'/profile',query:{nickname:list.nickname}}"><span
                  class = "float-left ml-3 mt-1 mr-3"><small>작성자 : {{ list.nickname }}</small></span></router-link>
              <br>
              <span
                  class = "float-left ml-3 mt-1 mr-3"> <small>작성일 : {{ displayedAt(list.createDate) }}</small></span>
              <br>
              <span class = "float-left ml-3 mt-1 mr-3"><small>지역 : {{ list.area }}</small></span>
              </span>
              <br>

              <div id = "myProductListImgDiv" style = "height: 100%; width: 100%">
                <router-link
                    :to = "{
                    path:'/productDetail',
                    query:{productId:list.id}
                  }">
                  <v-img
                      :src = "list.thumbnail"
                      class = "mt-15 mr-3 ml-13 grey lighten-3"
                      height = "200"
                      width = "200">

                  </v-img>
                </router-link>
                <div
                    v-if = "list.tags.length ===0 "
                    style = "height: 34px">
                </div>

                <div v-else id = "myProductTagDiv" class = "mt-5">
                  <v-row align-content = "center" justify = "center">
                    <div v-for = "(tags,index) in list.tags" :key = "index" style = "list-style: none; display: inline">
                      <v-chip
                          class = "ml-0 mr-1 pr-2 pl-2"
                          color = "info"
                          label
                          small>
                        {{ tags.tag }}
                      </v-chip>
                    </div>
                  </v-row>
                </div>

                <div id = "myProductListIconDiv" class = "mt-8">
                  <v-row align-content = "center" justify = "center">

                    <v-icon
                        color = "green">
                      mdi-image-multiple
                    </v-icon>
                    <span class = "mt-1">{{ list.productAlbumCount }}</span>

                    <v-btn
                        v-if = "list.like"
                        color = "pink"
                        dark
                        icon
                        @click = "pushLike(list.id)">

                      <v-icon dark>mdi-heart</v-icon>

                    </v-btn>

                    <v-btn
                        v-else
                        color = "grey"
                        dark
                        icon
                        @click = "pushLike(list.id)">

                      <v-icon dark>mdi-heart</v-icon>

                    </v-btn>
                  </v-row>
                </div>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>
    <v-pagination
        v-model = "page"
        :length = "myProductTotalPage"
        total-visible = "10"
        @input = "showMyProductPage(page)">

    </v-pagination>
  </v-container>

</template>

<script>
import {mixinData} from "@/mixin/mixins";

export default {
  name    : "MyProduct",
  mixins  : [mixinData],
  data() {
    return {
      names        : {
        originName     : '',
        requestNickname: '',

      },
      paramNickname: '',
      page         : 1
    }
  },
  methods : {
    showProductByNickname() {
      let data = {
        page    : this.myProductCurrentPage - 1,
        nickname: this.paramNickname
      }
      this.$store.dispatch('REQUEST_GET_MY_PRODUCT', data);
    },
    showMyProductPage(page) {
      console.log("page : " + page);
      this.$store.commit('SET_MY_PRODUCT_CURRENT_PAGE', page);
      let data = {
        page    : page - 1,
        nickname: this.paramNickname
      };
      this.$store.dispatch('REQUEST_GET_MY_PRODUCT', data);
    },
    async pushLike(productId) {
      await this.$store.dispatch('REQUEST_PUSH_INTEREST', productId)
          .then(() => {
                let data = {
                  page    : this.myProductCurrentPage - 1,
                  nickname: this.paramNickname
                }
                this.$store.dispatch('REQUEST_GET_MY_PRODUCT', data);
              }
          );
    },

  },
  computed: {
    myProductList() {
      return this.$store.state.productStore.myProductList;
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    },
    myProductTotalPage() {
      return this.$store.state.productStore.myProductTotalPage;
    },
    myProductCurrentPage() {
      return this.$store.state.productStore.myProductCurrentPage;
    }
  },
  created() {
    this.paramNickname = this.$route.query.nickname;
    this.names.requestNickname = this.$route.query.nickname;
    this.names.originName = this.nickname;
    this.page = this.myProductCurrentPage;
  },
  mounted() {
    console.log(this.names);
    this.showProductByNickname();
  }
}
</script>

<style scoped>

</style>