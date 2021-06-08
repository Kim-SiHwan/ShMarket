<template>
  <v-app id="headerDiv">


    <p><small v-if="boardList" >{{ boardList.length }}개의 동네생활이 있습니다</small></p>

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

    <div style="margin: auto">
      <v-chip
          v-if="clickTagFlag"
          color="info"
          label
          style="width: 320px"
          close
          @click:close="closeTag"
      >
        {{ tagName }} 카테고리 게시글만 출력합니다
      </v-chip>

    </div>


    <div id="boardListDiv" class="row justify-center mt-15">
      <ul v-for="(list,index) in boardList" :key="index"
          style="list-style: none">
        <li id="listDiv">
          <div class="p-5 mb-5 rounded float-left"
               style="width: 300px; height: 400px; border: 2px solid orange">
            <div class="card-body">
              <span class="mt-3" style="margin-left: 130px"><strong>{{ list.title }}</strong></span>
              <br>
              <span class="float-left card-subtitle">
              <span class="float-left ml-3 mt-1 mr-3"><small>작성자 : {{ list.nickname }}</small></span>
              <br>
              <span class="float-left ml-3 mt-1 mr-3"> <small>작성일 : {{
                  list.updateDate.substring(0, 10)
                }}</small></span>
              <br>
              <span class="float-left ml-3 mt-1 mr-3"><small>지역 : {{ list.area }}</small></span>
              </span>
              <br>

              <div id="imgDiv" style="height: 100%; width: 100%">
                <router-link :to="{path:'/boardDetail',query:{boardId:list.id}}">
                  <v-img
                      :src="list.thumbnail"
                      class="mt-15 mr-3 ml-13 grey lighten-3"
                      contain
                      width="200"
                      max-height="100"
                  >

                  </v-img>
                  <v-textarea
                      background-color="white"
                      class="ml-3 mr-3 mt-5"
                      no-resize
                      outlined
                      readonly="readonly"
                      rows="2"
                      v-bind:value="list.content.substring(0,15)"
                  ></v-textarea>
                </router-link>

              </div>
              <div class="mt-3">
                <v-row justify="center" align-content="center">

                  <v-chip
                      class="ml-0 mr-1 pr-2 pl-2"
                      color="info"
                      label
                      small
                      @click="clickTag(list.category)"
                  >
                    {{ list.category }}
                  </v-chip>
                </v-row>
              </div>

              <div id="boardListIconDiv" class="mt-8">

                <v-row justify="center" align-content="center">


                  <v-icon
                      color="blue darken-4">
                    mdi-message-text
                  </v-icon>
                  {{ list.readCount }}

                  <v-icon
                      color="green">
                    mdi-image-multiple
                  </v-icon>
                  {{ list.boardAlbumCount }}

                  <v-icon
                      color="red">
                    mdi-message-text
                  </v-icon>
                  {{ list.commentCount }}
                </v-row>

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
  name: "boardList",
  data() {
    return {
      showCategoryFlag: false,
      clickTagFlag: false,
      tagName: ''
    }
  },
  methods: {
    showBoardList() {
      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_BY_CATEGORIES', localStorage.getItem('boardCategories'));
    },
    checked(name) {
      this.categories[name] = !this.categories[name];
      localStorage.setItem('boardCategories', JSON.stringify(this.categories));
      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_BY_CATEGORIES', localStorage.getItem('boardCategories'));
    },
    changeCategoryFlag() {
      this.showCategoryFlag = !this.showCategoryFlag;
    },
    clickTag(name) {
      console.log(name);
      this.tagName = name;
      this.clickTagFlag = true;
      let toJson = {}
      toJson[name] = true;
      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_BY_CATEGORIES', JSON.stringify(toJson));
    },
    closeTag() {
      this.clickTagFlag = false;
      this.showBoardList();
    }
  },
  computed: {
    boardList() {
      return this.$store.state.boardStore.boardList;
    },

    categories() {
      return JSON.parse(localStorage.getItem('boardCategories'));
    }
  },
  mounted() {
    this.showBoardList();
  }
}
</script>

<style scoped>

small {
  color: black;
}

</style>