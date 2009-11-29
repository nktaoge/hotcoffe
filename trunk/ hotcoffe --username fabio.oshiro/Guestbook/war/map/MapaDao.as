package {
	import flash.external.ExternalInterface;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.display.Sprite;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	import flash.net.URLVariables;
	import flash.net.URLRequestMethod;
	import flash.events.MouseEvent;
	import flash.display.Loader;
	import flash.geom.Point;
	import br.com.goals.etrilhas.MapaItem;
	import br.com.goals.etrilhas.Camada;
	import br.com.goals.etrilhas.iCamada;
	import com.google.maps.LatLng;
	import com.google.maps.overlays.Marker;
	import com.google.maps.overlays.MarkerOptions;
	import com.google.maps.InfoWindowOptions;
	import com.google.maps.MapMouseEvent;
	import com.google.maps.overlays.Polyline;
	import com.google.maps.overlays.PolylineOptions;
	import com.google.maps.styles.StrokeStyle;
	import com.google.maps.styles.FillStyle;
	
	public class MapaDao {
		var ponto:Sprite;
		var dropOnMap:Function;
		var gMap:Sprite;
		public function salvar(mapa:Sprite):void {
			var urlRequest:URLRequest=new URLRequest("MapaItem.xml");
			var urlLoader:URLLoader = new URLLoader();
			var urlVariables:URLVariables = new URLVariables();
			urlLoader.addEventListener(Event.COMPLETE, handleLoadSuccessful);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, handleLoadError);
			//this.mapa=mapa;
			urlVariables["x"]=ponto.x;
			urlVariables["y"]=ponto.y;
			urlVariables["id"]=ponto["id"];
			urlRequest.method=URLRequestMethod.POST;
			urlRequest.data=urlVariables;
			urlLoader.load(urlRequest);
		}
		public function selecionar(mapa:Sprite,dropOnMap:Function):void{
			trace("selecionando mapa...");
			this.gMap = mapa;
			this.dropOnMap=dropOnMap;
			
			var retorno:Array = new Array();
			var urlRequest:URLRequest=new URLRequest("Mapa.xml");
			var urlLoader:URLLoader = new URLLoader();
			var urlVariables:URLVariables = new URLVariables();
			urlLoader.addEventListener(Event.COMPLETE, handSucesso);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, handleLoadError);
			/*
			urlRequest.method=URLRequestMethod.GET;
			urlVariables["t"]=new Date().getTime();
			urlRequest.data=urlVariables;
			*/
			urlLoader.load(urlRequest);
		}
		private function redimensionar(gMapIconLoader:Loader,gMapIcon:Sprite){
			gMapIconLoader.contentLoaderInfo.addEventListener(
				Event.INIT,
				function(evt:Event):void{
					trace("gMapIcon.width = " + gMapIcon.width);
					gMapIcon.width = 16;
					gMapIcon.height = 16;
				}
			);
		}
		private function handSucesso(e:Event){
			var xml:XML=new XML(e.target.data);
			var camadas:XMLList = xml.camada;
			var trilhas:XMLList = xml.trilha;
			var i:Number;
			var cols:Number=4;
			var pri:Boolean = true;
			var icamada:Sprite;
			var yCamadaAnterior:Number = 0;
			var xCamadaAnterior:Number = 0;
			var baseCamada:Sprite;
			var editor = this.gMap.parent;
			var scrollCamadas:Sprite = editor["dCamadas"]["Camadas"] as Sprite;
			var trilhaPts = new Array();
			for each (var trilha:XML in trilhas){
				var trilhaPontos:XMLList = trilha.point;
				trace("Trilha");
				for each  (var  pt:XML in trilhaPontos)  {
					trace("ponto na trilha " + pt.attribute("lat") + " " + pt.attribute("lng"));
					trilhaPts.push(new LatLng(pt.attribute("lat"), pt.attribute("lng")));
				}
			}
			//Desenhar a trilha
			var polyline:Polyline = new Polyline(
				trilhaPts, 
				new PolylineOptions({ strokeStyle: new StrokeStyle({
				color: 0xFF0000,
				thickness: 4,
				alpha: 0.7})
			}));
			this["gMap"]["map"].addOverlay(polyline);
			this["gMap"]["trilha"]=polyline;
			for each  (var  xmlCamada:XML in camadas)  {
				//abrir os MapaItens
				var idCamada:Number = xmlCamada.attribute("pid");
				var listMapaItens:XMLList = xmlCamada.mapaItem;
				var btnNum:Number = 0;
				var btnRowNum:Number = 0;
				trace("camada = " + idCamada);
				//Camada Atual
				if(pri){
					icamada = editor["dCamadas"]["Camadas"]["dCamada"]["iCamada"] as Sprite;
					yCamadaAnterior=icamada.y;
					xCamadaAnterior=icamada.x;
					//base para as camadas
					baseCamada = icamada.parent as Sprite;
					scrollCamadas["iCamadasArray"] = new Array();
					pri=false;
				}else{
					yCamadaAnterior+=55;
					icamada = new iCamada();
					icamada.y=yCamadaAnterior;
					icamada.x=xCamadaAnterior;
					baseCamada.addChild(icamada);
				}
				scrollCamadas["iCamadasArray"].push(icamada);
				trace("yCamadaAnterior = " + yCamadaAnterior);
				icamada["nome"].text = xmlCamada.attribute("nome");
				icamada["pontos"] = new Array();
				for each (var xmlMapaItem:XML in listMapaItens){
					var mapaItem:MapaItem = new MapaItem();
					var tmp:Sprite = mapaItem as Sprite;
					var btnMapaItem:MapaItem = new MapaItem();
					var carregarImg:Loader = new Loader();
					var enderecoImg:URLRequest = new URLRequest("media/icones/" + xmlMapaItem.attribute("icone"));
					var gMapIconLoader:Loader = new Loader();
					var gMapIconUrl:URLRequest = new URLRequest("media/icones/" + xmlMapaItem.attribute("icone"));
					var gMapIcon:Sprite = new Sprite();
					gMapIconLoader.load(gMapIconUrl);
					carregarImg.load(enderecoImg);
					gMapIcon.addChild(gMapIconLoader);
					
					this.redimensionar(gMapIconLoader,gMapIcon);

					tmp.y = xmlMapaItem.attribute("y");
					tmp.x = xmlMapaItem.attribute("x");
					
					btnMapaItem["id"] = xmlMapaItem.attribute("pid");
					btnMapaItem["tipo"] = xmlMapaItem.attribute("tipo");
					tmp["id_camada"]=idCamada;
					//tmp["btnMapaItem"].addEventListener(MouseEvent.MOUSE_UP,this.dropOnMap);
					//mapa.addChild(tmp);
					var markerOptions:MarkerOptions = new MarkerOptions({
						//strokeStyle: new StrokeStyle({color: 0x987654}),
						//fillStyle: new FillStyle({color: 0x223344, alpha: 0.8}),
						//radius: 16,
						icon: gMapIcon,
						iconAlignment: MarkerOptions.ALIGN_TOP,
						iconOffset: new Point(-16,-16)
						//hasShadow: true
					});
					markerOptions.tooltip = xmlMapaItem.attribute("nome");
					
					var latLng:LatLng = new LatLng(xmlMapaItem.attribute("x"),xmlMapaItem.attribute("y"));
					var marker:Marker = new Marker(latLng,markerOptions);
					marker.addEventListener(MapMouseEvent.CLICK,btnMapaItem["executarOverMap"]);

					this["gMap"]["map"].addOverlay(marker);
					//this["gMap"].addMarker(xmlMapaItem.attribute("x"),xmlMapaItem.attribute("y"));
					trace("mapItem = " + tmp["id"] + " " + xmlMapaItem.attribute("x") + " " + xmlMapaItem.attribute("y"));
					
					
					btnMapaItem.x = 15 + 53*btnNum;
					btnMapaItem.y = 35 + 53*btnRowNum;
					
					tmp.addEventListener(MouseEvent.MOUSE_UP,this.dropOnMap);
					tmp.addEventListener(MouseEvent.CLICK,tmp["executarFuncao"]);
					tmp.addEventListener(MouseEvent.MOUSE_DOWN,tmp["dragStarter"]);
					tmp.addEventListener(MouseEvent.MOUSE_UP,tmp["dragStopper"]);
					
					tmp.addChild(carregarImg);
					tmp.buttonMode = true;
					tmp.mouseChildren = false;
					
					var btnIconeNaCamada:Loader = new Loader();
					btnIconeNaCamada.load(enderecoImg);
					btnMapaItem.addEventListener(MouseEvent.CLICK,btnMapaItem["executarFuncao"]);
					btnMapaItem.addChild(btnIconeNaCamada);
					btnMapaItem.buttonMode = true;
					btnMapaItem.mouseChildren = false;
					btnMapaItem["btnMapaItem"].visible=false;
					tmp["btnMapaItem"].visible=false;
					if((btnNum+1)%cols==0){
						btnRowNum++;
						btnNum=-1;
					}
					if(btnNum%cols==0){
						yCamadaAnterior+=53;
					}
					btnMapaItem["latLng"] = latLng;
					btnMapaItem["marker"] = marker;
					btnMapaItem["ponto"] = gMapIcon;
					icamada.addChild(btnMapaItem);
					icamada["pontos"].push(marker);
					btnNum++;
				}				
			}
			trace("Msg: " + xml.msg);
		}
		private function handleLoadSuccessful(e:Event):void {
			var xml:XML=new XML(e.target.data);
			//somente diretorios :-)
			var arquivoList:XMLList  =  xml.pid;
			trace(arquivoList);
			ponto["id"] = arquivoList.toString();
			trace("Message sent.");
		}
		private function handleLoadError(evt:IOErrorEvent):void {
			trace("Message failed.");
		}

	}
}