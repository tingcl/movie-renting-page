var re;

$(".index-account").click(function(event){
    event.preventDefault();
    console.log("generating my account page.")
    if(userIsLoggedIn(eId, sId)){
        generateAccountPage();
    }
    else{
        alert("You are not logged in. Redirecting to login.");
        generateLoginPage();
    }
});

function generateAccountPage(){
    // Creating account DOM
    var accountDom = $(".accountPage");
    // Empty pre existing dynamic divs
    accountDom.empty();
    $(".loginPage").empty();
    $(".registerPage").empty();
    $(".moviesPage").empty();
    $(".detailPage").empty();
    $(".advancedSearch").empty();
    $(".cartPage").empty();
    $(".filters").empty();
    $(".orderPage").empty();
    // Empty quick search field
    document.getElementById("basic").reset();

    var HTML = "<h1>Your Account</h1>" +
        "<button type='submit' id='orderHistory'><h3>Your Orders</h3><h4>View list of previously ordered items</h4></button>" +
        "<button type='submit' id='currentCart'><h3>Your Cart</h3><h4>Proceed to check out or continue shopping</h4></button>";
    accountDom.append(HTML);
    $("#orderHistory").click(function(event) {
        console.log("retrieving order history information");
        $.ajax({
            method: "POST",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/billing/order/retrieve",
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                "email": eId,
            }),
            headers:{
                "email": eId,
                "sessionID": sId
            },
            success: reportOrderHistory
        });
    });
    $("#currentCart").click(function(event) {
        console.log("retrieving cart information");
        console.log(eId);
        console.log(sId);
        $.ajax({
            method: "POST",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/billing/cart/retrieve",
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                "email": eId,
            }),
            headers:{
                "email": eId,
                "sessionID": sId
            },
            success: reportCartItems
        });
    });
}
function reportOrderHistory(res, status, request){
    var email = request.getResponseHeader("email");
    var sessionID = request.getResponseHeader("sessionID");
    var transactionID = request.getResponseHeader("transactionID");
    console.log("email: " + email);
    console.log("sessionID: " + sessionID);
    console.log("transactionID: " + transactionID);
    console.log("status: " + status);
    console.log("Creating request for cart information to gateway report");
    $.ajax({
        method: "GET",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
        headers: {
            email: email,
            sessionID: sessionID,
            transactionID: transactionID,
        },
        success: generateOrderHistory
    });
}
function reportCartItems(res, status, request){
    var email = request.getResponseHeader("email");
    var sessionID = request.getResponseHeader("sessionID");
    var transactionID = request.getResponseHeader("transactionID");
    console.log("email: " + email);
    console.log("sessionID: " + sessionID);
    console.log("transactionID: " + transactionID);
    console.log("status: " + status);
    console.log("Creating request for cart information to gateway report");
    $.ajax({
        method: "GET",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
        headers: {
            email: email,
            sessionID: sessionID,
            transactionID: transactionID,
        },
        success: generateCartItems
    });
}

