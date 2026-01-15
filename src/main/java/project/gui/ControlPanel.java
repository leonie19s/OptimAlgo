package project.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ControlPanel extends HBox {
    public Button nextStepBtn = new Button("Next Step");
    public Button runBtn = new Button("Run Algorithm");
    public Button resetBtn = new Button("Reset");

    public ControlPanel() {
        super(10);
        getChildren().addAll(nextStepBtn, runBtn, resetBtn);
        setPadding(new Insets(10));
    }
}
