package uno.model;

import java.util.ArrayList;
import java.util.List;
/*pass.the examination*/
public class TableCard {
    private ArrayList<unoCard> cardArrayList = new ArrayList<>(115);

    public TableCard() {
        fill_card();
    }

    public List<unoCard> getCard(int n){
        if(cardArrayList.size()<n){//检查牌库是否够
            fill_card();
        }

        List<unoCard> li=cardArrayList.subList(cardArrayList.size()-n,cardArrayList.size());
        ArrayList<unoCard> li2=new ArrayList<>();
        li2.addAll(li);

        int x=cardArrayList.size();
        li.clear();
        return li2;
    }


    private void fill_card() {  //每张牌加两次
        ArrayList<unoCard> templist = new ArrayList<>(108);//然后random抽牌
        //加入数字牌

        for (Color c : Color.values()) {
            if (c != Color.Black) {
                for (int i = 0; i < 10; ++i) {
                    templist.add(new unoCard(c, i));
                    templist.add(new unoCard(c, i));
                }
                //有色功能牌
                templist.add(new unoCard(c, cardFunction.Skip));
                templist.add(new unoCard(c, cardFunction.plusTwo));
                templist.add(new unoCard(c, cardFunction.Skip));
                templist.add(new unoCard(c, cardFunction.plusTwo));
                templist.add(new unoCard(c, cardFunction.Reverse));
                templist.add(new unoCard(c, cardFunction.Reverse));
            } else {
                //无色功能牌
                templist.add(new unoCard(c, cardFunction.ChangeColor));
                templist.add(new unoCard(c, cardFunction.ChangeColor));
                templist.add(new unoCard(c, cardFunction.plusFour_ChangeColor));
                templist.add(new unoCard(c, cardFunction.plusFour_ChangeColor));
            }
        }
        //fill数组
        for(int i=0;i<108;++i){
            int random_int=(int)(Math.random() * templist.size());
            cardArrayList.add(templist.get(random_int));
            templist.remove(random_int);
        }
    }


}