function generateCartItems(res, status, request){
    if(status == "nocontent"){
        var email = request.getResponseHeader("email");
        var sessionID = request.getResponseHeader("sessionID");
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("Gateway report returned status of 204. Waiting " + delay +" seconds to resend request");
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
            success: generateCartItems
        });
    }
    else{
        if(res.resultCode == 312){
            console.log("empty cart");
            $(".loginPage").empty();
            $(".registerPage").empty();
            $(".advancedSearch").empty();
            $(".detailPage").empty();
            $(".accountPage").empty();
            $(".filters").empty();
            $(".moviesPage").empty();
            $(".orderPage").empty();
            var cartDom =  $(".cartPage");
            cartDom.empty();
            var HTML = "<h2>Your Cart</h2><h4>You currently have no items.<br>Continue browsing <a class='searchMovies'>here</a></h4>"
            cartDom.append(HTML);
            $('.searchMovies').click(function (event) {
                event.preventDefault();
                console.log("clicked");
                console.log("going to advanced.");
                generateAdvancedPage();
            });
        }
        else{
            var itemList = res.items;
            $(".loginPage").empty();
            $(".registerPage").empty();
            $(".advancedSearch").empty();
            $(".detailPage").empty();
            $(".accountPage").empty();
            $(".filters").empty();
            $(".moviesPage").empty();
            $(".orderPage").empty();
            var cartDom =  $(".cartPage");
            cartDom.empty();
            var HTML = "<h3>Your Cart</h3>" +
                       "<table border='1'><tr><td>Movie ID</td><td>Quantity</td><td>Update</td><td>Remove</td>";
            for (var i = 0; i < itemList.length; ++i) {
                HTML += "<tr>";
                var itemObject = itemList[i];
                HTML += "<td>" + itemObject["movieId"] + "</td>" +
                        "<td>" + itemObject["quantity"] + "</td>" +
                        '<td>' +
                        '<input type="text" class="updateQuantity" placeholder="Enter new quantity"/>' +
                        '<button id="n" type="submit" onclick="updateQ(\'' + itemObject["movieId"] + '\')">update</button>' +
                        '</td>' +
                        '<td><button id="r" type="submit" onclick="removeQ(\'' + itemObject["movieId"] + '\')" >remove</button></td>' +
                        '</tr>'
            }
            HTML += "<button id='all' type='submit' onclick='removeAll()'>Clear shopping cart</button>" +
                    "<button id='checkout' type='submit' onclick='checkout()'>Place order</button>" +
                    "<button id='check' type='submit'>Check out</button>"
            cartDom.append(HTML);
            $("#check").click(function (event) {
                event.preventDefault();
                console.log(re);
                window.open(re,'_blank');
                $.ajax({
                    method: "POST",
                    url: "http://andromeda-70.ics.uci.edu:8216/api/g/billing/cart/clear",
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "email": eId,
                    }),
                    headers:{
                        "email": eId,
                        "sessionID": sId
                    },
                    success: clearReport
                });
                $(".cartPage").empty();
                generateAccountPage();
            });


        }
    }


}
function removeQ(movieId){
    console.log("received request to remove item");
    $.ajax({
        method: "POST",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/billing/cart/delete",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "email": eId,
            "movieId": movieId,
        }),
        headers:{
            "email": eId,
            "sessionID": sId
        },
        success: deleteReport
    });
}
function deleteReport(res, status, request){
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
        success: newCartPage
    });
}
function updateQ(movieId){
    console.log("received request to update shopping cart quantity");
    console.log($(".updateQuantity").val());
    $.ajax({
        method: "POST",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/billing/cart/update",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "email": eId,
            "movieId": movieId,
            "quantity": $(".updateQuantity").val()
        }),
        headers:{
            "email": eId,
            "sessionID": sId
        },
        success: updateReport
    });
}
function updateReport(res, status, request){
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
        success: newCartPage
    });
}
function newCartPage(res, status, request){
    var email = request.getResponseHeader("email");
    var sessionID = request.getResponseHeader("sessionID");
    var transactionID = request.getResponseHeader("transactionID");
    console.log("email: " + email);
    console.log("sessionID: " + sessionID);
    console.log("transactionID: " + transactionID);
    console.log("status: " + status);
    console.log("Creating request for cart information to gateway report");
    $.ajax({
        method: "GET",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
        headers: {
            email: email,
            sessionID: sessionID,
            transactionID: transactionID,
        },
        success: generateNewCart
    });
}
function generateNewCart(res, status, request){
    if(status == "nocontent"){
        var email = request.getResponseHeader("email");
        var sessionID = request.getResponseHeader("sessionID");
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("Gateway report returned status of 204. Waiting " + delay +" seconds to resend request");
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
            success: generateNewCart
        });
    }
    else{
        $.ajax({
            method: "POST",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/billing/cart/retrieve",
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                "email": eId,
            }),
            headers:{
                "email": eId,
                "sessionID": sId
            },
            success: reportCartItems
        });
    }
}
function removeAll(){
    console.log("received request to clear shopping cart quantity");
    $.ajax({
        method: "POST",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/billing/cart/clear",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "email": eId,
        }),
        headers:{
            "email": eId,
            "sessionID": sId
        },
        success: clearReport
    });

}
function clearReport(res, status, request){
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
        success: newCartPage
    });
}
function checkout(){
    console.log("beginning check out");
    $.ajax({
        method: "POST",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/billing/order/place",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "email": eId,
        }),
        headers:{
            "email": eId,
            "sessionID": sId
        },
        success: reportCheckOut
    });
}
function reportCheckOut(res, status, request){
    var email = request.getResponseHeader("email");
    var sessionID = request.getResponseHeader("sessionID");
    var transactionID = request.getResponseHeader("transactionID");

    console.log("email: " + email);
    console.log("sessionID: " + sessionID);
    console.log("transactionID: " + transactionID);
    console.log("status: " + status);

    console.log("[Check out] Creating request for gateway report");
    $.ajax({
        method: "GET",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
        headers: {
            email: email,
            sessionID: sessionID,
            transactionID: transactionID,
        },
        success: checkOutAlert
    });
}
function checkOutAlert(res, status, request){
    if(status == "nocontent"){
        var email = request.getResponseHeader("email");
        var sessionID = request.getResponseHeader("sessionID");
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("Gateway report returned status of 204. Waiting " + delay +" seconds to resend request");
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
            success: checkOutAlert
        });
    }
    else{
        console.log("HERHEHREHRHEHRHERHEHREHRHEREHHREHREHRHEHREHRHEHRERHERH")
        console.log(res)
        console.log(res.redirectURL);
        re = res.redirectURL;
    }
}

