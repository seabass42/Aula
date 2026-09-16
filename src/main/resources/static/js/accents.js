// Inserts a Spanish character at the caret of the answer field, without
// clobbering whatever the student already typed.
document.addEventListener('DOMContentLoaded', function () {
  var field = document.getElementById('answer');
  if (!field) return;

  document.querySelectorAll('.key').forEach(function (key) {
    key.addEventListener('click', function () {
      var ch = key.dataset.char;
      var start = field.selectionStart;
      var end = field.selectionEnd;

      field.value = field.value.slice(0, start) + ch + field.value.slice(end);
      field.focus();
      field.selectionStart = field.selectionEnd = start + ch.length;
    });
  });
});
