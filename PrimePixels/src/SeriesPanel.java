import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

class SeriesPanel extends JPanel {
    public enum SeriesType {
        PRIME,
        FIBONACCI,
        ULAM,
        BAUM,
        LUCKY
    }
    public enum MovementType {
        QUARTER_SPIRAL,
        CLOCKWISE_SPIRAL,
        COUNTERCLOCKWISE_SPIRAL
    }
    private int square, side, x = 0, y = 0, layer = 0, innie = 0, outie;
    private boolean print = true, dir = true;
    private ArrayList<Integer> storage;
    private BufferedImage buff;
    private SeriesType seriesType;
    private MovementType movementType;

    SeriesPanel(int side, SeriesType seriesType, MovementType movementType) {
        this.side = side;
        this.seriesType = seriesType;
        this.movementType = movementType;

        square = (int)(Math.pow(side,2));
        buff = new BufferedImage(side, side, BufferedImage.TYPE_BYTE_BINARY);
        storage = new ArrayList<>();
        outie = side-1;

        if(seriesType == SeriesType.FIBONACCI){
            storage.add(0);
            storage.add(1);
        }
        if(seriesType == SeriesType.LUCKY){
            for(int z = 1; z<=square; z++){
                if(z%2==1){
                    storage.add(z);
                }
            }
            sieveLuckyListExclusive();
        }

        setSize(200, 200);
    }

    public void paint(Graphics g) {
        //Create image once
        if(print) {
            System.out.println("Drawing Image...");
            Graphics bg = buff.getGraphics();
            bg.setColor(Color.WHITE);
            bg.fillRect(0,0,side,side);
            bg.setColor(Color.BLACK);
            //This is #bulk
            for(int z = 1; z <= square; z++) {
                //For my viewing pleasure
                if(z == square/4) {
                    System.out.println("25% Complete");
                }
                else if(z == square/2) {
                    System.out.println("50% Complete");
                }
                else if(z == (square/4)*3) {
                    System.out.println("75% Complete");
                }

                //Checks and colors pixel if prime
                if(isCheckType(z)) {
                    bg.drawLine(x,y,x,y);
                }

                //Moves cursor
                moveThisWay(z);
            }
            System.out.println("Image Drawn!");
            saveImage();
            print = false;
        }
        //Display image out in a panel
        g.setColor(new Color(61, 208, 41));
        g.setFont(new Font("Arial",Font.BOLD,20));
        g.drawString("Done Computing!",getWidth()/10, (getHeight()/2)-10);
    }

    //--Series Types--//
    private boolean isCheckType(int index) {
        switch (seriesType){
            case PRIME:{
                return isPrime(index);
            }
            case FIBONACCI:{
                return isFibonacci(index);
            }
            case ULAM:{
                return isUlam(index);
            }
            case BAUM:{
                return isBaumSweet(index);
            }
            case LUCKY:{
                return isLucky(index);
            }
            default:{
                System.out.println("Series Type Error");
                System.exit(0);
                return false;
            }
        }
    }

