function addNum(num) {
  const input = document.getElementById("inputBox");

  // HTMLから埋め込んだ値取得
  const maxLen = input.getAttribute("data-len");

  if (input.value.length >= maxLen) return;

  // 重複防止
  if (input.value.includes(num)) return;

  input.value += num;
}

function backspace() {
  const input = document.getElementById("inputBox");
  input.value = input.value.slice(0, -1);
}
