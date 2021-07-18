export const mixinData={
    methods:{
        displayedAt(createdAt) {
            console.log("MIXIN! date");
            createdAt = new Date(createdAt);
            const milliSeconds = new Date() - createdAt
            const seconds = milliSeconds / 1000
            if (seconds < 60) {
                return `방금 전`
            }
            const minutes = seconds / 60
            if (minutes < 60) {
                return `${Math.floor(minutes)}분 전`
            }
            const hours = minutes / 60
            if (hours < 24) {
                return `${Math.floor(hours)}시간 전`
            }
            const days = hours / 24
            if (days < 7) {
                return `${Math.floor(days)}일 전`
            }
            const weeks = days / 7
            if (weeks < 5) {
                return `${Math.floor(weeks)}주 전`
            }
            const months = days / 30
            if (months < 12) {
                return `${Math.floor(months)}개월 전`
            }
            const years = days / 365
            return `${Math.floor(years)}년 전`
        },
        changeCategories(type, categories){
            let data ={
                page :'',
                category:''
            };
            if(type === 'board') {
                localStorage.setItem('boardCategories', JSON.stringify(categories));
                data.page =1;
                data.category = localStorage.getItem('boardCategories');
                this.$store.dispatch('REQUEST_GET_ALL_BOARDS_PAGES', data);

            }else{
                localStorage.setItem('productCategories', JSON.stringify(categories));
                data.page=1;
                data.category = localStorage.getItem('productCategories');
                this.$store.dispatch('REQUEST_GET_ALL_PRODUCTS_PAGES', data);
            }
        },
        mounted(){
            console.log("MIXIN! mount");
        }
    }
}