package br.poli.gp.util;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ChartDirector.*;

public class LineChart implements DemoModule
{

	public String[] dataX;
    public double[] dataY;
    public String textX;
    public String textY;
    public String nameGraphic;

    public LineChart(String[] x, double[] y, String textX_, String textY_, String nameGraphic_) {

        dataX = x.clone();
        dataY = y.clone();
        
        textX = textX_;
        textY = textY_;
        
        nameGraphic = nameGraphic_;

    }
   

    //Number of charts produced in this demo
    public int getNoOfCharts() { return 1; }

    //Main code for creating charts
    public void createChart(ChartViewer viewer, int chartIndex)
    {

        // Create a XYChart object of size 600 x 300 pixels, with a pale red (ffdddd) background,
        // black border, 1 pixel 3D border effect and rounded corners.
        XYChart c = new XYChart(600, 300, 0xffffff, 0x000000, 1);
        c.setRoundedFrame();

        // Set the plotarea at (55, 58) and of size 520 x 195 pixels, with white (ffffff)
        // background. Set horizontal and vertical grid lines to grey (cccccc).
        c.setPlotArea(55, 58, 520, 195, 0xffffff, -1, -1, 0x000000, 0x000000);

        // Add a legend box at (55, 32) (top of the chart) with horizontal layout. Use 9pt Arial
        // Bold font. Set the background and border color to Transparent.
        c.addLegend(55, 32, false, "Arial", 10).setBackground(Chart.Transparent);

        // Add a title box to the chart using 15pt Times Bold Italic font. The title is in CDML and
        // includes embedded images for highlight. The text is white (ffffff) on a dark red (880000)
        // background, with soft lighting effect from the right side.
        c.addTitle(
            " " + nameGraphic +
            " ", "Arial Bold", 15, 0x000000
            ).setBackground(0xffffff);
//        c.addTitle(
//                "<*block,valign=absmiddle*><*img=star.png*><*img=star.png*>" + nameGraphic +
//                "<*img=star.png*><*img=star.png*><*/*>", "Times New Roman", 15, 0x000000
//                ).setBackground(0xffffff, -1, Chart.softLighting(Chart.Right));

        // Add a title to the y axis
        c.yAxis().setTitle(textY);

        // Set the labels on the x axis
        c.xAxis().setLabels(dataX);

        // Add a title to the x axis using CMDL
        c.xAxis().setTitle(textX);

        // Set the axes width to 2 pixels
        c.xAxis().setWidth(2);
        c.yAxis().setWidth(2);

        // Add a spline layer to the chart
        SplineLayer layer = c.addSplineLayer();

        // Set the default line width to 2 pixels
        layer.setLineWidth(2);

        // Add a data set to the spline layer, using blue (0000c0) as the line color, with yellow
        // (ffff00) circle symbols.
        layer.addDataSet(dataY, 0x0000c0, "Fitness").setDataSymbol(Chart.CircleSymbol, 10,0xf040f0);

        // Output the chart
        viewer.setChart(c);

        
    }

    //Allow this module to run as standalone program for easy testing
//    public static void main(String[] args)
//    {
//    	 // The data for the chart
//        double[] data0 = {32, 39, 23, 28, 41, 38, 26, 35, 29};
//        double[] data1 = {50, 55, 47, 34, 47, 53, 38, 40, 51};
//
//        // The labels for the chart
//        String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
//    	
//    	//Instantiate an instance of this demo module
//        DemoModule demo = new LineChart(labels, data0, "Iteração", "Fitness", "Fitness para iterações");
//
//        //Create and set up the main window
//        JFrame frame = new JFrame(demo.toString());
//        frame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {System.exit(0);} });
//        frame.getContentPane().setBackground(Color.black);
//
//        // Create the chart and put them in the content pane
//        ChartViewer viewer = new ChartViewer();
//        demo.createChart(viewer, 0);
//        frame.getContentPane().add(viewer);
//
//        // Display the window
//        frame.pack();
//        frame.setVisible(true);
//    }
}