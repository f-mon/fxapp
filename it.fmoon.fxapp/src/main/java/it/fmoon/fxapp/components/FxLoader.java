package it.fmoon.fxapp.components;

import java.io.IOException;
import java.net.URL;

import org.springframework.stereotype.Component;

import it.fmoon.fxapp.support.NamesUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Callback;

@Component
public class FxLoader {
	
//	@Autowired
//	private ApplicationContext applicationContext;
	
	public Node loadView(Object controller) {
		String controllerName = controller.getClass().getSimpleName();
		String viewName = NamesUtils.viewNameFromControllerName(controllerName);
		return loadView(controller, viewName, controllerFactoryForObject(controller));
	}

	public Node loadView(Object baseResorceObj, String viewName) {
		return loadView(baseResorceObj,viewName,null);
	}
	public Node loadLinkedView(Object controller, String viewName) {
		return loadView(controller,viewName,controllerFactoryForObject(controller));
	}
	public Node loadView(Object baseResorceObj, String viewName, Callback<Class<?>,Object> controllerFactory) {
		return loadView(baseResorceObj.getClass(), viewName, controllerFactory);
	}
	
	public Node loadView(Class<?> baseResorceObjClass, String viewName) {
		return loadView(baseResorceObjClass,viewName,null);
	}
	public Node loadLinkedView(Class<?> baseResorceObjClass, String viewName, Object controller) {
		return loadView(baseResorceObjClass,viewName,controllerFactoryForObject(controller));
	}
	public Node loadView(Class<?> baseResorceObjClass, String viewName, Callback<Class<?>,Object> controllerFactory) {
		System.out.println("load view: "+viewName);
		URL fxmlResource = baseResorceObjClass.getResource(viewName+".fxml");
		FXMLLoader loader = new FXMLLoader(fxmlResource);
		if (controllerFactory!=null) {			
			loader.setControllerFactory(controllerFactory);
		}
		Node viewNode;
        try {
			viewNode = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return viewNode;
	}

	public static Callback<Class<?>,Object> controllerFactoryForObject(Object controller) {
		return (type)->{
			if (type.isInstance(controller)) {
				return controller;
			}
			return null;
		};
	}

}
