
for launch:

add to VM OPtion in eclipse run/DEbug config

In One line (formatted for clarity)

-Djava.library.path=
	"C:\Program Files (x86)\Java\zulu11.31.11-ca-fx-jdk11.0.3-win_x64\bin" 
--module-path  
	C:\Users\monaldfe\.m2\repository\org\openjfx\javafx-controls\12.0.1\javafx-controls-12.0.1-win.jar;
	C:\Users\monaldfe\.m2\repository\org\openjfx\javafx-base\12.0.1\javafx-base-12.0.1-win.jar;
	C:\Users\monaldfe\.m2\repository\org\openjfx\javafx-graphics\12.0.1\javafx-graphics-12.0.1-win.jar;
	C:\Users\monaldfe\.m2\repository\org\openjfx\javafx-fxml\12.0.1\javafx-fxml-12.0.1-win.jar 
--add-modules=
	javafx.controls,
	javafx.base,
	javafx.graphics,
	javafx.fxml

	
	