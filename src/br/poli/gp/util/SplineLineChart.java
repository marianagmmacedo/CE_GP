package br.poli.gp.util;

import ChartDirector.CFUtil;
import ChartDirector.Chart;
import ChartDirector.ChartViewer;
import ChartDirector.SplineLayer;
import ChartDirector.XYChart;

public class SplineLineChart implements DemoModule
{

	public String[] dataX;
    public double[] dataY0;
    public double[] dataY1;
    public String textX;
    public String textY;
    public String nameGraphic;

    public SplineLineChart(String[] x, double[] y0, double[] y1, String textX_, String textY_, String nameGraphic_) {

        dataX = x.clone();
        dataY0 = y0.clone();
        dataY1 = y1.clone();
        textX = textX_;
        textY = textY_;
        nameGraphic = nameGraphic_;

    }
   

    //Number of charts produced in this demo
    public int getNoOfCharts() { return 1; }

    //Main code for creating charts
    public void createChart(ChartViewer viewer, int chartIndex){

//    	//Create a XYChart object of size 600 x 300 pixels, with a pale red (ffdddd) background, black
//    	//border, 1 pixel 3D border effect and rounded corners.
//    	XYChart c = new XYChart(1220, 400, 0xffffff, 0x000000, 1);
//    	c.setRoundedFrame();
//    	
//    	// Set the plotarea at (55, 58) and of size 520 x 195 pixels, with white (ffffff)
//        // background. Set horizontal and vertical grid lines to grey (cccccc).
//        c.setPlotArea(58, 58, 1120, 280, 0xffffff, -1, -1, 0x000000, 0x000000);
//
//        // Add a legend box at (55, 32) (top of the chart) with horizontal layout. Use 9pt Arial
//        // Bold font. Set the background and border color to Transparent.
//        c.addLegend(55, 32, false, "Arial", 10).setBackground(Chart.Transparent);
//        
//    	//Add a title to the y axis
//    	c.yAxis().setTitle("titulo1");
//
//    	//Set the labels on the x axis
//    	c.xAxis().setLabels(dataX);
//
//    	//Add a title to the x axis using CMDL
//    	c.xAxis().setTitle("titulo0");
//
//    	//Set the axes width to 2 pixels
//    	c.xAxis().setWidth(2);
//    	c.yAxis().setWidth(2);
//

    	
        // Create a XYChart object of size 600 x 300 pixels, with a pale red (ffdddd) background,
        // black border, 1 pixel 3D border effect and rounded corners.
        XYChart c = new XYChart(1220, 400, 0xffffff, 0x000000, 1);
        c.setRoundedFrame();

        // Set the plotarea at (55, 58) and of size 520 x 195 pixels, with white (ffffff)
        // background. Set horizontal and vertical grid lines to grey (cccccc).
        c.setPlotArea(58, 58, 1120, 280, 0xffffff, -1, -1, 0xffffff, 0xffffff);

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
        c.yAxis().setTitle(textY, "Arial Bold", 12);
//        .setTitle("Revenue in USD millions", "Arial Bold Italic", 10);
        // Set the labels on the x axis
        c.xAxis().setLabels(dataX);

        // Add a title to the x axis using CMDL
        c.xAxis().setTitle(textX, "Arial Bold", 12);

        // Set the axes width to 2 pixels
        c.xAxis().setWidth(2);
        c.yAxis().setWidth(2);
        
    	//Add a spline layer to the chart
    	SplineLayer layer = c.addSplineLayer();

    	//Set the default line width to 2 pixels
    	layer.setLineWidth(2);
    	//Add a data set to the spline layer, using blue (0000c0) as the line color, with yellow (ffff00)
    	//circle symbols.
    	layer.addDataSet(dataY1, 0x0000c0, "Target Group").setDataSymbol(Chart.TriangleSymbol, 10,0xffff00);
    	//Add a data set to the spline layer, using brown (982810) as the line color, with pink (f040f0)
    	//diamond symbols.
    	layer.addDataSet(dataY0, 0x982810, "Calculated Group").setDataSymbol(Chart.CircleSymbol, 11, 0xf040f0);
        
    	// Output the chart
        viewer.setChart(c);
    }


}

