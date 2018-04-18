$('#metamask-sign-out').on('click', function (e) {
    // Delete cookie
    document.cookie = "Authorization=;expires=Thu, 01 Jan 1970 00:00:01 GMT;";

    // Navigate to login page
    window.location.href = "/login"
});