function showLogin() {
    var popup = document.getElementById("myPopup");
    popup.classList.toggle("show");
}


$(document).ready(function () {
    var objForgot = document.getElementById("frmRegForg:lnkFrgPass");
    if (objForgot !== null) {
        objForgot.addEventListener("click", openView);
    }
    function openView() {
        document.getElementById('divForgot').style.display = 'block';
    }

    document.getElementById("btnCancel").addEventListener("click", closeView);
    function closeView() {
        document.getElementById('divForgot').style.display = 'none';
    }
    var frgPass = document.getElementById("divForgot");
    window.onclick = function (event) {
        if (event.target === frgPass) {
            frgPass.style.display = "none";
        }
    };
});
