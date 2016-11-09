package com.example.alexandraboukhvalova.cmsc434doodle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.app.Dialog;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends Activity implements OnClickListener {

    private DoodleView doodleView;
    private ImageButton _currentTool, _drawBtn, _deleteBtn, _opacityBtn, _greyscaleButton;
    private float _smallBrush, _mediumBrush, _largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doodleView = (DoodleView)findViewById(R.id.drawing) ;
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        _currentTool = (ImageButton)paintLayout.getChildAt(0);
        _currentTool.setImageResource(R.drawable.paint_pressed);

        _smallBrush = getResources().getInteger(R.integer.small_size);
        _mediumBrush = getResources().getInteger(R.integer.medium_size);
        _largeBrush = getResources().getInteger(R.integer.large_size);

        _drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        _drawBtn.setOnClickListener(this);

        doodleView.setBrushSize(_smallBrush);

        _deleteBtn = (ImageButton)findViewById(R.id.delete_btn);
        _deleteBtn.setOnClickListener(this);

        _opacityBtn = (ImageButton)findViewById(R.id.opacity_btn);
        _opacityBtn.setOnClickListener(this);

        _greyscaleButton = (ImageButton)findViewById(R.id.greyscale_btn);
        _greyscaleButton.setOnClickListener(this);


    }

    public void paintClicked(View view){
        doodleView.setBrushSize(doodleView.getLastBrushSize());
        //use chosen color
        if(view!= _currentTool){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();

            doodleView.setColor(color);

            imgView.setImageResource(R.drawable.paint_pressed);
            _currentTool.setImageResource(R.drawable.paint);
            _currentTool =(ImageButton)view;
        }
    }

    @Override
    public void onClick(View view) {

        //respond to clicks
        if(view.getId()==R.id.draw_btn){

            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Opacity level:");
            seekDialog.setContentView(R.layout.brushchooser_seek);

            final TextView seekText = (TextView)seekDialog.findViewById(R.id.brushchooser_txt);
            final SeekBar seekBrushsize = (SeekBar)seekDialog.findViewById(R.id.brushchooser_seek);
            seekBrushsize.setMax(30);

            int currLevel = (int)doodleView.getLastBrushSize();
            seekText.setText(currLevel+"dp");
            seekBrushsize.setProgress(currLevel);

            seekBrushsize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekText.setText(Integer.toString(progress)+"dp");
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            Button brushsizeBtn = (Button)seekDialog.findViewById(R.id.brushchooser_ok);
            brushsizeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    doodleView.setBrushSize(seekBrushsize.getProgress());
                    doodleView.setLastBrushSize(seekBrushsize.getProgress());
                    //doodleView.setPaintAlpha(seekOpacity.getProgress());
                    seekDialog.dismiss();
                }
            });

            seekDialog.show();

        } else if(view.getId()==R.id.delete_btn) {
            //switch to erase - choose size
            final Dialog deleteDialog = new Dialog(this);
            deleteDialog.setTitle("Eraser:");
            deleteDialog.setContentView(R.layout.confirmation);

            Button confirmationOkBtn = (Button)deleteDialog.findViewById(R.id.confirmation_ok);
            confirmationOkBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    doodleView.clearAll();
                    deleteDialog.dismiss();
                }
            });

            Button confirmationCancelBtn = (Button)deleteDialog.findViewById(R.id.confirmation_cancel);
            confirmationCancelBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                }
            });


            deleteDialog.show();
        } else if (view.getId()==R.id.opacity_btn) {
            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Opacity level:");
            seekDialog.setContentView(R.layout.opacity_chooser);

            final TextView seekText = (TextView)seekDialog.findViewById(R.id.opacity_txt);
            final SeekBar seekOpacity = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);
            seekOpacity.setMax(100);

            int currLevel = doodleView.getPaintAlpha();
            seekText.setText(currLevel+"%");
            seekOpacity.setProgress(currLevel);

            seekOpacity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekText.setText(Integer.toString(progress)+"%");
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            Button opacityBtn = (Button)seekDialog.findViewById(R.id.opacity_ok);
            opacityBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    doodleView.setPaintAlpha(seekOpacity.getProgress());
                    seekDialog.dismiss();
                }
            });

            seekDialog.show();

        } else if (view.getId()==R.id.greyscale_btn) {

            final Dialog deleteDialog = new Dialog(this);
            deleteDialog.setTitle("Greyscale:");
            deleteDialog.setContentView(R.layout.greyscale_confirm);

            Button confirmationOkBtn = (Button)deleteDialog.findViewById(R.id.confirmation_ok);
            confirmationOkBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    doodleView.toGrayscale();
                    deleteDialog.dismiss();
                }
            });

            Button confirmationCancelBtn = (Button)deleteDialog.findViewById(R.id.confirmation_cancel);
            confirmationCancelBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                }
            });


            deleteDialog.show();
        }
    }
}
