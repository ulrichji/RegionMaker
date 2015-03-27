package properties;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import application.Main;

public class PropertiesWindowController
{
	@FXML private ListView<String> lv_selectionList;
	@FXML private AnchorPane ap_selectedMenu;
	@FXML private Label l_logLabel;
	private ObservableList<String> menus=FXCollections.observableArrayList("Map View","Ocean Generation");
	private Properties tempProperties;
	
	private Stage stage;
	public void initialize()
	{
		lv_selectionList.setItems(menus);
		lv_selectionList.getSelectionModel().selectedItemProperty().addListener(new MenuSelectionChange(this));
		tempProperties=new Properties(Main.getProperties());
	}
	
	public void showPropertiesWindow()
	{
		try
		{
			stage=new Stage();
			stage.setTitle("Properties");
			FXMLLoader loader=new FXMLLoader();
			loader.setController(this);
			AnchorPane pane=(AnchorPane)loader.load(this.getClass().getResourceAsStream("propertiesWindow.fxml"));
			Scene scene=new Scene(pane);
			stage.setScene(scene);
			stage.show();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadFXML(String fxml,Object controller)
	{
		try
		{
			FXMLLoader loader=new FXMLLoader();
			loader.setController(controller);
			Parent parent=(Parent)loader.load(this.getClass().getResourceAsStream(fxml));
			ap_selectedMenu.getChildren().clear();
			ap_selectedMenu.getChildren().add(parent);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@FXML public void applyChanges()
	{
		Main.setProperties(tempProperties);
		tempProperties.saveProperties();
	}
	
	@FXML public void applyChangesAndClose()
	{
		applyChanges();
		closeWindow();
	}
	
	@FXML public void closeWindow()
	{
		stage.close();
	}
	
	private class MenuSelectionChange implements ChangeListener<String>
	{
		private PropertiesWindowController controller;
		public MenuSelectionChange(PropertiesWindowController controller)
		{
			this.controller=controller;
		}
		@Override
		public void changed(ObservableValue<? extends String> observable,String oldValue,String newValue)
		{
			if(newValue.equals("Ocean Generation")) // FIXME this is stupid...
			{
				loadFXML("oceanGenerationProperties.fxml",
						new OceanGenerationPropertiesController(tempProperties,controller));
			}
			else if(newValue.equals("Map View"))
			{
				loadFXML("mapViewProperties.fxml",new MapViewPropertiesController(tempProperties,controller));
			}
		}
	}

	public void errorLog(String string)
	{
		l_logLabel.setText(string);
		l_logLabel.setStyle("-fx-font-color:red");
	}

	public void printLog(String string)
	{
		l_logLabel.setText(string);
		l_logLabel.setStyle("-fx-font-color:black");
	}
	
	public void clearLog()
	{
		l_logLabel.setText("");
	}
}
