// Verify that user is actually logged in
function userIsLoggedIn(eId, sId){
    if(sId != null && sId != null)
        return true;
    return false;
}

function verifySession(eId, sId){
    console.log("sending request for session to api gateway");
    $.ajax({
        method: "POST",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/idm/session",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "email": eId,
            "sessionID": sId
        }),
        success: sessionReport
    });
}

function sessionReport(res, status, request){
    console.log("getting report from api gateway");
    console.log("successful register with idm.");
    var transactionID = request.getResponseHeader("transactionID");

    console.log("transactionID: " + transactionID);
    console.log("status: " + status);

    console.log("Creating request for gateway report");
    $.ajax({
        method: "GET",
        url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
        headers: {
            transactionID: transactionID,
        },
        success: sessionResult
    });
}

function sessionResult(res, status, request){
    if(status == "nocontent"){
        var transactionID = request.getResponseHeader("transactionID");
        var delay = request.getResponseHeader("delay");
        console.log("Gateway report returned status of 204. Waiting " + delay +" seconds to resend request");
        /* suspending until delay time */
        setTimeout(function(){
        }, delay);
        $.ajax({
            method: "GET",
            url: "http://andromeda-70.ics.uci.edu:8216/api/g/report",
            headers: {
                transactionID: transactionID,
            },
            success: sessionResult
        });
    }
    else{
        responseforsession = res.resultCode;
        console.log(responseforsession);
    }
}