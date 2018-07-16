$(document).ready(function(){
    $(".bookmark ul li").hover(function(){
        // alert("11111");
        $(this).children('div').stop(true, true).slideToggle();
    });
});


