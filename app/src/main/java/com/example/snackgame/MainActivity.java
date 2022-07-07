package com.example.snackgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  implements SurfaceHolder.Callback {
    private final List<Snakepoint>snakePointList=new ArrayList<>();
    private TextView scorcetv;
    private SurfaceView safaceView;
    private String movingPosition="right";
    private SurfaceHolder surfaceHolder;
    private int score = 0;
    private static final int pointSize=10;
    private static final  int defaultTalePoints=3;
    private static final int snakeColor= Color.YELLOW;
    private static final int snakeMovingSpeed=600;
    private int positionX,positionY;
    private Timer timer;
    private Canvas canvas= null;
    private Paint pointColor=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scorcetv = findViewById(R.id.scoretv);
        safaceView =findViewById(R.id.safaceView);



        final AppCompatImageButton top_arrow=findViewById(R.id.top_arrow);
        final AppCompatImageButton left_arrow=findViewById(R.id.left_arrow);
        final AppCompatImageButton button_arrow=findViewById(R.id.buttom_arrow);
        final AppCompatImageButton right_arrow=findViewById(R.id.right_arrow);


        safaceView.getHolder().addCallback(this);



        top_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("bottom")){
                    movingPosition="top";

                }

            }
        });

        left_arrow. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("right")) {
                    movingPosition = "left";
                }

            }
        });
        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("left")) {
                    movingPosition = "right";
                }



            }
        });

        button_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("top")) {
                    movingPosition = "bottom";
                }

            }
        });

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder=surfaceHolder;

        init();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
    }
    private void init() {
        snakePointList.clear();
        scorcetv.setText("0");
        score = 0;

        movingPosition = "right";
        int startPositionX = (pointSize) * defaultTalePoints;
        for (int i = 0; i < defaultTalePoints; i++) {
            Snakepoint snakePoint =new Snakepoint (startPositionX,pointSize);
            snakePointList.add(snakePoint);

            startPositionX=startPositionX-(pointSize*2);






        }
        addpoint();
        moveSnake();
    }
        private void addpoint( ){
        int surfaceWidth = safaceView.getWidth()-(pointSize*2);
        int surfaceHeight = safaceView.getHeight()-(pointSize*2);

        int randomXPosition = new Random().nextInt( surfaceWidth/pointSize);
        int randomYPosition= new Random().nextInt( surfaceHeight/pointSize);
        if ((randomXPosition %2)!=0){
            randomXPosition=randomXPosition+1;
        }

            if ((randomYPosition %2)!=0){
                randomYPosition=randomYPosition+1;
            }

            positionX= (pointSize * randomXPosition) +pointSize;
            positionY= (pointSize * randomYPosition) +pointSize;
        }
            private void moveSnake(){


        timer= new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                int headpositionX = snakePointList.get(0).getPositionX();
                int headpositionY = snakePointList.get(0).getPositionY();

                if (headpositionX == positionX && headpositionY == positionY) {

                    growSnake();
                    addpoint();

                }
                switch (movingPosition) {
                    case "right":


                        snakePointList.get(0).setPositionX(headpositionX + (pointSize * 2));
                        snakePointList.get(0).setPositionY(headpositionY);
                        break;

                    case "left":
                        snakePointList.get(0).setPositionX(headpositionX - (pointSize * 2));
                        snakePointList.get(0).setPositionY(headpositionY);
                        break;

                    case "top":
                        snakePointList.get(0).setPositionX(headpositionX);
                        snakePointList.get(0).setPositionY(headpositionY - (pointSize * 2));
                        break;


                    case "bottom":
                        snakePointList.get(0).setPositionX(headpositionX);
                        snakePointList.get(0).setPositionY(headpositionY + (pointSize * 2));
                        break;


                }


                if (checkGameOver(headpositionX, headpositionY)) {
                    timer.purge();
                    timer.cancel();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setMessage("Your Score=" + score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Start again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            init();
                        }

                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();


                        }

                    });
                }

                else{

                    canvas= surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                    canvas.drawCircle(snakePointList.get(0).getPositionX(),snakePointList.get(0).getPositionY(),pointSize,createPointColor() );



                    canvas.drawCircle(positionX,positionY,pointSize,createPointColor());

                    for (int i=1;i<snakePointList.size();i++){

                        int getTemPositionX= snakePointList.get(i).getPositionX();
                        int getTemPositionY= snakePointList.get(i).getPositionY();
                        snakePointList.get(i).setPositionX(headpositionX);
                        snakePointList.get(i).setPositionY(headpositionY);
                        canvas.drawCircle(snakePointList.get(i).getPositionX(),snakePointList.get(i).getPositionY(),pointSize,createPointColor());

                        headpositionX=getTemPositionX;
                        headpositionY=getTemPositionY;


                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);




                }

            }







            } ,1000-snakeMovingSpeed,1000-snakeMovingSpeed);
            }


            private  void growSnake(){


        Snakepoint snakepoint=new Snakepoint(0,0);
        snakePointList.add(snakepoint);
        score++;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scorcetv.setText(String.valueOf(score));

            }
        });




            }

        private boolean checkGameOver(int headPositionX, int headPositionY){
            boolean gameOver =false;

          if( snakePointList.get(0).getPositionX()<0 ||
          snakePointList.get(0).getPositionY()< 0||
                  snakePointList.get(0).getPositionX()>= safaceView.getWidth()||
                  snakePointList.get(0).getPositionY()>= safaceView.getHeight())
          {

              gameOver=true;



          }
          else{
              for(int i=1;i<snakePointList.size();i++){
                  if(headPositionX==snakePointList.get(i).getPositionX()&&
                          headPositionY==snakePointList.get(i).getPositionY()){
                      gameOver=true;
                      break;
                  }
              }
          }



            return gameOver;

                }

                private Paint createPointColor(){
        if (pointColor==null) {

            pointColor = new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true);

        }
            return pointColor;

                }



            }



