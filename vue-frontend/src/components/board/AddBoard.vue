<template>
  <v-container>

    <v-row align = "center" class = "fill-height" justify = "center">
      <div id = "uploadBoardDiv" class = "elevation-5" style = "width: 800px; height: 500px">

        <div class = "mt-15 ml-15 mr-15 mb-15">
          <v-text-field
              v-model = "requestData.title"
              label = "게시글의 제목을 입력해주세요.">

          </v-text-field>

          <v-select
              v-model = "requestData.category"
              :items = "categories"
              class = "pt-3 pl-3 pr-3 "
              dense
              hide-details
              item-text = "address"
              label = "카테고리"
              no-data-text = "카테고리를 입력해주세요.">

          </v-select>

          <v-textarea
              v-model = "requestData.content"
              no-resize
              v-bind:label = "this.requestData.area +'에 올릴 게시글 내용을 작성해주세요.'">

          </v-textarea>

          <v-file-input
              chips
              label = "사진 선택"
              multiple
              @change = "selectedFile">

          </v-file-input>


          <v-btn
              class = "float-right"
              color = "success"
              large
              @click = "upload(fileData)">등록
          </v-btn>
        </div>
      </div>
    </v-row>

  </v-container>
</template>

<script>
export default {
  name    : "AddBoard",
  data() {
    return {
      categories : [],
      requestData: {
        area     : '',
        title    : '',
        content  : '',
        nickname : '',
        category : '',
        files    : '',
        hasImages: ''
      },
      fileData   : ''
    }
  },
  methods : {
    addTags() {
      this.requestData.tags.push(this.tagName);
      this.tagName = '';
    },
    deleteTag(tagIdx) {
      this.requestData.tags.splice(tagIdx, 1);
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
    upload(formData) {
      if (formData === '') {
        formData = new FormData;
        formData.set('hasImages', "no");
      }
      formData.set('area', this.requestData.area);
      formData.set('title', this.requestData.title);
      formData.set('content', this.requestData.content);
      formData.set('nickname', this.requestData.nickname);
      formData.set('category', this.requestData.category);

      this.$store.dispatch('REQUEST_ADD_BOARD', formData);

    },
  },
  created() {
    for (let key in JSON.parse(localStorage.getItem('boardCategories'))) {
      this.categories.push(key);
    }

    this.requestData.nickname = sessionStorage.getItem('nickname');
    this.requestData.area = sessionStorage.getItem('area');
  },
  mounted() {
  },
  computed: {}
}
</script>

<style scoped>

</style>