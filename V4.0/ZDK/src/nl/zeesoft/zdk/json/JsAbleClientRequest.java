package nl.zeesoft.zdk.json;

public class JsAbleClientRequest extends JsClientRequest {
	public JsAble	reqObject	= null;
	public JsAble	resObject	= null;
	
	public JsAbleClientRequest(JsAble request,String url,JsAble response) {
		super(request.toJson(),url);
		this.reqObject = request;
		this.resObject = response;
	}
}
