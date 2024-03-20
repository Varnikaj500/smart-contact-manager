console.log("This is script file");

// Create Arrow functions
// Here i have to make sure if my sidebar is visible then i have to hide it and vv
// I have to call this function whenever i am clicking my sidebar and when i am closing it using cross btn
const toggleSidebar = () => {
	console.log("Sidebar");

    if($('.sidebar').is(":visible"))
    {
        // In css i am going to make my display property to none so that if my sidebar is visible it will hide
        $(".sidebar").css("display", "none");
        // Since i am hiding my sidebar i want the contents to move accordingly
        $(".content").css("margin-left", "0%");

    }else{
        // To make sure that my sidebar is visible
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");

    }

};



  