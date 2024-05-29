$(document).ready(function () {
    let query = location.href.substring(location.href.indexOf('?') + 1);
    let errorDiv = document.getElementById('login-failed');

    // 쿼리 스트링에 error가 넘어오는 경우, 에러 메시지 출력
    if (query === 'error') {
        errorDiv.style.display = 'block';
    } else errorDiv.style.display = 'none';
})