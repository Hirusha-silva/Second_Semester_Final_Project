$(document).ready(function () {

    document.getElementById("goToRegister").addEventListener("click", () => {
        const registerTabButton = document.getElementById("register-tab");
        new bootstrap.Tab(registerTabButton).show();
    });

    document.getElementById("goToLogin").addEventListener("click", () => {
        const loginTabButton = document.getElementById("login-tab"); // tab trigger
        new bootstrap.Tab(loginTabButton).show();
    });

    $("#registerBtn").on('click',function (e){
        e.preventDefault();

        const nameu = $('#regName').val();
        const emailu = $('#regEmail').val();
        const usernameu = $('#regUsername').val();
        const addressu = $('#regAddress').val();
        const phoneu = $('#regNumber').val();
        const passwordu = $('#regPassword').val();
        const confirmPasswordu = $('#regConfirmPassword').val();
        const roleu = "USER";

        if (passwordu !== confirmPasswordu ){
            new Noty({
                type: "error",
                layout: "topRight",
                text: "Password not match.",
                timeout: 3000
            }).show();
            return;
        }

        const data = {
            name:nameu,
            email:emailu,
            address:addressu,
            username:usernameu,
            password:passwordu,
            phone:phoneu,
            role:roleu
        }

        $.ajax({
            url:'http://localhost:8080/auth/register',
            method:'POST',
            contentType: 'application/json',
            data:JSON.stringify(data),
            success:function (response){
                alert('Success');
                document.getElementById("goToLogin").click();
            },
            error: function (xhr) {
                alert("Sign up failed: " + xhr.responseText);
            }
        })

    })

});