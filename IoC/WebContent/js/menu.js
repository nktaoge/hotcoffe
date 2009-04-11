/**
 * Acessibilidade Brasil - Fabio Issamu Oshiro
 * 15/03/2009
 */
var menuId="pillmenu";

function makeLink(u,l){
	document.write('<a href="'+u+'">'+l+'</a>');
}
var id_closeMenu;
var selectVisible=true;
function selectHidden(){
	if(selectVisible){
		var sels=document.getElementsByTagName("select");
		for(var i=0;sels[i];i++){
			sels[i].style.visibility="hidden";
		}
		selectVisible=false;
	}
}
function selectShow(){
	if(!selectVisible){
		var sels=document.getElementsByTagName("select");
		for(var i=0;sels[i];i++){
			sels[i].style.visibility="visible";
		}
		selectVisible=true;
	}
}
function closeSub(v){
	var p=v.parentNode.parentNode;
	var uls = p.getElementsByTagName("ul");
	for( var x = 0; uls[x]; x++ ){
		uls[x].style.visibility="hidden";
		uls[x].style.display="none";
	}
}
function closeMenuAll(id){
	selectShow();
	var as = document.getElementById(id);
	as = as.getElementsByTagName("a");
	for(var i=0;as[i];i++){
		var uls=as[i].parentNode.getElementsByTagName("ul");
		for(var j=0;uls[j];j++){
			uls[j].style.visibility = "hidden";
			uls[j].style.display = "none";
		}
	}
}
function openMenu(){
	selectHidden();
	var x;
	if (id_closeMenu!=null && id_closeMenu!=undefined){
		clearTimeout(id_closeMenu);
	}
	closeSub(this);
	var p=this.parentNode;
	for(x = 0; p.childNodes[x]; x++ ){
		if(p.childNodes[x].nodeType ==1 && p.childNodes[x].tagName!="SCRIPT"){
			p.childNodes[x].style.display="block";
			p.childNodes[x].style.visibility="visible";
		}
	}
	//ini IE6 bug
	for(x = 0; p.childNodes[x]; x++ ){
		if(p.childNodes[x].nodeType ==1){
			var uls=p.childNodes[x].getElementsByTagName("ul");
			for(var j=0;uls[j];j++){
				uls[j].style.visibility = "visible";
				uls[j].style.display = "block";
				uls[j].style.visibility = "hidden";
				uls[j].style.display = "none";
			}
		}
	}//fim IE6 bug
}
document.onmouseup = function(){
	setTimeout("closeMenuAll('"+menuId+"');",500);
};
startList = function() {
	var i;
	var as = document.getElementById(menuId);
	as = as.getElementsByTagName("a");
	for (i=0;i<as.length;i++ ){
		as[i].onfocus = openMenu;
		as[i].onmouseover = openMenu;
		as[i].onmouseout=function(){
			if (id_closeMenu!=null && id_closeMenu!=undefined){
				clearTimeout(id_closeMenu);
			}
			id_closeMenu=setTimeout("closeMenuAll('"+menuId+"');",500);
		};
	}
	setTimeout("closeMenuAll('"+menuId+"');",500);
};
window.onload=function(){
	startList();
};
