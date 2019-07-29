package it.fmoon.fxapp.support;

public class NamesUtils {

	public static String viewNameFromControllerName(String controllerName) {
		String viewName =controllerName;
		if (viewName.endsWith("Controller")) {
			viewName = viewName.substring(0, viewName.length()-10);
		}
		if (viewName.endsWith("Activity")) {
			viewName = viewName.substring(0, viewName.length()-8);
		}
		viewName = viewName + "View";
		return viewName;
	}

	public static String pageNameFromPageDefName(String pageDefName) {
		String pageName = pageDefName;
		if (pageName.endsWith("Def")) {
			pageName = pageName.substring(0, pageName.length()-3);
		}
		return pageName;
	}

}
