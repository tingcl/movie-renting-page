function clearPage(){
    console.log("Received request to go return to home page");
    $('.loginPage').empty();
    $('.registerPage').empty();
    $('.moviesPage').empty();
    $(".advancedSearch").empty();
    $(".detailPage").empty();
    $(".accountPage").empty();
    $(".filters").empty();
    $(".cartPage").empty();
    $(".orderPage").empty();
    document.getElementById("basic").reset();
    console.log("Cleared existing page.");
}

$(".index-main").click(function (event) {
    event.preventDefault();
    clearPage();
    console.log("Successfully returned to home page.");
});