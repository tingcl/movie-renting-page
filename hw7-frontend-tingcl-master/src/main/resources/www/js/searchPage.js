/* ------------- Global variables ----------------- */
var title;
var director;
var year;
var genre;
var page = 1;
var d;
var o;
var l;
/* ------------- Global variables ----------------- */

// Basic search activated send request to gateway
$("#basic").submit(function (event) {
    event.preventDefault();
    if(eId == null && sId == null){
        alert("No logged in user. Please login or register. Redirecting to login")
        generateLoginPage();
    }
    else{
        // start page at 1
        page = 1;
        var rad = document.myForm.searchFor;

        if(rad.value == "title"){
            title = $(".index-bar-query").val();
            console.log("Searching for movies of title: " + title);
            $.ajax({
                method: "GET",
                url: "http://andromeda-70.ics.uci.edu:8216/api/g/movies/search?title=" + title,
                success: reportBasic
            });
        }
        else{
            genre = $(".index-bar-query").val();
            console.log("Searching for movies of genre: " + genre);
            title = null;
            $.ajax({
                method: "GET",
                url: "http://andromeda-70.ics.uci.edu:8216/api/g/movies/search?genre=" + genre,
                success: reportBasic
            });
        }
    }
});

// Create advanced movie search page
$('.index-bar-advanced').click(function (event) {
        event.preventDefault();
        console.log("Someone pressed advanced search.");
        if(sId == null && eId == null){
            alert("No logged in user. Please login or register. Redirecting to login")
            generateLoginPage();
        }
        else{
            generateAdvancedPage();
            console.log("Successful load.");
        }
    }
);

function generateAdvancedPage(){
    page = 1;
    var advancedDom = $(".advancedSearch");
    $(".loginPage").empty();
    $(".registerPage").empty();
    $(".moviesPage").empty();
    $(".detailPage").empty();
    $(".accountPage").empty();
    $(".cartPage").empty();
    $(".orderPage").empty();
    advancedDom.empty();
    console.log("Adding advanced html")
    document.getElementById("basic").reset();
    var rowHTML =
        "<h2>Advanced search</h2><h3>Enter movie search params below</h3>" +
        "<div class='container'>" +
        "<form id='advanced'>" +
        "Title: <input class='advanced-title' type='text'/><br>" +
        "Director: <input class='advanced-director' type='text'/><br>" +
        "Year: <input class='advanced-year' type='text'/><br>" +
        "Genre: <input class='advanced-genre' type='text'/><br>" +
        "<button class='b1' type='submit' style='vertical-align:middle'><span>Search</span></button>" +
        "</form>" +
        "</div>"
    advancedDom.append(rowHTML);
    // Advanced movie search
    $("#advanced").submit(function (event) {
            event.preventDefault();
            title = $(".advanced-title").val();
            director = $(".advanced-director").val();
            year = $(".advanced-year").val();
            genre = $(".advanced-genre").val();
            $.ajax({
                method: "GET",
                url: "http://andromeda-70.ics.uci.edu:8216/api/g/movies/search",
                data: {
                    "title": title,
                    "director": director,
                    "year": year,
                    "genre": genre,
                },
                success: reportBasic
            });
        }
    );
}

// Sends request to api gateway for report
function reportBasic(res, status, request)
{
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
        success: createBasicPage
    });
}

