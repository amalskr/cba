package com.ceylonapz.cbaapp.model;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by amalskr on 2017-05-19.
 */
public class Drawer {

    private Path drawingPath;
    private Paint drawingPaint;


    public Path getDrawingPath() {
        return drawingPath;
    }

    public void setDrawingPath(Path drawingPath) {
        this.drawingPath = drawingPath;
    }

    public Paint getDrawingPaint() {
        return drawingPaint;
    }

    public void setDrawingPaint(Paint drawingPaint) {
        this.drawingPaint = drawingPaint;
    }
}
