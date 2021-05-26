function setStorage(tokenInfo){
    sessionStorage.setItem('access_token','Bearer '+tokenInfo.token);
    sessionStorage.setItem('nickname',tokenInfo.nickname);
    sessionStorage.setItem('area',tokenInfo.area);
}

function initStorage(){
    localStorage.clear();
    sessionStorage.clear();
}

export default {setStorage,initStorage}