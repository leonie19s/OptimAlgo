package project.gui;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import project.algorithms.*;
import project.neighborhoods.Neighborhood;
import project.problems.PackingSolution;
import project.problems.PackingRectangle;
import project.problems.RectanglePackingProblem;
import project.selection.SelectionStrategy;
import project.utils.*;

import java.util.List;


public class MainController {

    private BorderPane root = new BorderPane();
    private PackingView packingView = new PackingView(800,600);
    private ConfigPanel configPanel = new ConfigPanel();
    private ControlPanel controlPanel = new ControlPanel();
    private RectanglePackingProblem currentProblem;

    private Algorithm<?, PackingSolution>  algorithm;
    private int count;
    private boolean algorithmInitialized = false;
    private enum AppState {
        NO_INSTANCE,
        INSTANCE_READY
    }
    private AppState state = AppState.NO_INSTANCE;

    public MainController() {
        Pane canvasWrapper = new Pane(packingView);
        canvasWrapper.setPrefSize(2000, 2000);
        ScrollPane centerPane = new ScrollPane(canvasWrapper);
        centerPane.setPannable(true); // allow dragging with mouse

        centerPane.setFitToWidth(false);
        centerPane.setFitToHeight(false);
        root.setCenter(centerPane);
        root.setTop(configPanel);
        root.setBottom(controlPanel);

        controlPanel.setPrefHeight(60);
        packingView.widthProperty().bind(canvasWrapper.widthProperty());
        packingView.heightProperty().bind(canvasWrapper.heightProperty());

        //packingView.widthProperty().bind(centerPane.widthProperty());
        //packingView.heightProperty().bind(centerPane.heightProperty());

        packingView.widthProperty().addListener((obs, o, n) -> packingView.redraw());
        packingView.heightProperty().addListener((obs, o, n) -> packingView.redraw());

        setupActions();
    }

    private void updateUIState() {
        boolean hasInstance = (state == AppState.INSTANCE_READY);

        // Instance controls
        configPanel.instanceControls.setDisable(hasInstance);

        // Show/hide clear button
        configPanel.clearInstancesBtn.setVisible(hasInstance);
        configPanel.clearInstancesBtn.setManaged(hasInstance);

        // Algorithm controls
        configPanel.algorithmChoice.setDisable(!hasInstance);
        configPanel.initAlgorithmBtn.setDisable(!hasInstance);

        // Execution controls
        controlPanel.runBtn.setDisable(!hasInstance);
        controlPanel.nextStepBtn.setDisable(!hasInstance);
    }

