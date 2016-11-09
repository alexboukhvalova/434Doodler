package com.example.alexandraboukhvalova.cmsc434doodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

import java.util.ArrayList;

/**
 * Created by alexandraboukhvalova on 11/3/16.
 */

public class DoodleView extends View{

    private Paint _paintDoodle = new Paint();
    private Paint _canvasPaint;
    private Path _path = new Path();
    private Bitmap _bitmap;
    private Canvas _canvas;
    private int _paintColor = 0xFF660000;
    private float _brushSize, _lastBrushSize;
    private boolean _erase = false;
    private int _paintAlpha = 255;
    private ArrayList<Path> _paths = new ArrayList<Path>();

    public DoodleView(Context context) {
        super(context);
        init(null, 0);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        _brushSize = getResources().getInteger(R.integer.medium_size);
        _lastBrushSize = _brushSize;
        _paintDoodle.setColor(Color.BLACK);
        _paintDoodle.setAntiAlias(true);
        _paintDoodle.setStyle(Paint.Style.STROKE);
        _paintDoodle.setStrokeWidth(_brushSize);
        _canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(_bitmap, 0, 0, _canvasPaint);
        canvas.drawPath(_path, _paintDoodle);
        //reset in case greyscale was just used
        _canvasPaint.reset();

    }

    public int getPaintAlpha(){
        return Math.round((float)_paintAlpha/255*100);
    }

    public void setPaintAlpha(int newAlpha){
        _paintAlpha=Math.round((float)newAlpha/100*255);
        _paintDoodle.setColor(_paintColor);
        _paintDoodle.setAlpha(_paintAlpha);
    }

    public void clearAll(){
        _bitmap.eraseColor(Color.TRANSPARENT);
        _canvas.drawBitmap(_bitmap, 0, 0, _canvasPaint);
        invalidate();
    }

    public void setBrushSize(float newSize) {
        //update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        _brushSize=pixelAmount;
        _paintDoodle.setStrokeWidth(_brushSize);
    }

    public void setLastBrushSize(float lastSize){
        _lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return _lastBrushSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        _bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        _canvas = new Canvas(_bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();

        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _path.moveTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                _path.lineTo(touchX,touchY);
                //draw path here
                break;
            case MotionEvent.ACTION_UP:
                _canvas.drawPath(_path, _paintDoodle);
                _path.reset();
                break;
        }

        invalidate();
        return true;
    }

    public void setColor(String newColor){
        //set color
        invalidate();
        _paintColor = Color.parseColor(newColor);
        _paintDoodle.setColor(_paintColor);
    }

    public void toGrayscale()
    {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        _canvasPaint.setColorFilter(f);
        _canvas.drawBitmap(_bitmap, 0, 0, _canvasPaint);
        invalidate();
    }
}