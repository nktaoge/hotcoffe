var btnClose = "<div id=\"titleBar\"><a href=\"javascript:fecharBox();\"><img src=\"img/btnFechar.png\" alt=\"fechar\"/></a></div>";
function fecharBox(){
	$("#Video").hide();
	$("#Galeria").hide();
	$("#Video").html("");
	$("#Galeria").html("");
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
                obj.show('slow');
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
var aPicL=$("#pic0");
var fotoPrincipalN=1;
function openFoto(indice){
	var url = arrFotos[indice];
	arrFotosI = indice;
	var aPic = $("#pic" + arrFotosI);
	aPic.css("color","#f00");
	aPicL.css("color","#000");
	aPicL = aPic;
	fotoPrincipalN++;
	var openId = "#fotoPrincipal"+(fotoPrincipalN%2);
	fotoPrincipalN++;
	var closeId = "#fotoPrincipal"+(fotoPrincipalN%2);
	fotoPrincipalN++;
	var open = $(openId);
	open.html("<img src=\""+url+"\" alt=\"Imagem\" height=\"277\" />");
	$(closeId).hide('slow');
	open.show('slow');	
}
function nextFoto(){
	arrFotosI++;
	openFoto(arrFotosI%arrFotos.length);
}
function prevFoto(){
	arrFotosI--;
	if(arrFotosI<0){
		arrFotosI=arrFotos.length-1;
	}
	openFoto(arrFotosI);
}

