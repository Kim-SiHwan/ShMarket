<template>
  <v-app id = "headerDiv">
    <v-switch
        class = "ml-15"
        color = "blue"
        hide-details
        label = "카테고리 필터"
        v-on:change = "changeCategoryFlag">

    </v-switch>

    <div v-if = "showCategoryFlag">
      <div v-for = "(flag,name) in categories" :key = "name">
        <v-checkbox
            v-if = "flag"
            :label = "name"
            class = "ml-15"
            color = "green"
            hide-details
            input-value = "true"
            v-on:change = "checked(name)">

        </v-checkbox>

        <v-checkbox
            v-else
            :label = "name"
            class = "ml-15"
            color = "green"
            hide-details
            v-on:change = "checked(name)">

        </v-checkbox>
      </div>
    </div>


    <div id = "productListDiv" class = "row justify-center mt-15">
      <ul v-for = "(list,index) in productList" :key = "index"
          style = "list-style: none">
        <li id = "listDiv">
          <div class = "p-5 mb-5 rounded float-left"
               style = "width: 300px; height: 375px; border: 2px solid green">
            <div class = "card-body">
              <v-row justify = "center">
                <span class = "mt-3"><strong>{{ list.title }}</strong></span>
              </v-row>

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

              <div id = "productListImgDiv" style = "height: 100%; width: 100%">
                <router-link
                    :to = "{
                    path:'/productDetail',
                    query:{productId:list.id}}">
                  <v-img
                      v-if="list.status !== 'COMPLETED'"
                      :src = "list.thumbnail"
                      class = "mt-15 mr-3 ml-13 grey lighten-3"
                      height = "200"
                      width = "200">

                  </v-img>
                  <div v-else class="fill-height repeating-gradient">
                  <v-img
                      :src = "list.thumbnail"
                      class = "mt-15 mr-3 ml-13 grey lighten-3"
                      style="filter: brightness(50%)"
                      height = "200"
                      width = "200">
                    <h2 class="ml-15 mt-15" style="text-decoration: none; color:blue">판매완료</h2>

                  </v-img>
                  </div>
                </router-link>
                <div
                    v-if = "list.tags.length ===0 "
                    style = "height: 34px">
                </div>

                <div v-else id = "productTagDiv" class = "mt-5">
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

                <div id = "productListIconDiv" class = "mt-5">
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
                        @click = "pushLike(list.id,index)">

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
        :length = "totalPage"
        total-visible = "10"
        @input = "showProductPage(page)">

    </v-pagination>

    <v-fab-transition>

      <v-btn
          bottom
          color = "green lighten -1"
          dark
          fab
          fixed
          right
          small
          @click = "$vuetify.goTo('#headerDiv')">
        <v-icon>mdi-chevron-double-up</v-icon>
      </v-btn>
    </v-fab-transition>

    <v-fab-transition>
      <v-btn
          color = "green lighten -1"
          dark
          fab
          fixed
          right
          small
          style = "bottom: 80px;"
          @click = "addProduct">
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </v-fab-transition>
  </v-app>
</template>

<script>

import {mixinData} from "@/mixin/mixins";

export default {
  name      : "productList",
  components: {},
  mixins    : [mixinData],
  data() {
    return {
      showCategoryFlag: false,
      page            : 1
    }
  },
  methods   : {
    addProduct() {
      this.$router.push('/addProduct');
    },
    showProductList() {
      let data = {
        page    : this.currentPage,
        category: localStorage.getItem('productCategories')
      };
      this.$store.dispatch('REQUEST_GET_ALL_PRODUCTS_PAGES', data);

    },
    showProductPage(page) {
      console.log("page : " + page);
      this.$store.commit('SET_PRODUCT_CURRENT_PAGE', page);
      let data = {
        page    : page,
        category: localStorage.getItem('productCategories')
      };
      this.$store.dispatch('REQUEST_GET_ALL_PRODUCTS_PAGES', data);

    },
    async pushLike(productId) {
      await this.$store.dispatch('REQUEST_PUSH_INTEREST', productId)
          .then(() => {
            let data = {
              page    : this.currentPage,
              category: localStorage.getItem('productCategories')
            };
            this.$store.dispatch('REQUEST_GET_ALL_PRODUCTS_PAGES', data);
          });
    },
    checked(name) {
      this.categories[name] = !this.categories[name];
      this.page = 1;
      this.changeCategories('product', this.categories);
      /*
      localStorage.setItem('productCategories', JSON.stringify(this.categories));
      this.$store.commit('SET_PRODUCT_CURRENT_PAGE', 1);
      this.page = 1;
      let data = {
        page    : 1,
        category: localStorage.getItem('productCategories')
      }
      this.$store.dispatch('REQUEST_GET_ALL_PRODUCTS_PAGES', data);*/
    },
    changeCategoryFlag() {
      this.showCategoryFlag = !this.showCategoryFlag;
    },
  },
  computed  : {
    productList() {
      return this.$store.state.productStore.productList;
    },
    categories() {
      return JSON.parse(localStorage.getItem('productCategories'));
    },
    nickname() {
      return this.$store.state.memberStore.nickname;
    },
    totalPage() {
      return this.$store.state.productStore.productTotalPage;
    },
    currentPage() {
      return this.$store.state.productStore.productCurrentPage;
    }

  },
  created() {
    this.page = this.currentPage;
  },
  mounted() {
    this.showProductList();
  }
}
</script>
<style scoped>
.repeating-gradient{
  background-image: repeating-linear-gradient(-45deg,
  rgba(255,0,0,.25),
  rgba(255,0,0,.25) 5px,
  rgba(0,0,255,.25) 5px,
  rgba(0,0,255,.25) 10px

  );
}
small {
  color: black;
}

</style>