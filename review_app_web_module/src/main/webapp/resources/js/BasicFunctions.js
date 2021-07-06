function showConfForm() {
    //  var x = document.getElementById("codeConfirmation");
    //   if (x.style.display === "none") {
    //      x.style.display = "block";
    //  }

}

$(document).ready(function () {
    document.getElementById("registrationForm:email").addEventListener("input", checkEmail);
    var email = false;
    var username = false;
    var password = false;
    var nonEmtpyInput = false;
    function checkEmail(evt) {
        var element = evt.currentTarget;
        var regex = /(.+)@(.+){2,}\.(.+){2,}/;
        var match = regex.test(element.value);
        if (match) {
            element.style.borderColor = "green";
            email = true;
        } else {
            element.style.borderColor = "red";
            email = false;
        }
    }
    document.getElementById("registrationForm:username").addEventListener("input", checkUsername);
    function checkUsername(evt) {
        var element = evt.currentTarget;
        if (element.value.length > 3 && element.value.length < 16) {
            element.style.borderColor = "green";
            username = true;
        } else {
            element.style.borderColor = "red";
            username = false;
        }
    }

    document.getElementById("registrationForm:password").addEventListener("input", checkPassword);
    document.getElementById("registrationForm:confPassword").addEventListener("input", checkPassword);
    function checkPassword(evt) {
        var element = evt.currentTarget;
        if (element.value.length > 3 && element.value.length < 20) {
            element.style.borderColor = "green";
            password = true;
        } else {
            element.style.borderColor = "red";
            password = false;
        }
    }
    document.getElementById("registrationForm:firstName").addEventListener("input", checkNotEmpty);
    document.getElementById("registrationForm:lastName").addEventListener("input", checkNotEmpty);
    function checkNotEmpty(evt) {
        var element = evt.currentTarget;
        if (element.value.length > 0) {
            element.style.borderColor = "green";
            nonEmtpyInput = true;
        } else {
            element.style.borderColor = "red";
            nonEmtpyInput = false;
        }
    }
    document.getElementById("registrationForm:btnRegister").addEventListener("click", resetRegValues);
    function resetRegValues() {
        if (!email) {
            document.getElementById("registrationForm:email").value = "";
        }
        if (!username) {
            document.getElementById("registrationForm:username").value = "";
        }
        if (!password) {
            document.getElementById("registrationForm:password").value = "";
            document.getElementById("registrationForm:confPassword").value = "";
        }
    }

    function passwordMatch() {
        var p1 = document.getElementById("registrationForm:password").value;
        var p2 = document.getElementById("registrationForm:confPassword").value;
        return p1 === p2;
    }

    // document.getElementById("registrationForm:btnRegister").addEventListener("click", showCodeInputForm);
    function showCodeInputForm() {
        var form = document.getElementById("div-confirm");
        if (email && username && password && nonEmtpyInput && passwordMatch()) {
            form.style.display = "block";
            //disableInput();
        } else {
            //form.style.display = "none";
        }

    }

    function disableInput() {
        document.getElementById("registrationForm:firstName").disabled = true;
        document.getElementById("registrationForm:lastName").disabled = true;
        document.getElementById("registrationForm:username").disabled = true;
        document.getElementById("registrationForm:password").disabled = true;
        document.getElementById("registrationForm:email").disabled = true;
        document.getElementById("registrationForm:confPassword").disabled = true;
    }

    document.getElementById("codeConfirmationForm:codeResend").addEventListener("click", clearCodeinput);
    function clearCodeinput() {
        document.getElementById("codeConfirmationForm:codeInput").value = "";
    }
});