// Filter request
function reportFilter(res, status, request)
{
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
        success: createFilteredPage
    });
}
function createFilteredPage(res, status, request) {
    /* if no content is returned */
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
            success: createFilteredPage
        });
    } else {
        console.log("Gateway report response: ");
        console.log(res.movies);
        var movieDom = $(".moviesPage");
        var filterDom = $(".filters");
        if (res.resultCode == 211) {
            if (page == 1) {
                $(".loginPage").empty();
                $(".registerPage").empty();
                $(".advancedSearch").empty();
                $(".detailPage").empty();
                $(".accountPage").empty();
                $(".cartPage").empty();
                $(".orderPage").empty();
                filterDom.empty();
                movieDom.empty();
                console.log("no movies were found");
                movieDom.append("<h2>Search Results</h2><p>No movies were found.</p>")
            } else {
                alert("End of results");
            }
        } else {
            $(".loginPage").empty();
            $(".registerPage").empty();
            $(".advancedSearch").empty();
            $(".detailPage").empty();
            $(".accountPage").empty();
            $(".cartPage").empty();
            movieDom.empty();
            var movieList = res.movies;
            console.log("response movie list: ");
            console.log(movieList);
            var rowHTML =
                "<br><table border='1'><tr><td>Movie ID</td><td>Title</td><td>Year</td><td>Director</td><td>Rating</td><td># of Votes</td>";
            for (var i = 0; i < movieList.length; ++i) {
                rowHTML += "<tr>";
                var movieObject = movieList[i];
                var movieNumber = movieObject["movieId"];
                rowHTML += '<td><button id="movieIdButton" onclick="movieIdPressed(\'' + movieNumber + '\')"><u>' + movieNumber + '</u></button></td>';
                rowHTML += "<td>" + movieObject["title"] + "</td>";
                rowHTML += "<td>" + movieObject["year"] + "</td>";
                rowHTML += "<td>" + movieObject["director"] + "</td>";
                rowHTML += "<td>" + movieObject["rating"] + "</td>";
                rowHTML += "<td>" + movieObject["numVotes"] + "</td>";
                rowHTML += "</tr>";
            }
            rowHTML += "</table>" +
                "<div class='pagination'>" +
                "<a id='back'>&laquo;</a>" +
                "<label id='page'></label>" +
                "<a id='forward'>&raquo;</a>" +
                "</div>";
            movieDom.append(rowHTML);
            console.log("Changing page number to " + page);
            document.getElementById('page').innerHTML = page.toString();
        }
        $("#back").click(function (event) {
            event.preventDefault();
            var page_temp = document.getElementById("page").innerText;
            page = parseInt(page_temp, 10);
            if ((page - 2) < 0) {
                alert("No movies earlier")
            } else {
                var limit = $("#id-number").val();
                var offset = limit * (page - 2);
                page -= 1;
                console.log("new page:" + page);
                console.log("limit: " + limit);
                console.log("offset: " + offset);
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "limit": limit,
                        "offset": offset,
                        "orderby": o,
                        "direction": d
                    },
                    success: reportFilter
                });
            }
        });
        $("#forward").click(function (event) {
            event.preventDefault();
            console.log("Request for next movies page");
            var page_temp = document.getElementById("page").innerText;
            page = parseInt(page_temp, 10);
            var limit = $("#id-number").val();
            var offset = limit * page;
            page += 1;
            console.log("new page:" + page);
            console.log("limit: " + limit);
            console.log("offset: " + offset);
            $.ajax({
                method: "GET",
                url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                data: {
                    "title": title,
                    "director": director,
                    "year": year,
                    "genre": genre,
                    "limit": limit,
                    "offset": offset,
                    "orderby": o,
                    "direction": d
                },
                success: reportFilter
            });
        });
        $("#id-sort").change(function () {
            console.log($(this).val());
            if ($(this).val() == 1) {
                console.log("case 1");
                o = "rating";
                d = "desc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "rating",
                        "direction": "desc"
                    },
                    success: reportFilter
                });
            } else if ($(this).val() == 2) {
                console.log("case 2");
                o = "rating";
                d = "asc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "rating",
                        "direction": "asc"
                    },
                    success: reportFilter
                });
            } else if ($(this).val() == 3) {
                console.log("case 3");
                o = "title";
                d = "desc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "title",
                        "direction": "desc"
                    },
                    success: reportFilter
                });
            } else {
                console.log("case 4");
                o = "title";
                d = "asc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "title",
                        "direction": "asc"
                    },
                    success: reportFilter
                });
            }
        });
        $("#id-number").change(function () {
            console.log($(this).val());
            var use = document.getElementById("id-sort");
            var temp = use.options[use.selectedIndex].value;
            if (temp == 1) {
                console.log("case 1");
                o = "rating";
                d = "desc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "rating",
                        "direction": "desc",
                        "limit": $(this).val()
                    },
                    success: reportFilter
                });
            } else if (temp == 2) {
                console.log("case 2");
                o = "rating";
                d = "asc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "rating",
                        "direction": "asc",
                        "limit": $(this).val()
                    },
                    success: reportFilter
                });
            } else if (temp == 3) {
                console.log("case 3");
                o = "title";
                d = "desc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "title",
                        "direction": "desc",
                        "limit": $(this).val()
                    },
                    success: reportFilter
                });
            } else {
                console.log("case 4");
                o = "title";
                d = "asc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "title",
                        "direction": "asc",
                        "limit": $(this).val()
                    },
                    success: reportFilter
                });
            }
        });
    }
}
/* creates the movie page when gateway response returns */
function createBasicPage(res, status, request) {
    /* if no content is returned */
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
            success: createBasicPage
        });
    } else {
        console.log("Gateway report response: ");
        console.log(res.movies);
        var movieDom = $(".moviesPage");
        var filterDom = $(".filters");
        if (res.resultCode == 211) {
            if (page == 1) {
                $(".loginPage").empty();
                $(".registerPage").empty();
                $(".advancedSearch").empty();
                $(".detailPage").empty();
                $(".accountPage").empty();
                $(".cartPage").empty();
                $(".orderPage").empty();
                filterDom.empty();
                movieDom.empty();
                console.log("no movies were found");
                movieDom.append("<h2>Search Results</h2><p>No movies were found.</p>")
            } else {
                alert("End of results");
            }
        } else {
            $(".loginPage").empty();
            $(".registerPage").empty();
            $(".advancedSearch").empty();
            $(".detailPage").empty();
            $(".cartPage").empty();
            $(".accountPage").empty();
            $(".orderPage").empty();
            movieDom.empty();
            filterDom.empty();
            var movieList = res.movies;
            console.log("response movie list: ");
            console.log(movieList);
            var filter =
                "<h2>Search Results</h2>" +
                "<div class='options-bar'>" +
                "<label class='sort-label'>Sort</label>" +
                "<select id = 'id-sort'>" +
                "<option value = '1'>Rating+Descending</option>" +
                "<option value = '2'>Rating+Ascending</option>" +
                "<option value = '3'>Title+Descending</option>" +
                "<option value = '4'>Title+Ascending</option>" +
                "</select>" +
                "<label class='number-label'>Items Per Page</label>" +
                "<select id = 'id-number'>" +
                "<option value = '10'>10</option>" +
                "<option value = '25'>25</option>" +
                "<option value = '50'>50</option>" +
                "<option value = '100'>100</option>" +
                "</select>" +
                "</div>";
            var rowHTML =
                "<br><table border='1'><tr><td>Movie ID</td><td>Title</td><td>Year</td><td>Director</td><td>Rating</td><td># of Votes</td>";
            for (var i = 0; i < movieList.length; ++i) {
                rowHTML += "<tr>";
                var movieObject = movieList[i];
                var movieNumber = movieObject["movieId"];
                rowHTML += '<td><button id="movieIdButton" onclick="movieIdPressed(\'' + movieNumber + '\')"><u>' + movieNumber + '</u></button></td>';
                rowHTML += "<td>" + movieObject["title"] + "</td>";
                rowHTML += "<td>" + movieObject["year"] + "</td>";
                rowHTML += "<td>" + movieObject["director"] + "</td>";
                rowHTML += "<td>" + movieObject["rating"] + "</td>";
                rowHTML += "<td>" + movieObject["numVotes"] + "</td>";
                rowHTML += "</tr>";
            }
            rowHTML += "</table>" +
                "<div class='pagination'>" +
                "<a id='back'>&laquo;</a>" +
                "<label id='page'></label>" +
                "<a id='forward'>&raquo;</a>" +
                "</div>"
            filterDom.append(filter);
            movieDom.append(rowHTML);
            console.log("Changing page number to " + page);
            document.getElementById('page').innerHTML = page.toString();
        }
        $("#back").click(function (event) {
            event.preventDefault();
            var page_temp = document.getElementById("page").innerText;
            page = parseInt(page_temp, 10);
            if ((page - 2) < 0) {
                alert("No movies earlier")
            } else {
                var limit = $("#id-number").val();
                var offset = limit * (page - 2);
                page -= 1;
                console.log("new page:" + page);
                console.log("limit: " + limit);
                console.log("offset: " + offset);
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "limit": limit,
                        "offset": offset,
                        "orderby": o,
                        "direction": d
                    },
                    success: reportFilter
                });
            }
        });
        $("#forward").click(function (event) {
            event.preventDefault();
            console.log("Request for next movies page");
            var page_temp = document.getElementById("page").innerText;
            page = parseInt(page_temp, 10);
            var limit = $("#id-number").val();
            var offset = limit * page;
            page += 1;
            console.log("new page:" + page);
            console.log("limit: " + limit);
            console.log("offset: " + offset);
            $.ajax({
                method: "GET",
                url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                data: {
                    "title": title,
                    "director": director,
                    "year": year,
                    "genre": genre,
                    "limit": limit,
                    "offset": offset,
                    "orderby": o,
                    "direction": d
                },
                success: reportFilter
            });
        });
        $("#id-sort").change(function () {
            console.log($(this).val());
            if ($(this).val() == 1) {
                console.log("case 1");
                o = "rating";
                d = "desc";
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "rating",
                        "direction": "desc"
                    },
                    success: reportFilter
                });
            } else if ($(this).val() == 2) {
                console.log("case 2");
                o = "rating";
                d = "asc";
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "rating",
                        "direction": "asc"
                    },
                    success: reportFilter
                });
            } else if ($(this).val() == 3) {
                console.log("case 3");
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "title",
                        "direction": "desc"
                    },
                    success: reportFilter
                });
            } else {
                console.log("case 4");
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "title",
                        "direction": "asc"
                    },
                    success: reportFilter
                });
            }
        });
        $("#id-number").change(function () {
            console.log($(this).val());
            var use = document.getElementById("id-sort");
            var temp = use.options[use.selectedIndex].value;
            console.log("HERERHERHEHREHRHEHRERERE");
            console.log(temp);
            if (temp == 1) {
                console.log("case 1");
                o = "rating";
                d = "desc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "rating",
                        "direction": "desc",
                        "limit": $(this).val()
                    },
                    success: reportFilter
                });
            } else if (temp == 2) {
                console.log("case 2");
                o = "rating";
                d = "asc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "rating",
                        "direction": "asc",
                        "limit": $(this).val()
                    },
                    success: reportFilter
                });
            } else if (temp == 3) {
                console.log("case 3");
                o = "title";
                d = "desc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "title",
                        "direction": "desc",
                        "limit": $(this).val()
                    },
                    success: reportFilter
                });
            } else {
                console.log("case 4");
                o = "title";
                d = "asc";
                page = 1;
                $.ajax({
                    method: "GET",
                    url: 'http://andromeda-70.ics.uci.edu:8216/api/g/movies/search',
                    data: {
                        "title": title,
                        "director": director,
                        "year": year,
                        "genre": genre,
                        "orderby": "title",
                        "direction": "asc",
                        "limit": $(this).val()
                    },
                    success: reportFilter
                });
            }
        });
    }
}





