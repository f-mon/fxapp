package it.fmoon.fxapp;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import it.fmoon.fxapp.controllers.application.ApplicationController;
import it.fmoon.fxapp.events.InitializeApplication;
import it.fmoon.fxapp.events.StartupApplication;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxApplication extends Application {

	private ConfigurableApplicationContext applicationContext;
	
	@Override
	public void init() throws Exception {
		super.init();
		
		var args = getParameters().getRaw().toArray(new String[] {});
		applicationContext = SpringApplication.run(SpringApp.class, args);
	}

	public void start(Stage primaryStage) {
		
//		stage.setTitle();
		
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		
		ApplicationController appCtrl = applicationContext.getBean(ApplicationController.class);
		Parent root = appCtrl.getView();
		//WindowStyle.allowDrag(root, primaryStage);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();	
		
		applicationContext.publishEvent(new InitializeApplication());
		
		applicationContext.publishEvent(new StartupApplication());
		
	}
	
}