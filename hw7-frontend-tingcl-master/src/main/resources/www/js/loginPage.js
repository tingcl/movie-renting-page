// Call to create login page
$('.index-login').click(function (event) {
        event.preventDefault();
        console.log("Someone pressed login.");
        generateLoginPage();
    }
);
// Creates html for login page
function generateLoginPage(){
    // Create login dom
    var loginDom = $(".loginPage");
    // Empty preexisting pages
    $(".registerPage").empty();
    $(".moviesPage").empty();
    $(".advancedSearch").empty();
    $(".filters").empty();
    $(".detailPage").empty();
    $(".orderPage").empty();
    loginDom.empty();
    document.getElementById("basic").reset();
    console.log("Creating login page dynamically");
    var rowHTML =
        "<h2 class='login-header'>Good to see you again!</h2>" +
        "<p class='login-body'>" +
        "<h3>Login to get started.</h3>" +
        "<img class='avatar' src='images/avatar.png'/><br>" +
        "<form id='login-form'>" +
        "<input class='login-email' id='input1' type='text' placeholder='Enter email ...'/><br>" +
        "<input class='login-password' id='input' type='password' placeholder='Enter password ...'/><br>" +
        "<input type='checkbox' onclick='showPassword()'>Show Password" +
        "<button class='login-btn' type='submit'>login</button>" +
        "</form>" +
        "Don't have account? Register <a class='goRegister'>here</a>" +
        "</p>";
    loginDom.append(rowHTML);
    // Register button in login page
    $('.goRegister').click(function (event) {
        event.preventDefault();
        console.log("No account. Going to register.");
        generateRegisterPage();
    });
    // User pressed login
    $("#login-form").submit(function (event) {
        event.preventDefault();
        console.log("received request to login user.");
        var email = $(".login-email").val();
        var password = $(".login-password").val();
        eId = email;
        console.log(email);
        console.log(Array.from(password));
        $.ajax({
            method: "POST",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/idm/login",
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                "email": email,
                "password": Array.from(password)
            }),
            success: reportLogin
        });

    });
}
// grabbing report from api gateway
function reportLogin(res, status, request){
    console.log("successful login with idm.");
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
        success: alertLogin
    });
}
// Checking what alert banner to show
function alertLogin(res, status, request){
    if(status == "nocontent"){
        var email = request.getResponseHeader("email");
        var sessionID = request.getResponseHeader("sessionID");
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("report returned status of 204. Waiting for delay time to resend");
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
            success: alertLogin
        });
    }
    else{
        sId = res.sessionID;
        if(res.resultCode == 120){
            alert("Login successful")
            $(".registerPage").empty();
            $(".moviesPage").empty();
            $(".advancedSearch").empty();
            $(".loginPage").empty();
            $(".filters").empty();
            $(".detailPage").empty();
            $(".orderPage").empty();
        }
        else{
            if(res.resultCode == 14){
                alert("Unregistered user please enter valid account.")
                document.getElementById('login-form').reset();
            }
            if(res.resultCode == 12){
                alert("passwords do not match.")
                document.getElementById('login-form').reset();
            }
            else{
                alert("wrong password.")
                document.getElementById('login-form').reset();
            }
        }
    }
}

// Helper function for showing password or not
function showPassword() {
    var x = document.getElementById('input');
    if (x.type === "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
}


