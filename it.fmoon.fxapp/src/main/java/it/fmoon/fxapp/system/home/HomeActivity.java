package it.fmoon.fxapp.system.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.components.menu.MenuManager;
import it.fmoon.fxapp.components.menu.PageMenuItem;
import it.fmoon.fxapp.mvc.AbstractActivity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HomeActivity extends AbstractActivity<HomeActivityDef> {


	@Autowired
	private MenuManager menuManager;
	
	@Autowired
	ActivityManager activityManager;

	@FXML FlowPane pane;

	public HomeActivity(HomeActivityDef def) {
		super(def);
	}

	@FXML
    public void initialize() {
		this.menuManager.getApplicationMenusObs().subscribe(appMenuList->{
			this.redrawHomeIcons(appMenuList);
		});
    }

	private void redrawHomeIcons(List<AppMenuItem> appMenuList) {
		pane.getChildren().clear();
		appMenuList.forEach(menuItem -> {
			if (menuItem instanceof ActivityMenuItem) {
				Node view = createActivityHomeIconView((ActivityMenuItem) menuItem);
				pane.getChildren().add(view);
			} else if (menuItem instanceof PageMenuItem) {
				Node view = createPageHomeIconView((PageMenuItem) menuItem);
				pane.getChildren().add(view);
			}
		});
	}

	private Node createPageHomeIconView(PageMenuItem menuItem) {
		PageDef pageDef = menuItem.getPageDef();
		Button button = new Button("Page: "+pageDef.getName());
    	button.setOnAction((ActionEvent e)->{
    		activityManager.startPage(pageDef).subscribe();
	    });
		return button;
	}

	private Node createActivityHomeIconView(ActivityMenuItem menuItem) {
    	ActivityDef<?> activityDef = menuItem.getActivityDef();
		Button button = new Button("Activity: "+activityDef.getName());
    	button.setOnAction((ActionEvent e)->{
    		activityManager.startActivity(activityDef).subscribe();
	    });
		return button;
	}
	
}
