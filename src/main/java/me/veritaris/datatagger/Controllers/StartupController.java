package me.veritaris.datatagger.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import me.veritaris.datatagger.Model.Model;
import me.veritaris.datatagger.Views.SceneType;
import me.veritaris.datatagger.Views.ViewController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class StartupController implements Initializable {
    @FXML protected Label foundDirsAmountLabel;
    @FXML protected Button selectDataFolderButton;
    @FXML protected Button goToTaggingButton;
    @FXML protected AnchorPane selectDataFolderAP;
    @FXML protected TextField dataFolderPathText;

    @FXML protected Label foundDatasetNameLabel;
    @FXML protected Label foundDatasetName;
    @FXML protected Label datasetFillingProgressLabel;
    @FXML protected ProgressBar datasetFillingProgress;

    private final Pattern dirNamePattern = Pattern.compile("\\d+");
    private File file;
    private static Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataFolderPathText.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        selectDataFolderAP.setOnDragOver(
            (event) -> {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        );
        selectDataFolderAP.setOnDragDropped(
            (event) -> {
                boolean success = false;
                File fileOrDir;
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasFiles()) {
                    fileOrDir = dragboard.getFiles().get(0);
                    if (fileOrDir.isDirectory()) {
                        success = true;
                        initDirectory(fileOrDir);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        );
    }

    @FXML
    public void selectDataFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        initDirectory(directoryChooser.showDialog(stage));
    }

    private void initDirectory(File selectedDirectory) {
        int filesAmount;
        filesAmount = getFilesAmount(selectedDirectory);
        file = selectedDirectory;

        if (!Model.getInstance().loadMetadata(file)) {
            Model.getMetadata().setImagesAmount(filesAmount);
            Model.getInstance().saveProgress();
        } else {
            foundDatasetNameLabel.setVisible(true);
            datasetFillingProgressLabel.setVisible(true);
            foundDatasetName.setVisible(true);
            foundDatasetName.setText(Model.getMetadata().getDatasetName());
            datasetFillingProgress.setVisible(true);
            datasetFillingProgress.setProgress(Model.getMetadata().getLastTaggedImage() / (double)Model.getMetadata().getImagesAmount());
            datasetFillingProgress.setTooltip(new Tooltip(String.format("%s/%s", Model.getMetadata().getLastTaggedImage(), Model.getMetadata().getImagesAmount())));
        }

        foundDirsAmountLabel.setText(String.valueOf(filesAmount));
        dataFolderPathText.setText(selectedDirectory.getAbsolutePath());

        goToTaggingButton.setDisable(filesAmount == 0);
    }

    private int getFilesAmount(File file) {
        if (file.isDirectory()) {
            return (int) Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .filter(
                    (innerFile) -> innerFile.isDirectory() && dirNamePattern.matcher(innerFile.getName()).matches()
                )
                .count();
        }
        return 0;
    }

    public static void setStage(Stage stage) {
        StartupController.stage = stage;
    }

    @FXML
    protected void goToTagging(ActionEvent actionEvent) throws IOException {
        ViewController.switchStage(stage, SceneType.IMAGE_TEXT).show();
    }
}
