/// The Card class is used to create the cards utilizing within the UnoGame.

//@Author Ethan
//@Date 2023/10/22
//@Version v1.0

import javax.swing.*;
import java.io.Serializable;

public class Card implements Serializable {

    //card value
    private Value value;

    //card color
    private Color color;
    private Color lightColor;
    private Color darkColor;
    private Value lightValue;
    private Value darkValue;
    private ImageIcon imageLight;

    private ImageIcon imageDark;

    //card constructor
    public Card(Value lightValue, Color lightColor, Value darkValue, Color darkColor) {
        this.lightColor = lightColor;
        this.lightValue = lightValue;
        this.darkColor = darkColor;
        this.darkValue = darkValue;
        this.imageLight = new ImageIcon("card images/" + lightColor.name() + " " + lightValue.name() + ".png");
        this.imageDark = new ImageIcon("card images/" + darkColor.name() + " " + darkValue.name() + ".png");
    }



    //get card value
    public Value getValue(Side theSide) {
        if (theSide == Side.LIGHT) {
            return this.lightValue;
        }
        else {
            return this.darkValue;
        }
    }

    public Card cloneCard() {
        return new Card(this.lightValue, this.lightColor, this.darkValue, this.darkColor);
    }

    //get card colour
    public Color getColor(Side theSide) {
        if (theSide == Side.LIGHT) {
            return this.lightColor;
        }
        else {
            return this.darkColor;
        }
    }

    public ImageIcon getImage(Side theSide){
        if (theSide == Side.LIGHT) {
            return imageLight;
        }
        else {
            return imageDark;
        }
    }

    //convert card to string
    public String cardString(Side theSide) {
        if (theSide == Side.LIGHT) {
            return lightColor.name() + " " + lightValue.name() + " \n";
        }
        else {
            return darkColor.name() + " " + darkValue.name() + " \n";
        }
    }
}