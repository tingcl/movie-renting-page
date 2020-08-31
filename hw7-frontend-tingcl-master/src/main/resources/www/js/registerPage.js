function reportRegister(res, status, request){
    console.log("successful register with idm.");
    var email = request.getResponseHeader("email");
    var sessionID = request.getResponseHeader("sessionID");
    var transactionID = request.getResponseHeader("transactionID");

    console.log("email: " + email);
    console.log("sessionID: " + sessionID);
    console.log("transactionID: " + transactionID);
    console.log("status: " + status);

    console.log("Creating request for gateway report");
    $.ajax({
        method: "GET",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
        headers: {
            email: email,
            sessionID: sessionID,
            transactionID: transactionID,
        },
        success: alertRegister
    });
}

function alertRegister(res, status, request){
    if(status == "nocontent"){
        var email = request.getResponseHeader("email");
        var sessionID = request.getResponseHeader("sessionID");
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("report returned status of 204. Waiting for delay time to resend");
        console.log("delay: " + delay);
        setTimeout(function(){
        }, delay);
        $.ajax({
            method: "GET",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
            headers: {
                email: email,
                sessionID: sessionID,
                transactionID: transactionID,
            },
            success: alertRegister
        });
    }
    else{
        if(res.resultCode == 110){
            alert("Register successful. Proceed to login.")
            $(".registerPage").empty();
            $(".filters").empty();
            $(".moviesPage").empty();
            $(".advancedSearch").empty();
            $(".loginPage").empty();
            $(".detailPage").empty();
            $(".accountPage").empty();
            $(".cartPage").empty();
            generateLoginPage();
        }
        else{
            if(res.resultCode == 16){
                alert("User already exists. Redirecting to login.")
                generateLoginPage();
            }
            else{
                alert("Register failed. Try again.")
                document.getElementById('register-form').reset();
            }
        }
    }
}

function generateRegisterPage(){
    var registerDom = $('.registerPage');
    $('.loginPage').empty();
    $(".advancedSearch").empty();
    $('.moviesPage').empty();
    $(".detailPage").empty();
    $(".accountPage").empty();
    $(".filters").empty();
    $(".cartPage").empty();
    $(".orderPage").empty();

    registerDom.empty();
    document.getElementById("basic").reset();
    console.log("Creating register page dynamically.");
    var rowHTML = "<h2 class='register-header'>Glad to have new users!</h2>" +
        "<p class='register-body'>" +
        "<h3>Input information below to register.<h3>" +
        "<form id='register-form'>" +
        "<input class='register-email' type='text' placeholder='Enter an email ...'/><br>" +
        "<input class='register-password' id='input2' type='password' placeholder='Enter a password ...'/><br>" +
        "<input type='checkbox' onclick='showPasswords()'>Show Password" +
        "<button class='register-btn' type='submit'>register</button>" +
        "</form>" +
        "<p/>";
    registerDom.append(rowHTML)
    $("#register-form").submit(function (event) {
            event.preventDefault();
            console.log("received request to register user.");
            var email = $(".register-email").val();
            var password = $(".register-password").val();
            console.log(email);
            console.log(Array.from(password));
            $.ajax({
                method: "POST",
                url: "http://andromeda-70.ics.uci.edu:8216/api/g/idm/register",
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    "email": email,
                    "password": Array.from(password)
                }),
                success: reportRegister
            });
        }
    );
}

$(".index-register").click(function (event) {
        event.preventDefault();
        generateRegisterPage();
        console.log("Successfully generated register page.");
    }
);
function showPasswords() {
    var x = document.getElementById('input2');
    if (x.type === "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
}


