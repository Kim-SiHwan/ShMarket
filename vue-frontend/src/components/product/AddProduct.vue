<template>
  <v-container>

    <v-row align="center" class="fill-height" justify="center">
      <div id="uploadProductDiv" class="elevation-5" style="width: 800px; height: 600px">

        <div class="mt-15 ml-15 mr-15 mb-15">

          <v-text-field
              v-model="requestData.title"
              label="판매글의 제목을 입력해주세요.">

          </v-text-field>

          <v-text-field
              v-model="requestData.price"
              label="판매 상품의 가격을 입력해주세요 ( 선택사항 )">

          </v-text-field>

          <v-select
              v-model="requestData.category"
              :items="categories"
              class="pt-3 pl-3 pr-3 "
              dense
              hide-details
              item-text="address"
              label="카테고리"
              no-data-text="카테고리를 입력해주세요.">

          </v-select>

          <v-textarea
              v-model="requestData.content"
              no-resize
              v-bind:label="this.requestData.area +'에 올릴 게시글 내용을 작성해주세요.'">

          </v-textarea>

          <v-file-input
              chips
              label="사진 선택"
              multiple
              @change="selectedFile">

          </v-file-input>

          <v-text-field
              v-if="this.requestData.tags.length<3"
              v-model="tagName" label="태그를 입력해주세요 [ 최대 3개 ]" v-on:keyup.enter="addTags">

          </v-text-field>

          <div v-for="(tag,index) in requestData.tags" :key="index" style="display: inline">

            <v-chip
                class="mr-3"
                close
                color="info"
                label
                small
                @click:close="deleteTag(index)">
              {{ tag }}

            </v-chip>
          </div>

          <v-btn
              v-if="this.requestData.tags.length<3"
              color="primary"
              small
              @click="addTags">
            태그 추가
          </v-btn>
        </div>

        <v-btn
            class="float-right"
            color="success"
            large
            @click="upload(fileData)">등록
        </v-btn>
      </div>
    </v-row>

  </v-container>
</template>

<script>
export default {
  name: "AddProduct",
  data() {
    return {
      categories: [],
      requestData: {
        area: '',
        title: '',
        content: '',
        username: '',
        nickname: '',
        category: '',
        price: '',
        files: '',
        tags: []
      },
      tagName: '',
      fileData: ''
    }
  },
  methods: {
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
      formData.set('price', this.requestData.price);
      formData.set('category', this.requestData.category);
      formData.set('tags', this.requestData.tags);
      this.$store.dispatch('REQUEST_ADD_PRODUCT', formData);

    },
  },
  created() {
    for (let key in JSON.parse(localStorage.getItem('productCategories'))) {
      this.categories.push(key);
    }

    this.requestData.nickname = sessionStorage.getItem('nickname');
    this.requestData.area = sessionStorage.getItem('area');
  },
  computed: {}
}
</script>

<style scoped>

</style>