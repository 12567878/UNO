package uno.model;

import uno.ColorObserver;
import uno.ControllerInterface;
import uno.ModelInterface;
import uno.tableObserver;

import java.util.ArrayList;

public class unoThread extends Thread implements tableObserver, ControllerInterface {
    ModelInterface model;
    ArrayList<unoCard> my_card_array=new ArrayList<>(7);
    int pointer;//决定出牌策略
    final int num;
    private unoCard current_card;
    private cardFunction current_function;
    private Color current_color;

    public unoThread(){
        model=unoModel.getSingleModel();
        num=model.registerObserver((tableObserver) this);
        this.update_card();
    }

    @Override
    public void run(){
        while (my_card_array.size()!=0){
            try {
                sleep((long) (Math.random()*500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double i=Math.random();
            if(i>0.8){
                skip();
                continue;
            };
            if(pointer==num){
                for(int i2=0;i2<my_card_array.size();++i2){
                    if(match(my_card_array.get(i2))){
                        send_card(i2);
                        break;
                    }
                }

            }else{
                for(int i2=0;i2<my_card_array.size();++i2){
                    if(qiang(my_card_array.get(i2))){
                        send_card(i2);
                        break;
                    }
                }
            }
            try {
                sleep((long) (Math.random()*500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update_card() {
        my_card_array.clear();
        my_card_array.addAll(model.get_my_card(num));
        current_card=model.get_current_card();
    }

    @Override
    public void update_pointer() {
        pointer=model.get_pointer();
    }


    @Override
    public void send_card(int a) {
        model.send_card(num,a);
    }

    @Override
    public void skip() {
        model.skip(num);
    }

    @Override
    public void choose_color(int n) {
        return;
    }

    boolean match(unoCard C){
        if(current_card.getNumber()!=-1){
            return C.getColor()==current_card.getColor()
                    ||C.getNumber()==current_card.getNumber()
                    ||is_BP4(C)
                    ||is_CH(C);
        }
        else if(current_card.getColor()!=Color.Black){//彩色功能牌
            if(current_function==null){
                return C.getColor()==current_card.getColor()
                        ||C.getFunc()==current_card.getFunc()
                        ||is_BP4(C)
                        ||is_CH(C);
            }
            else if(current_function==cardFunction.Forbid)
            {return C.getFunc()==current_function;}
            else return C.getFunc()==current_function //
                        ||is_BP4(C);
        }
        else if(current_function==null){//换色牌，过时加四
            return C.getColor()==current_color;
        }
        else return qiang(C);//一定是黑色加四

    }

    boolean qiang(unoCard C){
        if(C.getNumber()==current_card.getNumber()
                &&C.getFunc()==current_card.getFunc()
                &&C.getColor()==current_card.getColor())
            return true;
        else return false;
    }

    boolean is_BP4(unoCard C){
        return C.getColor()==Color.Black
                &&C.getFunc()==cardFunction.plusFour_ChangeColor;
    }

    boolean is_CH(unoCard C){
        return C.getColor()==Color.Black
                &&C.getFunc()==cardFunction.ChangeColor;
    }


}
