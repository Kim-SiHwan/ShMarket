<template>
  <v-container>

    <v-row align="center" class="fill-height" justify="center" >
      <div id="uploadReviewDiv" class="elevation-5" style="width: 800px; height: 600px" >

        <div class="mt-15 ml-15 mr-15 mb-15">
          <v-text-field
              label="판매글의 제목을 입력해주세요."
              v-model="requestData.title">

          </v-text-field>
          <v-text-field
              label="판매 상품의 가격을 입력해주세요."
              v-model="requestData.price">

          </v-text-field>
          <v-select
              class="pt-3 pl-3 pr-3 "
              hide-details
              :items="categories"
              item-text="address"
              label="카테고리"
              no-data-text="카테고리를 입력해주세요."
              v-model="requestData.category"
              dense
          ></v-select>
          <v-textarea
              no-resize
              v-model="requestData.content"
              label="판매글의 내용을 입력해주세요.">

          </v-textarea>
          <v-file-input
              chips
              multiple
              @change="selectedFile"
              label="사진을 1장 이상 선택해주세요.">

          </v-file-input>
          <v-text-field
              v-if="this.requestData.tags.length<3"
              v-on:keyup.enter="addTags" v-model="tagName" label="태그입력해주세요 [ 3개 까지의 태그만 입력가능합니다 ]"></v-text-field>
          <div v-for="(tag,index) in requestData.tags" :key="index" style="display: inline">
            <v-chip
                class="mr-3"
                close
                label
                small
                @click:close="deleteTag(index)"
                color="info">
              {{tag}}
            </v-chip>
          </div>
          <v-btn
              v-if="this.requestData.tags.length<3"
              color="primary"
              small
              @click="addTags">
            추가
          </v-btn>
        </div>

      </div>
    </v-row>

    <v-btn
        large
        color="success"
        @click="upload(fileData)">등록</v-btn>

  </v-container>

</template>

<script>
export default {
  name: "AddProduct",
  data(){
    return {
      categories:[],
      requestData:{
        area :'',
        title :'',
        content :'',
        username :'',
        nickname :'',
        category :'',
        price: '',
        files :'',
        tags :[]
      },
      tagName: '',
      fileData: ''
    }
  },
  methods:{
    addTags(){
      this.requestData.tags.push(this.tagName);
      this.tagName='';
    },
    deleteTag(tagIdx){
      this.requestData.tags.splice(tagIdx,1);
    },
    selectedFile(event){
      const files = event;
      let formData = new FormData;
      for(let file in files){
        formData.append('files',files[file]);
      }

      this.fileData= formData;
    },
    upload(formData){
      if(formData===''){
        this.$store.commit('SET_SNACK_BAR',{
          msg:'사진을 1장 이상 선택해주세요.',color:'warning'
        });
        return false;
      }
      formData.set('area',this.requestData.area);
      formData.set('title',this.requestData.title);
      formData.set('content',this.requestData.content);
      formData.set('nickname',this.requestData.nickname);
      formData.set('price',this.requestData.price);
      formData.set('category',this.requestData.category);
      formData.set('tags',this.requestData.tags);
      this.$store.dispatch('REQUEST_ADD_PRODUCT',formData);

    },
  },
  created() {
    for(let key in JSON.parse(localStorage.getItem('categories'))){
      this.categories.push(key);
    }

    this.requestData.nickname = sessionStorage.getItem('nickname');
    this.requestData.area = sessionStorage.getItem('area');
  },
  computed:{

  }
}
</script>

<style scoped>

</style>