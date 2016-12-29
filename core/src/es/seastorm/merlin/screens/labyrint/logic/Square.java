package es.seastorm.merlin.screens.labyrint.logic;


public class Square implements java.io.Serializable{
    boolean limitUp;
    boolean limitDown;
    boolean limitLeft;
    boolean limitRight;

    public boolean isLimitUp() {
        return limitUp;
    }
    public void setLimitUp(boolean limitUp) {
        this.limitUp = limitUp;
    }
    public boolean isLimitDown() {
        return limitDown;
    }
    public void setLimitDown(boolean limitDown) {
        this.limitDown = limitDown;
    }
    public boolean isLimitLeft() {
        return limitLeft;
    }
    public void setLimitLeft(boolean limitLeft) {
        this.limitLeft = limitLeft;
    }
    public boolean isLimitRight() {
        return limitRight;
    }
    public void setLimitRight(boolean limitRight) {
        this.limitRight = limitRight;
    }

    public void toggleLimitUp() {
        this.limitUp = !limitUp;
    }
    public void toggleLimitDown() {
        this.limitDown = !limitDown;
    }
    public void toggleLimitRight() {
        this.limitRight = !limitRight;
    }
    public void toggleLimitLeft() {
        this.limitLeft = !limitLeft;
    }

    public Square(boolean limitUp, boolean limitRight, boolean limitLeft, boolean limitDown) {
        super();
        this.limitUp = limitUp;
        this.limitDown = limitDown;
        this.limitLeft = limitLeft;
        this.limitRight = limitRight;
    }

    public String toString(){
        String txt = isLimitDown()?"_":" ";
        txt += isLimitRight()?"|":" ";
        return txt;
    }
    public String toJsonString(){
        StringBuffer txt = new StringBuffer("{");
        txt.append("limitUp:");
        txt.append(limitUp);
        txt.append(",limitDown:");
        txt.append(limitDown);
        txt.append(",limitLeft:");
        txt.append(limitLeft);
        txt.append(",limitRight:");
        txt.append(limitRight);
        txt.append("}");
        return txt.toString();
    }

}
