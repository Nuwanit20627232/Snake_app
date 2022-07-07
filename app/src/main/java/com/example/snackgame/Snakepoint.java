package com.example.snackgame;

public class Snakepoint {
    private  int positionX, positionY;

    public Snakepoint(int positionX,int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
    public int getPositionX(){
        return positionX;


    }
    public void setPositionX(int positionX){

        this.positionX=positionX;
    }
    public int getPositionY(){

        return positionY;
    }
    public  void setPositionY(int positionY){
        this.positionY=positionY;


    }
}