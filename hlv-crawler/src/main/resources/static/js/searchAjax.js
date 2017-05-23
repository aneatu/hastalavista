$(document).ready(function() {

    // SUBMIT FORM
    $("#search-form").submit(function(event) {
        // Prevent the form from submitting via the browser.
        event.preventDefault();
        ajaxSearch();
    });


    function ajaxSearch(){
        // DO GET
        $.ajax({
            type : "GET",
            url : "/find?term=" + $("#term").val(),
            success : function(resultStr) {
                var result = JSON.parse(resultStr);
                if(result.status == "200"){
                    var contentDiv = "<strong>List of results</strong>";
                    var dataArray = result.data;
                    if (dataArray.length == 0) {
                        contentDiv += "</br>No results found";
                    } else {
                        contentDiv += "<table><th>URL</th><th>Occurrences</th><th>Distance</th>";
                        for (var page in dataArray) {
                            contentDiv += "<tr><td><a href='/pageAnalytics?page=" + dataArray[page].uuid + "' target='_blank'>" + dataArray[page].url + "</a></td><td>" + dataArray[page].occurrences + "</td><td>" + dataArray[page].distance + "</td></tr>"
                        }
                        contentDiv += "</table>"
                    }
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