import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SortingVisualizer extends JPanel {

    private static final int WIDTH = 1200; //width of the GUI
    private static final int HEIGHT = 800; //height of the GUI
    private static final int BAR_WIDTH = 15; //width of the bars representing array values
    private static final int NUM_BARS = WIDTH / BAR_WIDTH; // Number of bars in the array
    private static final int MIN_BAR_HEIGHT = 20; //min height of the bar on the screen
    private static final int MAX_BAR_HEIGHT = HEIGHT - 60; //mas height of the bar on the screen
    private  int SORTING_DELAY = 100; // delay between refreshing the slow, i.e., maintain the speed of sorting.

    private int[] array; //to store the randomly generated array
    private JComboBox<String> sortingAlgorithms; //dropdown menu
    private JButton startButton; //start button
    private JSlider speedSlider; //slider to set the speed

    private Thread sortingThread;

    // function to initialize the interface
    public SortingVisualizer() {
        array = new int[NUM_BARS];
//        Random rng = new Random();
//        for (int i = 0; i < NUM_BARS; i++) {
//            array[i] = MIN_BAR_HEIGHT + rng.nextInt(MAX_BAR_HEIGHT - MIN_BAR_HEIGHT);
//        }
        resetArray(); // rests the array with random numbers

        sortingAlgorithms = new JComboBox<String>();
        sortingAlgorithms.addItem("Bubble Sort");
        sortingAlgorithms.addItem("Selection Sort");
        sortingAlgorithms.addItem("Insertion Sort");
        sortingAlgorithms.addItem("Quicksort");
        startButton = new JButton("Start");


        // add event listener for startbutton
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);

                //create a new thread to run the algorihms
                sortingThread = new Thread(new Runnable() {
                    public void run() {
                        runSortingAlgorithm();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                startButton.setEnabled(true);

                            }
                        });
                    }
                });
                sortingThread.start();
            }
        });


        //initiaze the speedslider
        speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 20);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SORTING_DELAY = 200 - speedSlider.getValue();
            }
        });

        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        speedSlider.setMajorTickSpacing(25);
        speedSlider.setMinorTickSpacing(5);
        JLabel selectSpeedLabel = new JLabel("Select Speed: ");

        JButton resetButton = new JButton("Regenerate Array");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetArray();
                repaint();
            }
        });

        //set the positions of the elements on the interface
        add(sortingAlgorithms, BorderLayout.NORTH);
        add(startButton, BorderLayout.SOUTH);
        add(resetButton, BorderLayout.SOUTH);
        add(selectSpeedLabel, BorderLayout.SOUTH);
        add(speedSlider, BorderLayout.SOUTH);
    }

    // function to randomly generate the array
    private void resetArray() {
        Random rng = new Random();
        for (int i = 0; i < NUM_BARS; i++) {
            array[i] = MIN_BAR_HEIGHT + rng.nextInt(MAX_BAR_HEIGHT - MIN_BAR_HEIGHT);
        }
    }


    // function to create bars
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < NUM_BARS; i++) {
            int x = (i * BAR_WIDTH) + (i+1);
            int y = HEIGHT - array[i];
            int height = array[i];

            g.setColor(Color.BLACK);
            g.fillRect(x, y, BAR_WIDTH, height);
        }
    }

    
    public void runSortingAlgorithm() {
        String selectedAlgorithm = (String) sortingAlgorithms.getSelectedItem();
        switch (selectedAlgorithm) {
            case "Bubble Sort":
                bubbleSort();
                break;
            case "Selection Sort":
                selectionSort();
                break;
            case "Insertion Sort":
                insertionSort();
                break;
            case "Quicksort":
                quicksort(0, NUM_BARS-1);
                break;
            default:
                break;
        }
    }

    private void bubbleSort() {
        for (int i = 0; i < NUM_BARS - 1; i++) {
            for (int j = 0; j< NUM_BARS-i- 1; j++){ 
            if (array[j] > array[j + 1]) {
                swap(j, j + 1);
                repaint();
                System.out.println("change");
                try {
                    Thread.sleep(SORTING_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        }
    }


private void selectionSort() {
    for (int i = 0; i < NUM_BARS - 1; i++) {
        int minIndex = i;
        for (int j = i + 1; j < NUM_BARS; j++) {
            if (array[j] < array[minIndex]) {
                minIndex = j;
            }
        }
        swap(minIndex, i);
        repaint();
        try {
            Thread.sleep(SORTING_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

private void insertionSort() {
    for (int i = 1; i < NUM_BARS; i++) {
        int key = array[i];
        int j = i - 1;
        while (j >= 0 && array[j] > key) {
            array[j + 1] = array[j];
            j--;
            repaint();
            System.out.println("change");
            try {
                Thread.sleep(SORTING_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        array[j + 1] = key;
    }
}
    private void quicksort(int low, int high) {
        if (low < high) {
            int pivotIndex = partition(low, high);
            quicksort(low, pivotIndex - 1);
            quicksort(pivotIndex + 1, high);
        }
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(i, j);
                repaint();
                try {
                    Thread.sleep(SORTING_DELAY);
                } catch (InterruptedException e) {
                    break;
                }
                if (Thread.interrupted()) {
                    break;
                }
            }
        }
        swap(i + 1, high);
        repaint();
        try {
            Thread.sleep(SORTING_DELAY);
        } catch (InterruptedException e) {
            return i;
        }
        if (Thread.interrupted()) {
            return i;
        }
        return i + 1;
    }
private void swap(int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            JFrame frame = new JFrame("Sorting Visualizer");
            SortingVisualizer panel = new SortingVisualizer();
            frame.add(panel, BorderLayout.CENTER);
            frame.setSize(WIDTH+100, HEIGHT+40);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    });
}
}
