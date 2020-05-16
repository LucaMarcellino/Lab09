
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	int anno=0;
    	List<Country> nazioni; 
    	
    	try {
			anno= Integer.parseInt(txtAnno.getText());
		} catch (NumberFormatException nfe) {
			txtResult.appendText("Scegli un numero intero figlio di puttana");
		}
    	
    	if(anno<1816 || anno>2016) {
    		txtResult.appendText("Un numero compreso tra 1816 e 2016 lurido bastardo");
    	}
    	
    	nazioni=new ArrayList<>(model.getListaNazionii(anno));
    	
    	for(Country c : nazioni ) {
    		txtResult.appendText("Nazione "+c+" è connesso a "+ model.numeroArchi(anno, c)+" stati\n");
    	}
    	txtResult.appendText("Numero stati presenti: "+model.numeroVertex(anno));
    	
    	txtAnno.clear();

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	txtResult.setEditable(false);
    }
}
