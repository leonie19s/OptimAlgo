package project.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ConfigPanel extends GridPane {

    /* ───────────── Instance controls ───────────── */

    public TextField boxSizeField = new TextField("8");
    public TextField rectCountField = new TextField("20");

    public TextField minWField = new TextField("1");
    public TextField maxWField = new TextField("5");
    public TextField minHField = new TextField("1");
    public TextField maxHField = new TextField("5");

    public Button generateInstancesBtn = new Button("Generate Instances");
    public Button clearInstancesBtn = new Button("Clear Instances");

    /** Container for all instance-related controls */
    public VBox instanceControls;

    /* ───────────── Algorithm controls ───────────── */

    public ComboBox<String> algorithmChoice = new ComboBox<>();
    public ComboBox<String> selectionStrategyChoice = new ComboBox<>();
    public ComboBox<String> neighborhoodChoice = new ComboBox<>();

    public Button initAlgorithmBtn = new Button("Initialize Algorithm");

    private final Label dependentLabel = new Label();

    public ConfigPanel() {
        setHgap(30);
        //setVgap(10);
        setPadding(new Insets(15));


        setupAlgorithmControls();
        buildLayout();
        updateDependentControls();
        algorithmChoice.valueProperty().addListener(
                (obs, oldVal, newVal) -> updateDependentControls()
        );

    }
    /* ───────────── Layout ───────────── */

    private void buildLayout() {

        instanceControls = new VBox(8,
                new Label("Box Size"),
                boxSizeField,

                new Label("Rectangles"),
                rectCountField,

                new Label("Min Width"),
                minWField,

                new Label("Max Width"),
                maxWField,

                new Label("Min Height"),
                minHField,

                new Label("Max Height"),
                maxHField,

                generateInstancesBtn

        );
        // clear button is NOT inside instanceControls
        clearInstancesBtn.setVisible(false);
        clearInstancesBtn.setManaged(false);
        VBox leftColumn = new VBox(8,
                instanceControls,
                clearInstancesBtn
        );

        VBox algorithmControls = new VBox(8,
                new Label("Algorithm"),
                algorithmChoice,

                dependentLabel,
                selectionStrategyChoice,
                neighborhoodChoice,

                initAlgorithmBtn
        );


        add(leftColumn, 0, 0);
        add(algorithmControls, 1, 0);
    }

    /* ───────────── Algorithm setup ───────────── */

    private void setupAlgorithmControls() {
        algorithmChoice.getItems().addAll("Greedy", "LocalSearch");
        algorithmChoice.getSelectionModel().selectFirst();

        selectionStrategyChoice.getItems().addAll(
                "BiggestFirst",
                "LargestSidelengthFirst"
        );

        neighborhoodChoice.getItems().addAll(
                "GeometryBased",
                "RuleBased",
                "Overlap"
        );
    }

    private void updateDependentControls() {
        boolean greedy = "Greedy".equals(algorithmChoice.getValue());

        dependentLabel.setText(greedy ? "Selection Strategy" : "Neighborhood");

        selectionStrategyChoice.setVisible(greedy);
        selectionStrategyChoice.setManaged(greedy);

        neighborhoodChoice.setVisible(!greedy);
        neighborhoodChoice.setManaged(!greedy);
    }
}