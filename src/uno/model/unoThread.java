package uno.model;

import uno.ColorObserver;
import uno.ControllerInterface;
import uno.ModelInterface;
import uno.tableObserver;

import java.util.ArrayList;

public class unoThread extends Thread implements tableObserver, ControllerInterface {
    ModelInterface model;
    ArrayList<unoCard> my_card_array;
    int pointer;//决定出牌策略
    final int num;
    public unoThread(){
        num=model.registerObserver((tableObserver) this);

    }

    @Override
    public void run(){
        while (my_card_array.size()!=0){



        }
    }

    @Override
    public void update_card() {
        my_card_array.clear();
        my_card_array.addAll(model.get_my_card(num));
    }

    @Override
    public void update_pointer() {
        pointer=model.get_pointer();
    }

    @Override
    public void update_color_chooser() {

    }

    @Override
    public void send_card(int a) {
        model.send_card(num,a);
    }

    @Override
    public void skip() {
        model.skip(num);
    }


}
