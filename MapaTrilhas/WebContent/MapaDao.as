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
	import br.com.goals.etrilhas.MapaItem;
	import br.com.goals.etrilhas.Camada;
	public class MapaDao {
		var ponto:Sprite;
		var mapa:Sprite;
		var dropOnMap:Function;
		public function salvar(mapa:Sprite):void {
			var urlRequest:URLRequest=new URLRequest("http://localhost:8080/MapaTrilhas/MapaItem.xml");
			var urlLoader:URLLoader = new URLLoader();
			var urlVariables:URLVariables = new URLVariables();
			urlLoader.addEventListener(Event.COMPLETE, handleLoadSuccessful);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, handleLoadError);
			this.mapa=mapa;
			urlVariables["x"]=ponto.x;
			urlVariables["y"]=ponto.y;
			urlVariables["id"]=ponto["id"];
			urlRequest.method=URLRequestMethod.POST;
			urlRequest.data=urlVariables;
			urlLoader.load(urlRequest);
		}
		public function selecionar(mapa:Sprite,dropOnMap:Function):void{
			trace("selecionando mapa...");
			this.mapa = mapa;
			this.dropOnMap=dropOnMap;
			
			var retorno:Array = new Array();
			var urlRequest:URLRequest=new URLRequest("http://localhost:8080/MapaTrilhas/Mapa.xml");
			var urlLoader:URLLoader = new URLLoader();
			var urlVariables:URLVariables = new URLVariables();
			urlLoader.addEventListener(Event.COMPLETE, handSucesso);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, handleLoadError);
			urlRequest.method=URLRequestMethod.GET;
			urlVariables["t"]=new Date().getTime();
			urlRequest.data=urlVariables;
			urlLoader.load(urlRequest);
		}
		private function handSucesso(e:Event){
			var xml:XML=new XML(e.target.data);
			var camadas:XMLList = xml.camada;
			var i:Number;
			for each  (var  xmlCamada:XML in camadas)  {
				//abrir os MapaItens
				var idCamada:Number = xmlCamada.attribute("pid");
				var listMapaItens:XMLList = xmlCamada.mapaItem;
				trace("camada = " + idCamada);
				for each (var xmlMapaItem:XML in listMapaItens){
					var mapaItem:MapaItem = new MapaItem();
					var tmp:Sprite = mapaItem as Sprite;
					tmp.y = xmlMapaItem.attribute("y");
					tmp.x = xmlMapaItem.attribute("x");
					tmp["id"] = xmlMapaItem.attribute("pid");
					tmp["tipo"] = xmlMapaItem.attribute("tipo");
					tmp["id_camada"]=idCamada;
					tmp["btnMapaItem"].addEventListener(MouseEvent.MOUSE_UP,dropOnMap);
					mapa.addChild(tmp);
					trace("mapItem = " + tmp["id"] + " " + tmp.x + " " + tmp.y);
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