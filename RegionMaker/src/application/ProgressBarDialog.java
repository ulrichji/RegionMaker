package application;

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressBarDialog extends Stage {
	
	public ProgressBarDialog(MapLoadTask task, Stage stageToBlock) {
		//Prepare root node
		ProgressBar progress_bar = new ProgressBar();
		progress_bar.progressProperty().bind(task.progressProperty());
		
		this.setScene(new Scene(progress_bar, 500, 50));
		
		//Set the stage to block it's parent stage and remove window buttons
		this.initModality(Modality.WINDOW_MODAL);
		this.initStyle(StageStyle.UNDECORATED);
		this.initOwner(stageToBlock);
	}
}
