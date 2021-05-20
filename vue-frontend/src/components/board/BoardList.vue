<template>
  <v-app id="headerDiv">

    <p><small v-if="boardList">{{ boardList.length }}개의 동네생활이 있습니다</small></p>

    <div id="boardListDiv" class="row justify-center mt-15">
      <ul v-for="(list,index) in boardList" :key="index"
          style="list-style: none">
        <li id="listDiv">
          <div class="p-5 mb-5 rounded float-left"
               style="width: 300px; height: 400px; border: 2px solid orange">
            <div class="card-body">
              <span style="margin-left: 130px; margin-top: 10px"><strong>{{ list.title }}</strong></span>
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

              <div id="imgDiv"  style="height: 100%; width: 100%">
                <router-link :to="{path:'/boardDetail',query:{boardId:list.id}}">
                  <v-img
                      v-if="list.thumbnail"
                      :src="list.thumbnail"
                      class="mt-15 mr-3 ml-13 grey lighten-3"
                      width="200"
                  >

                  </v-img>
                  <v-textarea
                      v-else
                      background-color="white"
                      class="ml-3 mr-3"
                      style="margin-top: 100px"
                      no-resize
                      outlined
                      readonly="readonly"
                      v-bind:rows="list.content.length/3"
                      v-bind:value="list.content"
                  ></v-textarea>
                </router-link>

              </div>
              <div style="margin-top: 30px">
                <v-row justify="center" align-content="center">

                <v-chip
                    class="ml-0 mr-1 pr-2 pl-2"
                    color="info"
                    label
                    small
                    @click="clickTag(list.category)"
                >
                  {{ list.category}}
                </v-chip>
                </v-row>
              </div>

                <div id="boardListIconDiv" style="margin-top: 50px;">

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
                  {{list.commentCount}}
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
      clickTagFlag: false,
      tagName:''
    }
  },
  methods: {
    showBoardList() {
      this.$store.dispatch('REQUEST_GET_ALL_BOARDS_BY_CATEGORIES', localStorage.getItem('boardCategories'));
    }
  },
  computed: {
    boardList() {
      return this.$store.state.boardStore.boardList;
    },

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