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
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PropertiesWindowController
{
	@FXML private ListView<String> lv_selectionList;
	@FXML private AnchorPane ap_selectedMenu;
	private ObservableList<String> menus=FXCollections.observableArrayList("Map","Ocean Generation");
	
	public void initialize()
	{
		lv_selectionList.setItems(menus);
		lv_selectionList.getSelectionModel().selectedItemProperty().addListener(new MenuSelectionChange());
	}
	
	public void showPropertiesWindow()
	{
		try
		{
			Stage stage=new Stage();
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
	
	private class MenuSelectionChange implements ChangeListener<String>
	{
		@Override
		public void changed(ObservableValue<? extends String> observable,String oldValue,String newValue)
		{
			if(newValue.equals("Ocean Generation")) // FIXME this is stupid...
			{
				loadFXML("oceanGenerationProperties.fxml",new OceanGenerationPropertiesController());
			}
			else if(newValue.equals("Map"))
			{
			}
		}
	}
}
