$(function(){
//console.log(location.search.concat());
//manyValues();
    $("#show").html(oneValues());
})

//接收一个值
function oneValues(){
    var result;
    var url=window.location.search; //获取url中"?"符后的字串
    if(url.indexOf("?")!=-1){
        result = url.substr(url.indexOf("=")+1);
    }
    return result;
}


//接收多值
function manyValues(){
    var url=window.location.search;
    if(url.indexOf("?")!=-1){
        var str = url.substr(1);
        strs = str.split("&");
        var key=new Array(strs.length);
        var value=new Array(strs.length);
        for(i=0;i<strs.length;i++){
            key[i]=strs[i].split("=")[0]
            value[i]=unescape(strs[i].split("=")[1]);
            alert(key[i]+"="+value[i]);
        }
    }
}