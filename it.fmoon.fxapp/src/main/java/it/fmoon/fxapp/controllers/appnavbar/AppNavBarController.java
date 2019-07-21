package it.fmoon.fxapp.controllers.appnavbar;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Streams;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.controllers.application.AppMenuState;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.mvc.Activity;
import it.fmoon.fxapp.mvc.Page;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

@Component
public class AppNavBarController 
	extends AbstractController
{
		
		private PublishSubject<ActionEvent> onMenu = PublishSubject.create();
		
		private PublishSubject<ActionEvent> onClose = PublishSubject.create();
		private PublishSubject<ActionEvent> onLogin = PublishSubject.create();
		
		@FXML HBox breadcrumb;
		
		@Autowired
		ActivityManager activityManager;

		private ObservableValue<AppMenuState> appMenuState;

		@FXML Button menuButton;

		@FXML HBox navBar;
	
		@FXML
		public void initialize() {
			activityManager.onNavigationStack()
				.debounce(200,TimeUnit.MILLISECONDS)
				.subscribe(this::updateBreadcrumb);
			
			DropShadow shadow1 = new DropShadow();
			shadow1.setOffsetX(0);
			shadow1.setOffsetY(5);
			navBar.setEffect(shadow1);
		}

		@FXML 
		public void onMenu(ActionEvent event) {
			onMenu.onNext(event);	
		}
		public Observable<ActionEvent> onMenu() {
			return onMenu;
		}
		
		@FXML
		public void onClose(ActionEvent event) {
			onClose.onNext(event);
		}
		public Observable<ActionEvent> onClose() {
			return onClose;
		}
		
		@FXML
		public void onLogin(ActionEvent event) {
			onLogin.onNext(event);
		}
		public Observable<ActionEvent> onLogin() {
			return onLogin;
		}
	
		public void updateBreadcrumb(List<Page> navStack) {
			System.out.println("---> updateBreadcrumb");
			
			
			Platform.runLater(()->{
				breadcrumb.getChildren().clear();
				flatStack(navStack).forEachOrdered(obj->{
					
					if (obj instanceof Page) {
						Page page = (Page)obj;
						Node p = pageBreadcrumbNode(page );
						breadcrumb.getChildren().add(p);
					}
					if (obj instanceof Activity) {
						Activity act = (Activity)obj;
						Node a = activityBreadcrumbNode(act);
						breadcrumb.getChildren().add(a);
					}
				});
			});
			
		}

		private Stream<Object> flatStack(List<Page> navStack) {
			return navStack.stream()
				.flatMap(page->Streams.concat(Stream.of(page),page.getActivityStack().stream()));
		}

		private Node activityBreadcrumbNode(Activity act) {

			Label labelA = new Label();
			
			FontIcon fontIconA = new FontIcon(FontAwesome.ANGLE_RIGHT);
			fontIconA.setFill(Color.WHITESMOKE);
			labelA.setGraphic(fontIconA);
			
			labelA.setText(act.getTitle());
			labelA.setTextFill(Color.WHITESMOKE);
			return labelA;
		}

		private Node pageBreadcrumbNode(Page page) {
			Label label = new Label();
			
			FontIcon fontIcon = new FontIcon(FontAwesome.FILE_O);
			fontIcon.setFill(Color.WHITESMOKE);					
			label.setGraphic(fontIcon);
			
			label.setText(page.getTitle());
			label.setTextFill(Color.WHITESMOKE);
			return label;
		}

		public void setMenuState(ObservableValue<AppMenuState> appMenuState) {
			this.appMenuState = appMenuState;
			this.appMenuState.addListener((observable, oldValue, newValue)->{
				updateMenuIcon(newValue);
			});
			updateMenuIcon(this.appMenuState.getValue());
		}

		private void updateMenuIcon(AppMenuState appMenuState) {
			FontIcon fontIcon = new FontIcon(stateToIcon(appMenuState));
			fontIcon.setIconSize(24);
			fontIcon.setFill(Color.WHITESMOKE);		
			
			this.menuButton.setGraphic(fontIcon);
		}

		private FontAwesome stateToIcon(AppMenuState newValue) {
			switch (newValue) {
			case CLOSED:
				return FontAwesome.BARS;
			case OPEN_OVERLAY: 
				return FontAwesome.CHEVRON_DOWN;
			case OPEN_MOUNTED: 
				return FontAwesome.CHEVRON_LEFT;
			}
			return null;
		}
		
}
