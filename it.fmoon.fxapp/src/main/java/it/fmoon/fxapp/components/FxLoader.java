package it.fmoon.fxapp.components;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.support.NamesUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

@Component
public class FxLoader {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public Parent loadView(Object controller) {
		String controllerName = controller.getClass().getSimpleName();
		String viewName = NamesUtils.viewNameFromControllerName(controllerName);
		return loadView(controller, viewName);
	}

	public Parent loadView(Object baseResorceObj, String viewName) {
		System.out.println("load view: "+viewName);
		FXMLLoader loader = new FXMLLoader(baseResorceObj.getClass().getResource(viewName+".fxml"));
        loader.setControllerFactory(applicationContext::getBean);
        Parent viewNode;
        try {
			viewNode = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return viewNode;
	}


}
