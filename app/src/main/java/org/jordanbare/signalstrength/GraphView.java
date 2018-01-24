package org.jordanbare.signalstrength;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_KEY_CDMA_DBM;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_KEY_CDMA_LEVEL;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_KEY_GSM_DBM;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_KEY_GSM_LEVEL;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_KEY_LTE_DBM;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_KEY_LTE_LEVEL;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_KEY_WCDMA_DBM;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_KEY_WCDMA_LEVEL;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_VALUE_CDMA;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_VALUE_GSM;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_VALUE_LTE;
import static org.jordanbare.signalstrength.CellInfoDataImpl.CELL_INFO_VALUE_WCDMA;

/**
 * Created by jordanbare on 1/8/18.
 */

public class GraphView extends View {
    private final int width;
    private final int height;
    private final int oldRange = 80;
    private final int oldMin = -130;
    private final float[] labelHeight;
    private final float[] lineYValues;
    private final Paint[] fillPaints;
    private final Paint linePaint;
    private final Paint labelPaint;
    private final String[] signalTypeLabels = {"CDMA", "GSM", "LTE", "WCDMA"};
    private final String dBmLabel = "dBm";
    private final float[] dBmLabelCoords;
    private final Paint dBmLabelPaint;
    private float[] topY;
    private float[] leftX;
    private float[] rightX;
    private int[] dBmValues;
    private float[] labelXCoords;
    private int rectangleNumber = 0;
    private int[] fillPaintAssignments;
    private int[] signalTypeLabelAssignments;
    private String[] dBmValueStrings;

    public GraphView(Context context, int width, int height) {
        super(context);
        this.fillPaints = initializeFillPaints();
        this.width = width;
        this.height = height;
        this.linePaint = initializeLinePaint();
        this.labelPaint = initializeLabelPaint();
        this.dBmLabelPaint = initializedBmLabelPaint();
        this.lineYValues = initializeLineYValues();
        this.labelHeight = initializeLabelHeights();
        this.dBmLabelCoords = initializedBmLabelCoords();
    }

    private Paint initializedBmLabelPaint() {
        Paint dBmLabelPaint = new Paint();
        dBmLabelPaint.setColor(Color.BLACK);
        dBmLabelPaint.setTextSize(35);
        return dBmLabelPaint;
    }

    private float[] initializedBmLabelCoords() {
        float[] dBmLabelCoords = new float[2];
        dBmLabelCoords[0] = width / 8;
        dBmLabelCoords[1] = height / 8;
        return dBmLabelCoords;
    }

