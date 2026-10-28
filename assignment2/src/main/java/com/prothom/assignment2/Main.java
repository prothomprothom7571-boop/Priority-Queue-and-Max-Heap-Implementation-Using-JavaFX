
package com.prothom.assignment2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private final List<String> items = new ArrayList<>();
    private final List<Integer> priorities = new ArrayList<>();
    private final List<Integer> heap = new ArrayList<>();

    private TextField itemField;
    private TextField priorityField;
    private TextField heapField;
    private TextArea pqOutput;
    private TextArea heapOutput;

    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane();

        Tab pqTab = new Tab("Priority Queue");
        pqTab.setClosable(false);
        pqTab.setContent(createPriorityQueue());

        Tab heapTab = new Tab("Max Heap");
        heapTab.setClosable(false);
        heapTab.setContent(createHeap());

        tabs.getTabs().addAll(pqTab, heapTab);

        Label heading = new Label("ASSIGNMENT 2 - DATA STRUCTURES");
        heading.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");

        VBox root = new VBox(15, heading, tabs);
        root.setPadding(new Insets(20));

        stage.setTitle("Assignment 2");
        stage.setScene(new Scene(root, 850, 600));
        stage.show();
    }

    // Priority Queue
    private VBox createPriorityQueue() {
        itemField = new TextField();
        itemField.setPromptText("Item name");

        priorityField = new TextField();
        priorityField.setPromptText("Priority number");

        Button insert = new Button("Insert");
        Button remove = new Button("Remove");
        Button peek = new Button("Peek");
        Button display = new Button("Display");

        pqOutput = new TextArea();
        pqOutput.setEditable(false);

        insert.setOnAction(e -> {
            String item = itemField.getText().trim();

            try {
                if (item.isEmpty()) {
                    pqOutput.appendText("Enter an item name.\n");
                    return;
                }

                int priority = Integer.parseInt(
                        priorityField.getText().trim()
                );

                int i = 0;
                while (i < priorities.size()
                        && priorities.get(i) >= priority) {
                    i++;
                }

                items.add(i, item);
                priorities.add(i, priority);

                pqOutput.appendText(
                        "Inserted: " + item
                                + ", Priority: " + priority + "\n"
                );

                itemField.clear();
                priorityField.clear();

            } catch (NumberFormatException ex) {
                pqOutput.appendText(
                        "Enter a valid priority number.\n"
                );
            }
        });


        remove.setOnAction(e -> {
            if (items.isEmpty()) {
                pqOutput.appendText("Priority Queue is empty.\n");
                return;
            }

            String removedItem = items.remove(0);
            int removedPriority = priorities.remove(0);

            pqOutput.appendText(
                    "Successfully removed: " + removedItem
                            + " (Priority: " + removedPriority + ")\n"
            );
        });


        peek.setOnAction(e -> {
            if (items.isEmpty()) {
                pqOutput.appendText("Priority Queue is empty.\n");
            } else {
                pqOutput.appendText(
                        "Highest priority: " + items.get(0)
                                + ", Priority: " + priorities.get(0) + "\n"
                );
            }
        });

        display.setOnAction(e -> {
            if (items.isEmpty()) {
                pqOutput.appendText("Priority Queue is empty.\n");
            } else {
                pqOutput.appendText("Queue (highest priority first):\n");

                for (int i = 0; i < items.size(); i++) {
                    pqOutput.appendText(
                            items.get(i) + " [Priority: "
                                    + priorities.get(i) + "]\n"
                    );
                }
            }
        });

        HBox input = new HBox(10, itemField, priorityField);
        HBox buttons = new HBox(10, insert, remove, peek, display);

        VBox box = new VBox(
                15,
                new Label("Priority Queue - ArrayList Implementation"),
                input,
                buttons,
                pqOutput
        );

        box.setPadding(new Insets(20));
        return box;
    }

    // Max Heap
    private VBox createHeap() {
        heapField = new TextField();
        heapField.setPromptText("Enter integer");

        Button insert = new Button("Insert");
        Button extract = new Button("Extract Maximum");
        Button peek = new Button("Peek Maximum");
        Button display = new Button("Display Heap");

        heapOutput = new TextArea();
        heapOutput.setEditable(false);

        insert.setOnAction(e -> {
            try {
                int value = Integer.parseInt(
                        heapField.getText().trim()
                );

                heap.add(value);
                int i = heap.size() - 1;

                while (i > 0) {
                    int parent = (i - 1) / 2;

                    if (heap.get(parent) >= heap.get(i)) {
                        break;
                    }

                    swap(i, parent);
                    i = parent;
                }

                heapOutput.appendText("Inserted: " + value + "\n");
                heapField.clear();

            } catch (NumberFormatException ex) {
                heapOutput.appendText("Enter a valid integer.\n");
            }
        });

        extract.setOnAction(e -> {
            if (heap.isEmpty()) {
                heapOutput.appendText("Max Heap is empty.\n");
                return;
            }

            int max = heap.get(0);
            int last = heap.remove(heap.size() - 1);

            if (!heap.isEmpty()) {
                heap.set(0, last);
                heapifyDown(0);
            }

            heapOutput.appendText("Extracted maximum: " + max + "\n");
        });

        peek.setOnAction(e -> {
            if (heap.isEmpty()) {
                heapOutput.appendText("Max Heap is empty.\n");
            } else {
                heapOutput.appendText(
                        "Maximum value: " + heap.get(0) + "\n"
                );
            }
        });

        display.setOnAction(e -> {
            if (heap.isEmpty()) {
                heapOutput.appendText("Max Heap is empty.\n");
            } else {
                heapOutput.appendText("Heap array: " + heap + "\n");
            }
        });

        HBox buttons = new HBox(
                10, insert, extract, peek, display
        );

        VBox box = new VBox(
                15,
                new Label("Max Heap - ArrayList Implementation"),
                heapField,
                buttons,
                heapOutput
        );

        box.setPadding(new Insets(20));
        return box;
    }

    private void heapifyDown(int i) {
        while (true) {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < heap.size()
                    && heap.get(left) > heap.get(largest)) {
                largest = left;
            }

            if (right < heap.size()
                    && heap.get(right) > heap.get(largest)) {
                largest = right;
            }

            if (largest == i) {
                break;
            }

            swap(i, largest);
            i = largest;
        }
    }

    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
