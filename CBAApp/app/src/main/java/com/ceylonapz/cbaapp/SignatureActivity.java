package com.ceylonapz.cbaapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.ceylonapz.cbaapp.model.Drawer;
import com.ceylonapz.cbaapp.model.User;
import com.ceylonapz.cbaapp.utils.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SignatureActivity extends AppCompatActivity {

    private RelativeLayout drawingLayout;
    private Paint paint;
    private View view;
    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;
    private String name;
    private double amount;
    private int code;
    private Uri picUri;
    private File signatureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get data from main
        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        amount = intent.getDoubleExtra("AMOUNT", 0d);
        code = intent.getIntExtra("CODE", 0);

        //init
        drawingLayout = (RelativeLayout) findViewById(R.id.drawingLayout);
        TextView message_tv = (TextView) findViewById(R.id.messageTv);

        //set amount in title
        getSupportActionBar().setTitle("Amount : " + amount);

        //set bottom message from name and code.
        assert message_tv != null;
        message_tv.setText(name + ", agree to pay the above total according to my card issuer agreement. " +
                "Card ending 1234, Authorisation code " + code);

        view = new SketchSheetView(SignatureActivity.this);
        paint = new Paint();
        path = new Path();

        drawingLayout.addView(view, new LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#000000"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);

    }


    class SketchSheetView extends View {

        public SketchSheetView(Context context) {
            super(context);
            this.setBackgroundColor(Color.WHITE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
        }

        private ArrayList<Drawer> drawingArrayList = new ArrayList<Drawer>();

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            Drawer drawer = new Drawer();

            canvas.drawPath(path, paint);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                path.moveTo(event.getX(), event.getY());
                path.lineTo(event.getX(), event.getY());

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                path.lineTo(event.getX(), event.getY());
                drawer.setDrawingPath(path);
                drawer.setDrawingPaint(paint);
                drawingArrayList.add(drawer);
            }

            invalidate();
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (drawingArrayList.size() > 0) {
                canvas.drawPath(
                        drawingArrayList.get(drawingArrayList.size() - 1).getDrawingPath(),
                        drawingArrayList.get(drawingArrayList.size() - 1).getDrawingPaint());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signature_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.sendItem:

                saveSignatureAsImage();

                //save data in USER model
                User user = new User();
                user.setSignatureImagePath(String.valueOf(picUri));
                user.setName(name);
                user.setAmount(amount);
                user.setCode(code);

                //open send screen
                Intent signatureIntent = new Intent(getApplicationContext(), SendActivity.class);

                //send object using GSON
                Gson gson = new Gson();
                String userStr = gson.toJson(user);
                signatureIntent.putExtra("USER", userStr);

                startActivity(signatureIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //signature image save in temp location
    private void saveSignatureAsImage() {

        File file = getExternalFilesDir(null);
        try {
            signatureFile = new File(file, "signature.png");

            OutputStream os = new FileOutputStream(signatureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();

            picUri = Uri.parse("file://" + signatureFile);

        } catch (Exception e) {
            Util.showToast(this, "Sorry! Can't save your signature as an image.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        path.reset();
    }
}