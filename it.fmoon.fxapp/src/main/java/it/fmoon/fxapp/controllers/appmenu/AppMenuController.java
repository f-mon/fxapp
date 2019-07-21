package it.fmoon.fxapp.controllers.appmenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.MenuManager;
import it.fmoon.fxapp.components.menu.PageMenuItem;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

@Component
public class AppMenuController 
	extends AbstractController 
{

	@FXML
	private VBox menuPanel;
	
	@Autowired
	private MenuManager menuManager;
	
	@Autowired
	private ActivityManager activityManager;
	
	@FXML
	public void initialize() {
		
		 this.menuManager.getApplicationMenusObs().subscribe(appMenuList->{
			
			 this.menuPanel.getChildren().clear();
			 appMenuList.forEach(menuItem->{
				 
				 	if (menuItem instanceof ActivityMenuItem) {
				 		
				 		ActivityDef<?> activityDef = ((ActivityMenuItem) menuItem).getActivityDef();
				 		
				 		Button button = new Button("Activity: "+menuItem.getLabel());
				 		button.setOnAction((ActionEvent e)->{
				 			activityManager.startActivity(activityDef).subscribe();
				 		});
				 		this.menuPanel.getChildren().add(button);
				 		
				 	} 
				 	else if (menuItem instanceof PageMenuItem) {
				 		
				 		PageDef pageDef = ((PageMenuItem) menuItem).getPageDef();
				 		
				 		Button button = new Button("Page: "+menuItem.getLabel());
				 		button.setOnAction((ActionEvent e)->{
				 			activityManager.startPage(pageDef).subscribe();
				 		});
				 		this.menuPanel.getChildren().add(button);
				 	}
				 
		        });
			 
		 });
	        
		
	}
	
}
