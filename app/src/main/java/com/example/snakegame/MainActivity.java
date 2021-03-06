package com.example.snakegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.snakegame.R;
import com.example.snakegame.SnakePoints;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    //    private final List<SnakePoints> snakePoints = new ArrayList<>();
    private final List<SnakePoints> snakePointsList = new ArrayList<>();

    private SurfaceView surfaceView;
    private TextView scoreTV;

    //surface holdert to draw snake on surface's canvas
    private SurfaceHolder surfaceHolder;

    // Snake di chuyen, value se la right left top bottom
    // mac dinh se la right
    private String movingPosition = "right";

    //score
    private int score=0;
    //kich co cua qua tao
    private static final int pointSize = 28;
    // chieu dai default
    private static final int defaultTalePoints= 3;
    // mau
    private static final int snakeColor = Color.BLUE;
    // toc do
    private static final int snakeMovingSpeed = 600;
    // random point position cordinates on the surfaceview
    private int positionX,positionY;
    // thoi gian ran co the di chuyen sau mot khoang thoi gian di chuyen ( turn rate dota2)
    private Timer timer;
    //point color
    private Paint pointColor = null;
    // canvas to draw snake and show on surface view
    private Canvas canvas = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // getting surfaceview and score Text view from xml file
        surfaceView = findViewById(R.id.surfacevView);
        scoreTV = findViewById(R.id.scoreTV);

        // getting ImageButtons from xml file
        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);
        final AppCompatImageButton bottomBtn = findViewById(R.id.bottomBtn);

        // adding call back to surfaceview
        surfaceView.getHolder().addCallback(this);
        // khi ran di chuyen, neu ma dang di huong nguoc lai se khong the di chuyen theo huong da chon
        topBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!movingPosition.equals("bottom")){
                    movingPosition="top";
                }
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!movingPosition.equals("right")){
                    movingPosition="left";
                }
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!movingPosition.equals("left")){
                    movingPosition="right";
                }
            }
        });
        bottomBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!movingPosition.equals("top")){
                    movingPosition="bottom";
                }
            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        // khi b??? m???t ???????c t???o sau ???? l???y b??? m???t gi??? t??? n?? v?? g??n cho surfaceHolder
        this.surfaceHolder = surfaceHolder;
        // d??? li???u init cho solid / surfaceview
        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void init(){
        // clear point
        snakePointsList.clear();
        // clear text
        scoreTV.setText("0");
        // clear diem ve 0
        score = 0;
        //set moving ve right
        movingPosition="right";
        // vi tri mac dinh cua ran khi vao game
        int startPositionX = (pointSize)*defaultTalePoints;
        for (int i=0;i<defaultTalePoints;i++){
            //adding points to snake's tale
            SnakePoints snakePoints = new SnakePoints(startPositionX,pointSize);
            snakePointsList.add(snakePoints);
            // increasing value for next point as snake's tale
            startPositionX = startPositionX - (pointSize*2);

        }
        // th??m ??i???m ng???u nhi??n v??o m??n h??nh
        addPoint();
        // b???t ?????u di chuy???n
        moveSnake();
    }
    private void addPoint(){
        int surfaceWidth= surfaceView.getWidth() - (pointSize*2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize*2);

        int randomXPosition = new Random().nextInt(surfaceWidth/pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight/pointSize);
        if ((randomXPosition % 2) != 0){
            randomXPosition=randomXPosition+1;
        }
        if ((randomYPosition % 2) != 0){
            randomYPosition=randomYPosition+1;
        }

        positionX=(pointSize * randomXPosition) +pointSize;
        positionY=(pointSize * randomYPosition) +pointSize;

    }
    private void moveSnake() {
        timer = new Timer();

        // scheduleAtFixedRate (nhi???m v??? TimerTask, ????? tr??? l??u, th???i gian d??i) l?? ph????ng th???c c???a l???p Timer.
        // N?? ???????c s??? d???ng ????? l???p l???ch tr??nh l???p ??i l???p l???i nhi???m v??? ???? cho v???i t???c ????? th???c thi c??? ?????nh.
        // N?? s??? b???t ?????u sau ????? tr??? ???????c ch??? ?????nh.
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // l???y v??? tr?? ban ?????u
                int headPositionX = snakePointsList.get(0).getPositionX();
                int headPositionY = snakePointsList.get(0).getPositionY();

                // ki???m tra n???u r???n ??n ??i???m
                if (headPositionX == positionX && headPositionY == positionY) {

                    // t??ng ??i???m r???n sau khi ??n ??i???m
                    growSnake();

                    // th??m m???t ??i???m ng???u nhi??n kh??c tr??n m??n h??nh
                    addPoint();
                }

                // ki???m tra xem con r???n b??n n??o ??ang di chuy???n
                switch(movingPosition) {
                    case "right":

                        // di chuy???n r???n sang ph???i
                        // c??c ??i???m kh??c theo ??i???m ?????u c???a con r???n ????? di chuy???n con r???n
                        snakePointsList.get(0).setPositionX(headPositionX + (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                        break;

                    case "left":
                        // di chuy???n r???n sang tr??i
                        // c??c ??i???m kh??c theo ??i???m ?????u c???a con r???n ????? di chuy???n con r???n
                        snakePointsList.get(0).setPositionX(headPositionX - (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                        break;

                    case "top":

                        // di chuy???n r???n l??n tr??n
                        // c??c ??i???m kh??c theo ??i???m ?????u c???a con r???n ????? di chuy???n con r???n
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY - (pointSize * 2));
                        break;

                    case "bottom":
                        // move snake`s head to bottom.
                        // other points follow snake`s head point to move the snake
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY + (pointSize * 2));
                        break;
                }

                // check if game over, weather snake touch edges or snake itself
                if (checkGameOver(headPositionX, headPositionY)){
                    // stop timer, stop moving snake
                    timer.purge();
                    timer.cancel();

                    // show game over dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Your Score = "+score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Start Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // restart game/ re-init data
                            init();
                        }
                    });

                    // timer runs in background so we need to show dialog on main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }

                else {
                    // lock canvas on surfaceHolder to draw on it
                    canvas = surfaceHolder.lockCanvas();

                    // clear canvas with white color
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                    // change snake`s head position. other snake points will follow snake`s head
                    canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(), pointSize, createPointColor());

                    // draw random point circle on the surface to be eaten by the snake
                    canvas.drawCircle(positionX, positionY, pointSize, createPointColor());

                    // other points following snake`s head
                    for (int i = 1; i < snakePointsList.size(); i++) {
                        int getTempPositionX = snakePointsList.get(i).getPositionX();
                        int getTempPositionY = snakePointsList.get(i).getPositionY();


                        // move points accross the head
                        snakePointsList.get(i).setPositionX(headPositionX);
                        snakePointsList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(snakePointsList.get(i).getPositionX(), snakePointsList.get(i).getPositionY(), pointSize, createPointColor());

                        // change head position
                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                    }

                    // unlock canvas to draw on surfaceview
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

            }

        }, 1000 - snakeMovingSpeed, 1000 - snakeMovingSpeed);
    }
    private void growSnake(){
        // tao snake point moi
        SnakePoints snakePoints = new SnakePoints(0,0);
        //cong vao list cua snake (tang chieu dai)
        snakePointsList.add(snakePoints);
        // tang diem
        score++;
        // tang diem text
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTV.setText(String.valueOf(score));
            }
        });
    }
    private boolean checkGameOver(int headPositionX, int headPositionY){
        boolean gameOver = false;
        // kiem tra xem dau ran co cham ria hay khong
        if(snakePointsList.get(0).getPositionX() <0||
                snakePointsList.get(0).getPositionY() <0||
                snakePointsList.get(0).getPositionX() >= surfaceView.getWidth()||
                snakePointsList.get(0).getPositionY() >= surfaceView.getHeight())
        {
            gameOver=true;
        }
        else{
            // kiem tra xem dau ran co cham vao than ran hay khong
            for(int i=1;i<snakePointsList.size();i++){
                if(headPositionX == snakePointsList.get(i).getPositionX()&&
                        headPositionY == snakePointsList.get(i).getPositionY()){
                    gameOver=true;
                    break;
                }
            }
        }
        return gameOver;
    }
    private Paint createPointColor(){
        if(pointColor == null){
            pointColor= new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true);
            return pointColor;
        }
        return pointColor;
    }
}