    private void generateInstance() {
        int boxSize = Integer.parseInt(configPanel.boxSizeField.getText());
        int count = Integer.parseInt(configPanel.rectCountField.getText());
        int minH = Integer.parseInt(configPanel.minHField.getText());
        int maxH = Integer.parseInt(configPanel.maxHField.getText());
        int minW = Integer.parseInt(configPanel.minWField.getText());
        int maxW = Integer.parseInt(configPanel.maxWField.getText());
        this.count = count;
        InstanceGenerator gen =
                new InstanceGenerator(boxSize, count, maxW, minW, maxH, minH, 123);

        currentProblem = gen.generate();

        algorithm = null;
        algorithmInitialized = false;
        state = AppState.INSTANCE_READY;
        updateUIState();
    }
    private void setupActions() {

        configPanel.clearInstancesBtn.setOnAction(e -> {
            currentProblem = null;
            algorithm = null;
            algorithmInitialized = false;

            packingView.getGraphicsContext2D().clearRect(0, 0,
                    packingView.getWidth(), packingView.getHeight());

            state = AppState.NO_INSTANCE;
            updateUIState();
        });

        // NEXT STEP
        controlPanel.nextStepBtn.setOnAction(e -> {
            if (!algorithmInitialized) {
                return;// todo: warning popup
            }
            if (algorithm != null && algorithm.hasNext()) {
                PackingSolution next = algorithm.next();
                packingView.draw(algorithm.getCurrent());}
            if (algorithm != null && !algorithm.hasNext()) {
                showAlgorithmFinishedPopup();
            }
        });

        // RUN TO COMPLETION (from current state)
        controlPanel.runBtn.setOnAction(e -> {
            if (!algorithmInitialized) {
                return;// todo: warning popup
            }
            if (algorithm != null) {
                while (algorithm.hasNext()) {
                    algorithm.next();
                    //packingView.draw(algorithm.getCurrent());
                }
                packingView.draw(algorithm.getCurrent());
            }
            showAlgorithmFinishedPopup();
        });

        // RESET
        controlPanel.resetBtn.setOnAction(e -> {
            packingView.getGraphicsContext2D().clearRect(0, 0,
                    packingView.getWidth(), packingView.getHeight());
            algorithm = null;
            algorithmInitialized = false;
        });

        // generate instances
        configPanel.generateInstancesBtn.setOnAction(e -> {
            generateInstance();


        });

        configPanel.initAlgorithmBtn.setOnAction(e -> {
            initializeAlgorithm();
            algorithmInitialized = true;
        });
    }
    private void showAlgorithmFinishedPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Algorithm Finished");
        alert.setHeaderText(null);
        alert.setContentText("The algorithm has finished running.");

        alert.showAndWait();
    }
    private void initializeAlgorithm() {

        if (state != AppState.INSTANCE_READY) return;

        if (currentProblem == null){
            return;
        }

        String algo = configPanel.algorithmChoice.getValue();
        PackingSolution sol = null;
        switch (algo) {
            case "Greedy":
                String strategyName = configPanel.selectionStrategyChoice.getValue();
                System.out.println(strategyName);
                SelectionStrategy<PackingRectangle> strategy = SelectionStrategyFactory.create(strategyName);
                AlgoArgs<PackingRectangle, PackingSolution> algoArgs = new AlgoArgs<>();
                algoArgs.setProblem(currentProblem);
                algoArgs.setStrategy(strategy);

                Algorithm<List<PackingRectangle>, PackingSolution> typedAlgorithm =
                        (Algorithm<List<PackingRectangle>, PackingSolution>) AlgorithmFactory.createAlgorithm("Greedy", algoArgs);
                sol = currentProblem.createGreedyState();
                typedAlgorithm.initialize(currentProblem.getRectangles(), sol);
                algorithm = typedAlgorithm;
                break;

            case "LocalSearch":
                String neighborhoodName = configPanel.neighborhoodChoice.getValue();

                Neighborhood<PackingSolution> neighborhood = NeighborhoodFactory.create(neighborhoodName);
                AlgoArgs<PackingRectangle, PackingSolution> algoArgs2 = new AlgoArgs<>();
                if (neighborhoodName.equals("overlap")){
                    algoArgs2.setnRecs(this.count);
                }
                algoArgs2.setProblem(currentProblem);
                algoArgs2.setNeighborhood(neighborhood);
                algoArgs2.setnNeigh(50);
                algoArgs2.setCriteria(CriteriaFactory.create("maxIterations:5000"));
                algorithm = AlgorithmFactory.createAlgorithm("LocalSearch", algoArgs2);

                if (neighborhoodName.equals("Overlap")){
                    sol = currentProblem.createInitialSolutionOverlap();
                    sol.setAllRectangles(currentProblem.getRectangles());
                }
                else{
                    sol = currentProblem.createInitialSolution();
                }

                algorithm = AlgorithmFactory.createAlgorithm("LocalSearch", algoArgs2);                  // keep generic variable for backend calls
                algorithm.initialize(null, sol);  // works if initialize accepts Void (or null)

                break;
        }

        packingView.draw(sol);
        algorithmInitialized = true;
    }

    public Parent getRoot() {
        return root;
    }

    }
