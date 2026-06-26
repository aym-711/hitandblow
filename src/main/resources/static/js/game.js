function addNum(num) {
  const input = document.getElementById("inputBox");
  const error = document.getElementById("errorMessage");

  const maxLen = input.getAttribute("data-len");

  // エラーメッセージ初期化
  error.textContent = "";

  if (input.value.length >= maxLen) return;

  // 重複チェック
  if (input.value.includes(num)) {
    error.textContent = "同じ数字は使えません！";
    return;
  }

  input.value += num;
}

function backspace() {
  const input = document.getElementById("inputBox");
  const error = document.getElementById("errorMessage");

  input.value = input.value.slice(0, -1);

  // 消したらエラーもリセット
  error.textContent = "";
}
