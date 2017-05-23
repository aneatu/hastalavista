$(document).ready(function() {

    ajaxStatistics();


    function ajaxStatistics(){
        // DO GET
        $.ajax({
            type : "GET",
            url : "/analyticsData",
            success : function(resultStr) {
                var result = JSON.parse(resultStr);
                if(result.status == "200"){
                    var contentDiv = "<strong>Words</strong>";
                    var words = JSON.stringify(result.words).replace(/\"/g, "").replace(/{/g, "").replace(/}/g, "")
                    var wordsArray = words.split('|')
                    contentDiv += "<table><th>Word</th><th>Searched X times</th>";
                    for (var i in wordsArray) {
                        var tokens = wordsArray[i].split(",")
                        contentDiv += "<tr><td>" + tokens[0] + "</td><td>" + tokens[1] +"</td></tr>"
                    }
                    contentDiv += "</table>"

                    contentDiv += "</br></br></br>"

                    contentDiv += "<strong>Terms</strong>";
                    var terms = JSON.stringify(result.terms).replace(/\"/g, "").replace(/{/g, "").replace(/}/g, "")
                    var termsArray = terms.split('|')
                    contentDiv += "<table><th>Term</th><th>Searched X times</th>";
                    for (var i in termsArray) {
                        var tokens = termsArray[i].split(",")
                        contentDiv += "<tr><td>" + tokens[0] + "</td><td>" + tokens[1] +"</td></tr>"
                    }
                    contentDiv += "</table>"

                    contentDiv += "</br></br></br>"

                    contentDiv += "<strong>Urls</strong>";
                    var urls = JSON.stringify(result.urls).replace(/\"/g, "").replace(/{/g, "").replace(/}/g, "")
                    var urlsArray = urls.split('|')
                    contentDiv += "<table><th>Url</th><th>Searched X times</th>";
                    for (var i in urlsArray) {
                        var tokens = urlsArray[i].split(",")
                        contentDiv += "<tr><td>" + tokens[0] + "</td><td>" + tokens[1] +"</td></tr>"
                    }
                    contentDiv += "</table>"

                    $("#postResultDiv").html(contentDiv);
                }else{
                    $("#postResultDiv").html("<strong>Validation error:</strong><br>" + result.message);
                }
                console.log(result);
            },
            error : function(e) {
                alert("Error! It's time to get panicked!")
                console.log("ERROR: ", e);
            }
        });
    }
})