package it.fmoon.fxapp.system.home;

import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.components.FxLoader;
import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.components.menu.MenuManager;
import it.fmoon.fxapp.components.menu.PageMenuItem;
import it.fmoon.fxapp.mvc.AbstractActivity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HomeActivity extends AbstractActivity<HomeActivityDef> {


	@Autowired
	private MenuManager menuManager;
	
	@Autowired
	ActivityManager activityManager;

	@Autowired
	private FxLoader loader;
	
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
	
	protected Node createPageHomeIconView(PageMenuItem menuItem) {
		PageDef pageDef = menuItem.getPageDef();
		Node menuItemV = loader.loadView(this,"HomeButtonItem");
		((Label)menuItemV.lookup("#label")).setText(menuItem.getLabel());
		((FontIcon)menuItemV.lookup("#icon")).setIconLiteral(menuItem.getIcon());
		menuItemV.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			activityManager.startPage(pageDef).subscribe();
		});
		return menuItemV;
	}

	protected Node createActivityHomeIconView(ActivityMenuItem menuItem) {
		ActivityDef<?> activityDef = menuItem.getActivityDef();
		Node menuItemV = loader.loadView(this,"HomeButtonItem");
		((Label)menuItemV.lookup("#label")).setText(menuItem.getLabel());
		((FontIcon)menuItemV.lookup("#icon")).setIconLiteral(menuItem.getIcon());
		menuItemV.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			activityManager.startActivity(activityDef).subscribe();
		});
		return menuItemV;
	}
	
}
