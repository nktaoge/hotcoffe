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
	public class CamadaDao {
		var camada:Sprite;
		var dropOnMap:Function;
		public function salvar(camada:Sprite):void {
			var urlRequest:URLRequest=new URLRequest("http://localhost:8080/MapaTrilhas/Camada.xml");
			var urlLoader:URLLoader = new URLLoader();
			var urlVariables:URLVariables = new URLVariables();
			urlLoader.addEventListener(Event.COMPLETE, handleLoadSuccessful);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, handleLoadError);
			urlVariables["x"]=camada.x;
			urlVariables["y"]=camada.y;
			urlVariables["id"]=camada["id"];
			this.camada = camada;
			urlRequest.method=URLRequestMethod.POST;
			urlRequest.data=urlVariables;
			urlLoader.load(urlRequest);
		}
		private function handleLoadSuccessful(e:Event):void {
			var xml:XML=new XML(e.target.data);
			var returnedId:XMLList  =  xml.pid;
			trace(returnedId);
			camada["id"] = returnedId.toString();
			trace("Camada salva " + camada["id"]);
			trace("Msg: " + xml.msg);
		}
		private function handleLoadError(evt:IOErrorEvent):void {
			trace("Message failed.");
		}

	}
}