package it.fmoon.fxapp.controllers.appnavbar;

import org.springframework.stereotype.Component;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import it.fmoon.fxapp.mvc.AbstractController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

@Component
public class AppNavBarController 
	extends AbstractController
{
		
		private PublishSubject<ActionEvent> onMenu = PublishSubject.create();
		private PublishSubject<ActionEvent> onClose = PublishSubject.create();
		private PublishSubject<ActionEvent> onLogin = PublishSubject.create();
	
		@FXML
		public void initialize() {
			
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
	
}
