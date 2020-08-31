var movieid;

function detailReport(res, status, request)
{
    var email = request.getResponseHeader("email");
    var sessionID = request.getResponseHeader("sessionID");
    var transactionID = request.getResponseHeader("transactionID");

    console.log("email: " + email);
    console.log("sessionID: " + sessionID);
    console.log("transactionID: " + transactionID);

    console.log("Sending report request to API gateway Responses table.");
    $.ajax({
        method: "GET",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
        headers: {
            email: email,
            sessionID: sessionID,
            transactionID: transactionID,
        },
        success: generateDetailPage
    });
}


function generateDetailPage(res, status, request){
    if(status == "nocontent"){
        var email = request.getResponseHeader("email");
        var sessionID = request.getResponseHeader("sessionID");
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("Gateway report returned status of 204. Waiting " + delay +" seconds to resend request");
        console.log("   transactionID: " + transactionID);
        console.log("   email: " + email);
        console.log("   sessionID: " + sessionID);
        /* suspending until delay time */
        setTimeout(function(){
            console.log("timeout...");
        }, delay);
        /* sending request for gateway report again */
        $.ajax({
            method: "GET",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
            headers: {
                email: email,
                sessionID: sessionID,
                transactionID: transactionID,
            },
            success: generateDetailPage
        });
    }
    else{
        // Creating dynamic DOM
        var detailDom = $(".detailPage");
        // --- Clearing all dynamic divs ---
        $(".loginPage").empty();
        $(".registerPage").empty();
        $(".moviesPage").empty();
        $(".advancedSearch").empty();
        $(".accountPage").empty();
        $(".filters").empty();
        $(".orderPage").empty();
        detailDom.empty();
        // ----------------------------------
        console.log(res);
        var movieDetail=res.movies;
        movieid = movieDetail['movieId'];
        var HTML = "<h2>Movie Details</h2>";
        HTML += "<div id='pleaseFormat'>"
        HTML += "<table border='1'><tr><td>Movie ID</td><td>" + movieDetail['movieId'] + "</td></tr>";
        HTML += "<tr><td>Title</td><td>" + movieDetail['title'] + "</td></tr>";
        HTML += "<tr><td>Director</td><td>" + movieDetail['director'] + "</td></tr>";
        HTML += "<tr><td>Year</td><td>" + movieDetail['year'] + "</td></tr>";
        HTML += "<tr><td>Budget</td><td>" + movieDetail['budget'] + "</td></tr>";
        HTML += "<tr><td>Overview</td><td>" + movieDetail['overview'] + "</td></tr>";
        HTML += "<tr><td>Revenue</td><td>" + movieDetail['revenue'] + "</td></tr>";
        HTML += "<tr><td>Rating</td><td>" + movieDetail['rating'] + "</td></tr>";
        HTML += "<tr><td>Number of votes</td><td>" + movieDetail['numVotes'] + "</td></tr>";

        var genreList = movieDetail['genres'];
        var starList = movieDetail['stars'];

        HTML += "<tr><td>Genres</td><td>"
        for (var i = 0; i < genreList.length; ++i) {
            var genreObject = genreList[i];
            HTML += "name: " + genreObject["name"] + ", id: " + genreObject["id"] + "<br>";
        }
        HTML += + "</td></tr>";
        HTML += "<tr><td>Stars</td><td>"
        for (var i = 0; i < starList.length; ++i) {
            var starObject = starList[i];
            HTML += "name: " + starObject["name"] + ", id: " + starObject["id"] + "<br>";
        }
        HTML += "</td></tr><br>" +
                "<form class='orderM'>" +
                "Purchase movie below<br><input type='text' class='order-quantity' placeholder='Enter quantity...'/>" +
                "<button type='submit' onclick='addCart()'>Add to Cart</button>" +
                "</form>" +
                "<br>" +
                "<form class='rateM'>" +
                "Rate movie below<br><input type='text' class='rate-amount' placeholder='Enter rating [0-10]...'/>" +
                "<button type='submit' onclick='rateMovie()'>Rate Movie</button>" +
                "</form>" +
                "</div>";
        detailDom.append(HTML);

    }
}

function addCart() {
    if(!userIsLoggedIn(eId,sId)){
        alert("login before adding")
        generateLoginPage();
    }
    else{
        console.log("adding to cart")
        var quantity = $(".order-quantity").val();
        $.ajax({
            method: "POST",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/billing/cart/insert",
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                "email": eId,
                "movieId": movieid,
                "quantity": quantity
            }),
            headers: {
                "email": eId,
                "sessionID": sId
            },
            success: reportAddCart
        });
    }
}
function rateMovie(){
    if(!userIsLoggedIn(eId,sId)){
        alert("login before adding")
        generateLoginPage();
    }
    else{
        console.log("Adding rating")
        var rating = $(".rate-amount").val();
        $.ajax({
            method: "POST",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/movies/rating",
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                "id": movieid,
                "rating": rating
            }),
            headers: {
                "email": eId,
                "sessionID": sId
            },
            success: reportAddRating
        });
    }
}
function movieIdPressed(movieNumber){
    console.log("Detail request for movie: " + movieNumber);
    $.ajax({
        method: "GET",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/movies/get/" + movieNumber,
        success: detailReport
    });
}


function reportAddCart(res, status, request){
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
        success: createCartAlert
    });
}

function reportAddRating(res, status, request){
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
        success: createRatingAlert
    });
}

function createCartAlert(res, status, request){
    if (status == "nocontent") {
        var email = request.getResponseHeader("email");
        var sessionID = request.getResponseHeader("sessionID");
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("Gateway report returned status of 204. Waiting " + delay + " seconds to resend request");
        /* suspending until delay time */
        setTimeout(function () {
        }, delay);
        /* sending request for gateway report again */
        $.ajax({
            method: "GET",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
            headers: {
                email: email,
                sessionID: sessionID,
                transactionID: transactionID,
            },
            success: createCartAlert
        });
    } else {
        console.log("Gateway report response: ");
        if(res.resultCode == 33){
            alert("Enter a valid quantity");
            $(".order-quantity").val("");
            $(".rate-amount").val("");
        }
        else if(res.resultCode == 311){
            alert("item already added");
            $(".order-quantity").val("");
            $(".rate-amount").val("");
        }
        else{
            alert("added item successfully")
            $(".order-quantity").val("");
            $(".rate-amount").val("");
        }
    }
}
function createRatingAlert(res, status, request){
    if (status == "nocontent") {
        var email = request.getResponseHeader("email");
        var sessionID = request.getResponseHeader("sessionID");
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("Gateway report returned status of 204. Waiting " + delay + " seconds to resend request");
        /* suspending until delay time */
        setTimeout(function () {
        }, delay);
        /* sending request for gateway report again */
        $.ajax({
            method: "GET",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
            headers: {
                email: email,
                sessionID: sessionID,
                transactionID: transactionID,
            },
            success: createRatingAlert
        });
    } else {
        console.log("Gateway report response: ");
        if(res.resultCode == 251){
            alert("Error could not add");
            $(".order-quantity").val("");
            $(".rate-amount").val("");
        }
        else{
            alert("Added rating successfully")
            $(".order-quantity").val("");
            $(".rate-amount").val("");
        }
    }
}
