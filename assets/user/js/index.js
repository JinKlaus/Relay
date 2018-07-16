// $("p").click(function(){
//     $("this").css("background-color","yellow");
// },function(){
//     $("p").css("background-color","pink");
// });
$(document).ready(function(){
    $("#texts a").hover(function(){
        // alert("11111");
        $(this).children('a').css("background-color","yellow").slideToggle();
    });
});