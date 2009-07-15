function executarFuncao(args){
	var a = args.split(":");
	var tipo = a[1];
	var id = a[0];
	alert("chamando " + id + " tipo="+tipo);
	$.ajax({
		  url: "data/"+tipo+"-"+id+".html",
		  cache: false,
		  success: function(html){
		    	$("#"+tipo).append(html);
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
		var win = window.open("definirMapaItem?id="+id,"id"+id,"width=700, height=600");
		win.focus();
	}catch(e){
		
	}
}