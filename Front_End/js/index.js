$(document).ready(function () {

    document.getElementById("goToRegister").addEventListener("click", () => {
        const registerTabButton = document.getElementById("register-tab");
        new bootstrap.Tab(registerTabButton).show();
    });

    document.getElementById("goToLogin").addEventListener("click", () => {
        const loginTabButton = document.getElementById("login-tab"); // tab trigger
        new bootstrap.Tab(loginTabButton).show();
    });

    //register
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

    //login
    $("#loginBtn").on('click',function (e){
        e.preventDefault();

        const userNamer = $("#loginEmail").val();
        const passwordr = $("#loginPassword").val();

        const loginData = {
            username: userNamer,
            password: passwordr
        };

        $.ajax({
            url: "http://localhost:8080/auth/login",
            method:'POST',
            contentType:'application/json',
            data:JSON.stringify(loginData),
            success:function (res){
                 const token = res.data.accessToken;
                 const userId = res.data.userId;
                 const userName = res.data.username;
                if (token) localStorage.setItem("token", token);
                if (userId !== undefined && userId !== null) localStorage.setItem("userId", userId.toString());
                if (userName) localStorage.setItem("username", userName);

                console.log("Login success. Token:",token);
                console.log(userId);

                setTimeout(()=>{
                    redirectBasedOnRole(token);
                },100);
            }
        })
    })

    function redirectBasedOnRole(token){
        //fist check user role
        $.ajax({
            url:"http://localhost:8080/hello/user",
            method:"GET",
            headers:{
                "Authorization": "Bearer " + token
            },
            success:function (){
                window.location.href = "pages/user.html";
            },
            error:function (){
                //Then check ADMIN role
                $.ajax({
                    url:"http://localhost:8080/hello/admin",
                    method:"GET",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    success:function (){
                        window.location.href = "pages/admin.html";
                    },
                    error:function (){
                        alert("Access denied: No role match");
                    }
                })
            }

        })
    }

});