    private Paint[] initializeFillPaints(){
        Paint[] paints = new Paint[5];

        for(int i = 0; i < 5; i++){
            paints[i] = new Paint();
            paints[i].setStyle(Paint.Style.FILL);
        }
        paints[0].setColor(Color.BLACK);
        paints[1].setColor(Color.RED);
        paints[2].setColor(Color.YELLOW);
        paints[3].setColor(Color.GREEN);
        paints[4].setColor(Color.BLUE);
        return paints;
    }
    private Paint initializeLinePaint(){
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);
        return linePaint;
    }

    private Paint initializeLabelPaint(){
        Paint labelPaint = new Paint();
        labelPaint.setColor(Color.BLACK);
        labelPaint.setTextSize(28);
        return labelPaint;
    }

    private float[] initializeLineYValues(){
        float[] lineYValues = new float[4];
        for(int i = 0; i < 4; i++){
            lineYValues[i] = (float) height / 4 * (i + 1);
        }
        return lineYValues;
    }

    private float[] initializeLabelHeights(){
        float[] labelHeight = new float[2];
        labelHeight[0] = height - height / 7;
        labelHeight[1] = height - height / 14;
        return labelHeight;
    }

    protected float[] calculateTopY(){
        float[] topY = new float[rectangleNumber];
        for(int i = 0 ; i < rectangleNumber; i++){
            topY[i] = height - (((dBmValues[i] - oldMin) * height) / oldRange);
        }
        return topY;
    }

    private void calculateX(){
        leftX = new float[rectangleNumber];
        rightX = new float[rectangleNumber];
        labelXCoords = new float[rectangleNumber];
        float offsetPerBar = (width * 5)/(6 * rectangleNumber);
        float halfTotalOffsetSpace = width / 12;
        float offsetBetweenBars = offsetPerBar / 6;

        float xOffset = halfTotalOffsetSpace;
        for(int i = 0; i < rectangleNumber; i++){
            leftX[i] = xOffset;
            labelXCoords[i] = xOffset;
            rightX[i] = xOffset + offsetPerBar - offsetBetweenBars;
            xOffset = rightX[i] + offsetBetweenBars;
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        drawLines(canvas);
        drawRectangles(canvas);
        try {
            drawLabels(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        invalidate();
    }

    private void drawLines(Canvas canvas) {
        for(int i = 0; i < lineYValues.length; i++){
            canvas.drawLine(0, lineYValues[i], width, lineYValues[i], linePaint);
        }
    }
    private void drawRectangles(Canvas canvas){
        for(int i = 0; i < rectangleNumber; i++){
            canvas.drawRect(leftX[i], topY[i], rightX[i], height, fillPaints[fillPaintAssignments[i]]);
        }
    }
    private void drawLabels(Canvas canvas) throws Exception {
        for(int i = 0; i < rectangleNumber; i++){
            canvas.drawText(signalTypeLabels[signalTypeLabelAssignments[i]], labelXCoords[i], labelHeight[0], labelPaint);
            canvas.drawText(dBmValueStrings[i], labelXCoords[i], labelHeight[1], labelPaint);
        }
        canvas.drawText(dBmLabel, dBmLabelCoords[0], dBmLabelCoords[1], dBmLabelPaint);
    }

    public void initializeDataToDraw(ArrayList<Map> cellInfoMapList){

        rectangleNumber = cellInfoMapList.size();
        fillPaintAssignments = new int[rectangleNumber];
        dBmValues = new int[rectangleNumber];
        signalTypeLabelAssignments = new int[rectangleNumber];
        dBmValueStrings = new String[rectangleNumber];

        int currentRectangleIndex = 0;
        int dBmValue = -130;
        int strengthValue = 0;

        for(Map<Object, Serializable> cellInfoMap: cellInfoMapList){

            if(cellInfoMap.containsKey(CELL_INFO_VALUE_CDMA)){
                strengthValue = Integer.valueOf(cellInfoMap.get(CELL_INFO_KEY_CDMA_LEVEL).toString());
                fillPaintAssignments[currentRectangleIndex] = strengthValue;

                dBmValue = Integer.valueOf(cellInfoMap.get(CELL_INFO_KEY_CDMA_DBM).toString());
                dBmValues[currentRectangleIndex] = dBmValue;
                dBmValueStrings[currentRectangleIndex] = "  " + dBmValue;
                signalTypeLabelAssignments[currentRectangleIndex] = 1;
                ++currentRectangleIndex;
            }
            else if(cellInfoMap.containsKey(CELL_INFO_VALUE_GSM)){
                strengthValue = Integer.valueOf(cellInfoMap.get(CELL_INFO_KEY_GSM_LEVEL).toString());
                fillPaintAssignments[currentRectangleIndex] = strengthValue;

                dBmValue = Integer.valueOf(cellInfoMap.get(CELL_INFO_KEY_GSM_DBM).toString());
                dBmValues[currentRectangleIndex] = dBmValue;
                dBmValueStrings[currentRectangleIndex] = "  " + dBmValue;
                signalTypeLabelAssignments[currentRectangleIndex] = 2;
                ++currentRectangleIndex;
            }
            else if(cellInfoMap.containsKey(CELL_INFO_VALUE_LTE)){
                strengthValue = Integer.valueOf(cellInfoMap.get(CELL_INFO_KEY_LTE_LEVEL).toString());
                fillPaintAssignments[currentRectangleIndex] = strengthValue;

                dBmValue = Integer.valueOf(cellInfoMap.get(CELL_INFO_KEY_LTE_DBM).toString());
                dBmValues[currentRectangleIndex] = dBmValue;
                dBmValueStrings[currentRectangleIndex] = "  " + dBmValue;
                signalTypeLabelAssignments[currentRectangleIndex] = 3;
                ++currentRectangleIndex;
            }
            else if(cellInfoMap.containsKey(CELL_INFO_VALUE_WCDMA)){
                strengthValue = Integer.valueOf(cellInfoMap.get(CELL_INFO_KEY_WCDMA_LEVEL).toString());
                fillPaintAssignments[currentRectangleIndex] = strengthValue;

                dBmValue = Integer.valueOf(cellInfoMap.get(CELL_INFO_KEY_WCDMA_DBM).toString());
                dBmValues[currentRectangleIndex] = dBmValue;
                dBmValueStrings[currentRectangleIndex] = "  " + dBmValue;
                signalTypeLabelAssignments[currentRectangleIndex] = 4;
                ++currentRectangleIndex;
            }
        }
        topY = calculateTopY();
        calculateX();
    }

    //for testing, although mediocre

    public void setdBmValues(int[] valueArray){
        dBmValues  = valueArray.clone();
    }

}