function generateOrderHistory(res, status, request){
    if(status == "nocontent"){
        var email = request.getResponseHeader("email");
        var sessionID = request.getResponseHeader("sessionID");
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("Gateway report returned status of 204. Waiting " + delay +" seconds to resend request");
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
            success: generateOrderHistory
        });
    }
    else {
        console.log(res);
        if (res.resultCode != 3410) {
            console.log("empty orders");
            $(".loginPage").empty();
            $(".registerPage").empty();
            $(".advancedSearch").empty();
            $(".detailPage").empty();
            $(".accountPage").empty();
            $(".filters").empty();
            $(".moviesPage").empty();
            $(".cartPage").empty();
            var orderDom = $(".orderPage");
            orderDom.empty();
            var HTML = "<h2>Your orders</h2><h4>You currently have no orders.</h4>";
            orderDom.append(HTML);
        }
        else{
            console.log(res);
            $(".loginPage").empty();
            $(".registerPage").empty();
            $(".advancedSearch").empty();
            $(".detailPage").empty();
            $(".accountPage").empty();
            $(".filters").empty();
            $(".moviesPage").empty();
            $(".cartPage").empty();
            var orderDom = $(".orderPage");
            orderDom.empty();
            var transactionList = res.transactions;
            var HTML = "<h3>List of past orders</h3>"
            for (var i = 0; i < transactionList.length; ++i) {
                var transactionObject = transactionList[i];
                HTML += "<table border='1'><tr><td>transaction ID</td><td>" + transactionObject['transactionId'] + "</td></tr>" +
                        "<tr><td>Total Price $</td><td>" + transactionObject['amount'].total + "</td></tr>" +
                        "<tr><td>Date</td><td>" + transactionObject['create_time'] + "</td></tr>" +
                        "<tr><td>PayPal Service Fees</td><td>" + transactionObject['transaction_fee'].value + "</td></tr>";
                var itemList = transactionObject["items"];
                HTML += "<tr><td>Items</td><td><p>";
                for(var i = 0; i < itemList.length; ++i) {
                    var itemObject = itemList[i];
                    HTML += "Movie id: " + itemObject['movieId'] + " Quantity: " + itemObject['quantity'] + " Price: " + itemObject["unit_price"] + " Discount: " + itemObject['discount'] + "<br>"
                }
                HTML += "</p></td></tr>" + "<tr><td>State</td><td>" + transactionObject['state'] + "</td></tr><br>"
            }
            orderDom.append(HTML);

        }
    }
}
