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
		createApplicationContext();
		applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
	}

	public void start(Stage primaryStage) {
		showPrimaryStage(primaryStage);
		applicationContext.publishEvent(new InitializeApplication());
		afterInitialize();
		applicationContext.publishEvent(new StartupApplication());
		afterStart();		
	}

	protected void createApplicationContext() {
		var args = getParameters().getRaw().toArray(new String[] {});
		var primarySources = new Class<?>[] { SpringApp.class, this.getClass() };
		applicationContext = SpringApplication.run(primarySources, args);
	}
	
	protected void afterInitialize() {
	}
	
	protected void afterStart() {
	}

	protected void showPrimaryStage(Stage primaryStage) {
//		stage.setTitle();
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		
		ApplicationController appCtrl = applicationContext.getBean(ApplicationController.class);
		Parent root = appCtrl.getView();
		//WindowStyle.allowDrag(root, primaryStage);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
	
}