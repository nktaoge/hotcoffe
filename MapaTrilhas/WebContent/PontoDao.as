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
	public class PontoDao {
		var ponto:Sprite;
		var mapa:Sprite;
		public function salvar(ponto:Sprite):void {
			var urlRequest:URLRequest=new URLRequest("http://localhost:8080/MapaTrilhas/MapaItem.xml");
			var urlLoader:URLLoader = new URLLoader();
			var urlVariables:URLVariables = new URLVariables();
			urlLoader.addEventListener(Event.COMPLETE, handleLoadSuccessful);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, handleLoadError);
			this.ponto=ponto;
			urlVariables["x"]=ponto.x;
			urlVariables["y"]=ponto.y;
			urlVariables["id"]=ponto["id"];
			urlVariables["id_camada"]=ponto["id_camada"];
			urlRequest.method=URLRequestMethod.POST;
			urlRequest.data=urlVariables;
			urlLoader.load(urlRequest);
		}
		public function listar(mapa:Sprite):void{
			trace("listando...");
			var retorno:Array = new Array();
			var urlRequest:URLRequest=new URLRequest("http://localhost:8080/MapaTrilhas/MapaItem.xml");
			var urlLoader:URLLoader = new URLLoader();
			var urlVariables:URLVariables = new URLVariables();
			urlLoader.addEventListener(Event.COMPLETE, handSucesso);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, handleLoadError);
			urlRequest.method=URLRequestMethod.GET;
			urlVariables["t"]=new Date().getTime();
			urlRequest.data=urlVariables;
			urlLoader.load(urlRequest);
			this.mapa = mapa;
		}
		private function handSucesso(e:Event){
			var xml:XML=new XML(e.target.data);
			var mapaItens:XMLList = xml.mapaItem;
			var i:Number;
			for each  (var  elMapItem:XML  in mapaItens)  { 
				var mapaItem:MapaItem = new MapaItem();
				var tmp:Sprite = mapaItem as Sprite;
				tmp.y = elMapItem.attribute("y");
				tmp.x = elMapItem.attribute("x");
				tmp["id"] = elMapItem.attribute("pid");
				mapa.addChild(tmp);
				trace("mapItem = " + tmp.x + " " + tmp.y);
			}
			trace("ok!");
		}
		private function handleLoadSuccessful(e:Event):void {
			var xml:XML=new XML(e.target.data);
			//somente diretorios :-)
			var arquivoList:XMLList  =  xml.pid;
			trace(arquivoList);
			ponto["id"] = arquivoList.toString();
			trace("Msg: " + xml.msg);
		}
		private function handleLoadError(evt:IOErrorEvent):void {
			trace("Message failed.");
		}

	}
}