package src;

import uno.model.TableCard;
import uno.model.unoCard;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String ...args){
        TableCard tableCard=new TableCard();
        List<unoCard> li=tableCard.getCard(108);
        ArrayList<unoCard> newarray=new ArrayList<>(108);
        newarray.addAll(li);
        TableCardTest.print_card_arraylist(newarray);
    }
}
