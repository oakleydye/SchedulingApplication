package Forms;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class ReportViewerController {
    @FXML TableView grdReport;

    public void init(List<String> columns){
        for (String column : columns){
            grdReport.getColumns().add(new TableColumn<>(column));
        }
    }
}
