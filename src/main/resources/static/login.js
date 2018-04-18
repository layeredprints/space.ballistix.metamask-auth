$( document ).ready(function() {
    // check if web3 is available
    if(typeof(web3) === "undefined"){
        document.getElementById("metamask-warning").innerHTML = "Please make sure MetaMask is installed.";
    }
    // check if metamask is unlocked
    else if(web3.eth.accounts.length === 0){
        document.getElementById("metamask-warning").innerHTML = "Please log in to your MetaMask account first and refresh this page.";
    }
    // enable sign in button
    else {
        document.getElementById("metamask-sign-in").disabled = false;
    }
});

$('#metamask-sign-in').on('click', function (e) {
    login();
});

function login(){
    async.waterfall([
        getSignature,
        getJWTToken
    ], function(err){
        if(err){
            // login failed. Show an error message to the user here.
            // ...
        }
        else {
            // the browser has set the 'Authorization' cookie. We can now navigate to the welcome page
            window.location.href = "/welcome"
        }
    });
}


function getJWTToken(signature, callback){
    $.ajax({
        type: "POST",
        url: "/login",
        contentType:"application/json; charset=utf-8",
        data: JSON.stringify({
            signature: signature,
            address: web3.eth.accounts[0]
        }),
        success: function(){
            callback(null);
        },
        error: function(){
            callback("login failed");
        }
    });
}

function getSignature(callback){
    console.log("message hash to be signed", web3.fromUtf8("metamask-auth demo"));

    web3.personal.sign(
        web3.fromUtf8("metamask-auth demo"),
        web3.eth.accounts[0],
        function(err, result){
            if(err){
                callback(err);
            }
            else {
                callback(null, result);
            }

        }
    );
}