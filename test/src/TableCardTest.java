package src;

import uno.model.unoCard;

import java.util.ArrayList;

public class TableCardTest {
    public void main(String ...arg){

    }

    public static void printCard(unoCard C){
        System.out.println(C.getColor()+"  "+C.getNumber()+"  "+C.getFunc());
    }

    public static void print_card_arraylist(ArrayList<unoCard> li){
        for (unoCard c:li
             ) {
            printCard(c);
        }
    }
}
