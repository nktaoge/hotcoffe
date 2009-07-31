var btnClose = "<div id=\"titleBar\"><a href=\"#\" onclick=\"fecharVideo()\">Fechar</a></div>";
function fecharVideo(){
	$("#Video").hide();
}
function executarFuncao(args){
	var a = args.split(":");
	var tipo = a[1];
	var id = a[0];
	$("#"+tipo).html("Chamando " + id + " tipo="+tipo);
	//alert("chamando " + id + " tipo="+tipo);
	$.ajax({
		  url: "data/"+tipo+"-"+id+".html",
		  cache: false,
		  success: function(html){
				var obj = $("#"+tipo);
		    	$("#"+tipo).html(btnClose + html);
		    	obj.css('top', $(window).height()/2-obj.height()/2);
                obj.css('left', $(window).width()/2-obj.width()/2); 
                obj.show();
		  }
	});
}
function getFlashMovie(movieName) {   
	var isIE = navigator.appName.indexOf("Microsoft") != -1;
	return (isIE) ? window[movieName] : document[movieName];
}
function formSend() {
	var text = document.htmlForm.sendField.value;
	getFlashMovie("Player").sendTextToFlash(text);
}
function getTextFromFlash(str) {
	document.htmlForm.receivedField.value = "From Flash: " + str;
	return str + " received";
}
var stateWindow = "g";
function resizeFlashTo(v){
	//var fla = document.getElementById("fla");
	//fla.style.height=v+"px";
	//fla.style.width="550px";
}
function definirMapaItem(id){
	try{
		var win = window.open("definirMapaItem?id="+id,"id"+id,"width=700, height=600,scrollbars=1");
		win.focus();
	}catch(e){
		
	}
}