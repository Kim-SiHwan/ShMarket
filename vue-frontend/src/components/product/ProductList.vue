<template>
  <v-app id="headerDiv">

    <p><small v-if="productList">{{ productList.length }}개의 상품이 있습니다</small></p>

    <v-switch
        class="ml-15"
        color="orange"
        hide-details
        label="카테고리 필터"
        v-on:change="changeCategoryFlag"
    ></v-switch>

    <div v-if="showCategoryFlag">

      <div v-for="(flag,name) in categories" :key="name">

        <v-checkbox
            v-if="flag"
            :label="name"
            class="ml-15"
            color="orange"
            hide-details
            input-value="true"
            v-on:change="checked(name)">

        </v-checkbox>

        <v-checkbox
            v-else
            :label="name"
            class="ml-15"
            color="orange"
            hide-details
            v-on:change="checked(name)">

        </v-checkbox>


      </div>
    </div>


    <div id="productListDiv" class="row justify-center mt-15">
      <ul v-for="(list,index) in productList" :key="index"
          style="list-style: none">
        <li id="listDiv">
          <div class="p-5 mb-5 rounded float-left"
               style="width: 300px; height: 400px; border: 2px solid orange">
            <div class="card-body">
              <span class="mt-3" style="margin-left: 130px"><strong>{{ list.title }}</strong></span>
              <br>
              <span class="float-left card-subtitle">
              <router-link :to="{path:'/profile',query:{nickname:list.nickname}}"><span
                  class="float-left ml-3 mt-1 mr-3"><small>작성자 : {{ list.nickname }}</small></span></router-link>
              <br>
              <span
                  class="float-left ml-3 mt-1 mr-3"> <small>작성일 : {{ list.createDate.substring(0, 10) }}</small></span>
              <br>
              <span class="float-left ml-3 mt-1 mr-3"><small>지역 : {{ list.area }}</small></span>
              </span>
              <br>

              <div id="productListImgDiv" style="height: 100%; width: 100%">
                <router-link :to="{path:'/productDetail',query:{productId:list.id}}">
                  <v-img
                      :src="list.thumbnail"
                      class="mt-15 mr-3 ml-13 grey lighten-3"
                      max-height="200"
                      width="200">

                  </v-img>
                </router-link>
                <div
                    v-if="list.tags.length ===0 "
                    style="height: 34px">
                </div>

                <div v-else id="productTagDiv" class="mt-5">
                  <v-row align-content="center" justify="center">
                    <div v-for="(tags,index) in list.tags" :key="index" style="list-style: none; display: inline">
                      <v-chip
                          class="ml-0 mr-1 pr-2 pl-2"
                          color="info"
                          label
                          small>
                        {{ tags.tag }}
                      </v-chip>
                    </div>
                  </v-row>
                </div>

                <div id="productListIconDiv" class="mt-8">
                  <v-row align-content="center" justify="center">
                    <v-icon
                        color="blue darken-4">
                      mdi-message-text
                    </v-icon>
                    <span class="mt-1">{{ list.readCount }}</span>

                    <v-icon
                        color="green">
                      mdi-image-multiple
                    </v-icon>
                    <span class="mt-1">{{ list.productAlbumCount }}</span>

                    <v-btn
                        v-if="list.like"
                        color="pink"
                        dark
                        icon
                        @click="pushLike(list.id)">

                      <v-icon dark>mdi-heart</v-icon>

                    </v-btn>

                    <v-btn
                        v-else
                        color="grey"
                        dark
                        icon
                        @click="pushLike(list.id)">

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

    <v-fab-transition>
      <v-btn
          bottom
          color="orange lighten -1"
          fab
          fixed
          right
          small
          @click="$vuetify.goTo('#headerDiv')">
        <v-icon>mdi-chevron-double-up</v-icon>
      </v-btn>
    </v-fab-transition>
  </v-app>
</template>

<script>
export default {
  name: "productList",
  data() {
    return {
      showCategoryFlag: false
    }
  },
  methods: {
    showProductList() {
      this.$store.dispatch('REQUEST_GET_ALL_PRODUCTS_BY_CATEGORIES', localStorage.getItem('productCategories'));
    },
    pushLike(productId) {
      this.$store.dispatch('REQUEST_PUSH_INTEREST', productId);
    },
    checked(name) {
      this.categories[name] = !this.categories[name];
      localStorage.setItem('productCategories', JSON.stringify(this.categories));
      this.$store.dispatch('REQUEST_GET_ALL_PRODUCTS_BY_CATEGORIES', localStorage.getItem('productCategories'));
    },
    changeCategoryFlag() {
      this.showCategoryFlag = !this.showCategoryFlag;
    }
  },
  computed: {
    productList() {
      return this.$store.state.productStore.productList;
    },
    categories() {
      return JSON.parse(localStorage.getItem('productCategories'));
    }
  },
  mounted() {
    this.showProductList();
  }
}
</script>
<style scoped>

small {
  color: black;
}

</style>