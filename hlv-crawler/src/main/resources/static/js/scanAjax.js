$(document).ready(function() {

    // SUBMIT FORM
    $("#scan-form").submit(function(event) {
        // Prevent the form from submitting via the browser.
        event.preventDefault();
        ajaxScan();
    });


    function ajaxScan(){

        // PREPARE FORM DATA
        var formData = {
            website : $("#website").val(),
            noLinks : $("#no-links").val()
        }

        // DO POST
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/crawler",
            data : JSON.stringify(formData),
            dataType : 'json',
            success : function(result) {
                if(result.status == "200"){
                    $("#postResultDiv").html("<strong>" + "Successfully scanned <i>" + result.data.noLinks + "</i> links for <i>" + result.data.website + "</i></strong>");
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

        // Reset FormData after Posting
        resetData();

    }

    function resetData(){
        $("#website").val("");
        $("#no-links").val("");
    }
})