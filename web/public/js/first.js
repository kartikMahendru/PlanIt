function testJS() {
    var b = document.getElementById('form104').value,
        url ='./form.html?name=' + encodeURIComponent(b);

    document.location.href = url;
}