    private boolean isPrime(int index) {
        //One's not prime
        if(index == 1) {
            return false;
        }

        double chk = Math.sqrt(index);
        //Check if divisible by any prime
        for(int z : storage) {
            //This will exponentially get longer the more numbers added
            if(index % z == 0) {
                return false;
            }
            //Reduces checking size greatly
            if(z > chk) {
                break;
            }
        }

        //If number isn't less than the list created, then add to list and return true
        if(index<=side) {
            storage.add(index);
        }
        return true;
    }
    private boolean isFibonacci(int index) {
        if(index == storage.get(storage.size()-1)+storage.get(storage.size()-2)){
            storage.add(index);
            return true;
        }
        return false;
    }
    private boolean isUlam(int index) {
        if(index==1||index==2||index==3||index==47||index==69){
            storage.add(index);
            return true;
        }
        //Apparently a good check
        if(Math.cos(2.5714474995*index)>=0){
            return false;
        }

        int distinct = 0;
        for(int z : storage){
            if(binarySearch(storage, index - z )){
                if(z*2 != index){
                    distinct++;
                }
            }
            if(distinct>2){
                return false;
            }
        }
        if(distinct==0){
            return false;
        }

        storage.add(index);
        return true;
    }
    private boolean isBaumSweet(int index){
        String search = Integer.toBinaryString(index);
        int count = 0;
        for(char z : search.toCharArray()){
            if(z == '0'){
                count++;
            }
            else{
                if(count%2==1){
                    return false;
                }
                else {
                    count=0;
                }
            }
        }
        return count%2==0;
    }
    private boolean isLucky(int index){
        return binarySearch(storage,index);
    }
    private void sieveLuckyListInclusive(){
        //--This is used if you initialised the list with even numbers--//
        //Every time this increments another lucky number has been found
        for(int index = 0; index<storage.size(); index++){
            int z = storage.get(index);
            //Removes every Zth number from the list
            for(int w = 0; w<storage.size(); w += (z==1) ? z : z-1){
                if(w!=0){
                    storage.remove(w);
                }
            }
        }
    }
    private void sieveLuckyListExclusive(){
        //--This is used if you initialised the list without even numbers--//
        //Every time this increments another lucky number has been found
        for(int index = 1; index<storage.size(); index++){
            int z = storage.get(index);
            //Removes every Zth number from the list
            for(int w = z-1; w<storage.size(); w += z-1){
                storage.remove(w);
            }
        }
    }

    //--Sequence Types--//
    private void moveThisWay(int index) {
        switch (movementType){
            case QUARTER_SPIRAL:{
                moveQuarterSpiral(index);
                break;
            }
            case CLOCKWISE_SPIRAL:{
                moveClockwiseSpiral();
                break;
            }
            case COUNTERCLOCKWISE_SPIRAL:{
                moveCounterClockwiseSpiral();
                break;
            }
            default:{
                System.out.println("Movement Type Error");
                System.exit(0);
                break;
            }
        }
    }

    private void moveQuarterSpiral(int index) {
        if(index==Math.pow(layer+1,2)) {
            if(dir) {
                x+=1;
            }
            else {
                y+=1;
            }
            layer++;
            dir = !dir;
            return;
        }
        if(dir) {
            if(x<layer) {
                x+=1;
            }
            else {
                y-=1;
            }
        }
        else {
            if(y<layer) {
                y+=1;
            }
            else {
                x-=1;
            }
        }
    }
    private void moveClockwiseSpiral(){
        if(dir){
            if(x<outie){
                x++;
            }
            else if(y<outie){
                y++;
            }
            else {
                dir = !dir;
                outie--;
            }
        }
        else {
            if(x>innie){
                x--;
            }
            else if(y>innie+1){
                y--;
            }
            else {
                dir = !dir;
                innie++;
            }
        }
    }
    private void moveCounterClockwiseSpiral(){
        if(dir){
            if(y<outie){
                y++;
            }
            else if(x<outie){
                x++;
            }
            else {
                dir = !dir;
                outie--;
            }
        }
        else {
            if(y>innie){
                y--;
            }
            else if(x>innie+1){
                x--;
            }
            else {
                dir = !dir;
                innie++;
            }
        }
    }

    //--Image saving--//
    private void saveImage() {
        try {
            System.out.println("Processing image...");
            File image = new File("SavedImages/"+ movementType.name());
            if (Files.notExists(image.toPath())) {
                image.mkdir();
            }
            image = new File("SavedImages/"+ movementType.name()+"/"+ seriesType.name());
            if (Files.notExists(image.toPath())) {
                image.mkdir();
            }
            image = new File("SavedImages/"+ movementType.name()+"/"+ seriesType.name()+"/"+side+".bmp");
            ImageIO.write(buff, "BMP", image);
            System.out.println("Image saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //--Bleh--//
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    private boolean binarySearch(ArrayList<Integer> data, int numToFind) {
        int start=0, end=data.size()-1;
        while(start <= end) {
            int check = (start + end) / 2;
            if (data.get(check) == numToFind) {
                return true;
            }
            else if (data.get(check) > numToFind) {
                end = check-1;
            }
            else{
                start = check+1;
            }
        }
        return false;
    }

}