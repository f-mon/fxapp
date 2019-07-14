package it.fmoon.fxapp.system.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.mvc.AbstractActivity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HomeActivity extends AbstractActivity<HomeActivityDef> {

	@Autowired
	List<ActivityDef<?>> allActivityDefs;
	
	@Autowired
	List<PageDef> allPagesDefs;

	@Autowired
	ActivityManager activityManager;

	@FXML FlowPane pane;

	public HomeActivity(HomeActivityDef def) {
		super(def);
	}

	@FXML
    public void initialize() {
        this.allActivityDefs.forEach(actDef->{
        	Button button = new Button("Activity: "+actDef.getName());
        	button.setOnAction((ActionEvent e)->{
        		activityManager.startActivity(actDef).subscribe();
    	    });
        	this.pane.getChildren().add(button);
        });
        
        this.allPagesDefs.forEach(pageDef->{
        	Button button = new Button("Page: "+pageDef.getName());
        	button.setOnAction((ActionEvent e)->{
        		activityManager.startPage(pageDef).subscribe();
    	    });
        	this.pane.getChildren().add(button);
        });
    }
	
}
