function setStorage(tokenInfo) {
    sessionStorage.setItem('access_token', 'Bearer ' + tokenInfo.token);
    sessionStorage.setItem('nickname', tokenInfo.nickname);
    sessionStorage.setItem('username', tokenInfo.username);
    sessionStorage.setItem('area', tokenInfo.area);
}

function initStorage() {
    sessionStorage.clear();
}

export default {setStorage, initStorage}