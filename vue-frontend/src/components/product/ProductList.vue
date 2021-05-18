<template>
  <v-app id="headerDiv">

    <p><small v-if="productList">{{ productList.length }}개의 상품이 있습니다</small></p>

    <div id="productListDiv" class="row justify-center mt-15">
      <ul v-for="(list,index) in productList" :key="index"
          style="list-style: none">
        <li id="listDiv">
          <div class="p-5 mb-5 rounded float-left"
               style="width: 500px; height: 600px; border: 1px solid cornflowerblue">
            <div class="card-body" style="margin-top: 20px">
              <span><strong>&lt;&nbsp; {{ list.title }} &gt;&nbsp;</strong></span>
              <br>
              <span class="float-right card-subtitle">
              <span class="float-right mt-1 mr-3"><small>작성자 : {{ list.nickname }}</small></span>
              <br>
              <span class="float-right mt-1 mr-3"> <small>작성일 : {{
                  list.createDate.substring(0, 10)
                }}</small></span>
              <br>
              <span class="float-right mt-1 mr-3"><small>지역 : {{ list.area }}</small></span>
              </span>
              <br>

              <div id="imgDiv" style="height: 100%; width: 100%">
                <router-link :to="{path:'/reviewDetail',query:{reviewId:list.id}}">
                  <v-img
                      :src="list.thumbnail"
                      aspect-ratio="1"
                      class="mt-15 mr-3 ml-13 grey lighten-3"
                      width="400"
                  >

                  </v-img>
                </router-link>

                <div v-for="(tags,index) in list.tags" :key="index" style="list-style: none; display: inline">
                  <v-chip
                      class="ml-0 mr-1 pr-2 pl-2"
                      color="info"
                      label
                      small>
                    {{ tags.tag }}
                  </v-chip>
                </div>

                <div id="reviewListIconDiv" class="mt-4 mb-5">
                  <v-icon
                      color="blue darken-4">
                    mdi-message-text
                  </v-icon>
                  {{ list.readCount }}

                  <v-icon
                      color="green">
                    mdi-image-multiple
                  </v-icon>
                  {{ list.productAlbumCount }}
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
          right
          fixed
          fab
          small
          color="orange lighten -1"
          @click="$vuetify.goTo('#headerDiv')">
        <v-icon>mdi-chevron-double-up</v-icon>
      </v-btn>
    </v-fab-transition>


  </v-app>
</template>

<script>
export default {
  name: "productList",
  methods: {
    showProductList() {
      this.$store.dispatch('REQUEST_GET_ALL_PRODUCTS_BY_CATEGORIES', localStorage.getItem('categories'));
    },
    clickTag(tag) {
      this.$store.dispatch('REQUEST_GET_ALL_REVIEWS_BY_TAG', tag);
    }

  },
  computed: {
    productList() {
      return this.$store.state.productStore.productList;
    },
    productDetail() {
      return this.$store.state.productStore.productDetail;
    },
    categories(){
      return this.$store.getters.GET_CATEGORIES;
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