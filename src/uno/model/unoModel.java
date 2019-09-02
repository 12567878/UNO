package uno.model;


import uno.ClockObserver;
import uno.ColorObserver;
import uno.ModelInterface;
import uno.tableObserver;

import javax.swing.Timer;
import java.util.ArrayList;

public class unoModel implements ModelInterface {


    private int player_number=4;//default
    private static unoModel singleModel;
    private ArrayList<tableObserver> tableObservers=new ArrayList<>(player_number);//列表向右为顺时针牌桌
    private ArrayList<ClockObserver> clockObservers=new ArrayList<>(player_number);
    private ArrayList<ColorObserver> ColorObservers=new ArrayList<>(player_number);
    private ArrayList<ArrayList<unoCard>>  card_on_player=new ArrayList<>(player_number);
    private ArrayList<unoThread> autoplay_array=new ArrayList<>(player_number);//自动打牌器
    private ArrayList<Boolean> is_alive=new ArrayList<>(player_number);
    private int pointer=0;
    private unoCard current_card;
    private cardFunction current_function;
    private Color current_color;  //换色牌和+4牌
    private int all_plus=0;
    private boolean direction=true;//顺时针逆时针
    private TableCard table_card=new TableCard();
    private Clock clock=new Clock();


    private class Clock{//pass
        private int CLOCK=10;
        private Timer timer=new Timer(1000,event -> {
            CLOCK-=1;
            notifyclockObserver();
            if(CLOCK<=0){
                skip();
            }
        });

        public Clock(){}
        public void start(){
            timer.start();
            notifyclockObserver();
        }
        public void reset_10(){
            CLOCK=10;
            timer.stop();
            timer.start();
        }

        public void reset_5(){
            CLOCK=5;
            timer.stop();
            timer.start();
        }
    }


    private unoModel(){

        //洗牌
        for (ArrayList<unoCard> array:card_on_player
             ) {
            array.addAll(table_card.getCard(7));
            array.sort(null);
        }//初始化二维数组,每人七张牌
        do {//开局不能拿到功能牌
            current_card = table_card.getCard(1).get(0);
        }while(current_card.getNumber()==-1);

        clock.start();
        notifytableObserver();
    }


    public static synchronized unoModel getSingleModel() {
        if(singleModel==null){
            singleModel=new unoModel();
        }
        return singleModel;
    }

    void next(){  //还要加入判断是否已经获胜
        if(direction){
            if(pointer<player_number-1){//要减一
                pointer+=1;
            }
            else{pointer=0;}
        }
        else if(pointer>0){
            pointer-=1;
        }
        else {pointer=player_number-1;}
    }

    boolean is_BP4(unoCard C){
        return C.getColor()==Color.Black
                &&C.getFunc()==cardFunction.plusFour_ChangeColor;
    }

    boolean is_CH(unoCard C){
        return C.getColor()==Color.Black
                &&C.getFunc()==cardFunction.ChangeColor;
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

    void give_card(int pointer,int n){
        card_on_player.get(pointer).addAll(table_card.getCard(n));
        card_on_player.get(pointer).sort(null);
    }

    void notifytableObserver(){
        for (tableObserver o:tableObservers
             ) {
            o.update_card();
            o.update_pointer();
        }
    }

    void notifycolorObserver() {
        ColorObservers.get(pointer);
    }

    void notifyclockObserver(){
        for(int i=0;i<clockObservers.size();++i){
            clockObservers.get(i).update_clock(i,clock.CLOCK);
        }
    }

    @Override
    public synchronized int registerObserver(tableObserver o) {
        tableObservers.add(o);
        return tableObservers.size()-1;//返回其在数组位置
    }

    @Override
    public void removeObserver(tableObserver o) {

    }

    @Override
    public void registerObserver(ColorObserver o) {
        ColorObservers.add(o);
    }

    @Override
    public void removeObserver(ColorObserver o) {

    }

    @Override
    public void registerObserver(ClockObserver o) {
        clockObservers.add(o);
    }

    @Override
    public void removeObserver(ClockObserver o) {

    }

    @Override
    public synchronized void send_card(int num, int a) {
        unoCard C= card_on_player.get(num).get(a);
        if(pointer==num){
            if (match(C)||qiang(C)){
                current_card=C;
                card_on_player.get(num).remove(a);
                if(C.getNumber()==-1)
                {
                    update_function();//仅开启选色器，或者更新功能信息
                }

                if(current_card.getFunc()==cardFunction.ChangeColor//换色牌
                    ||current_card.getFunc()==cardFunction.plusFour_ChangeColor)
                {
                    clock.reset_5();
                    notifycolorObserver();
                    notifytableObserver();
                }
                else{
                    next();
                    clock.reset_10();
                    notifytableObserver();}//notify一定是最后一步
                }
        }
        else{
            if(qiang(C)){
                current_card=C;
                card_on_player.get(num).remove(a);
                pointer=num;
                if(C.getNumber()==-1){
                    update_function();
                }
                next();//要的要的
                clock.reset_10();
                notifytableObserver();

            }
        }
    }

    @Override
    public void choose_color(int n) {
        switch (n) {
            case 1:current_color=Color.Red;
                break;
            case 2:current_color=Color.Yellow;
                break;
            case 3:current_color=Color.Blue;
                break;
            case 4:current_color=Color.Green;
        }
        next();
        clock.reset_10();
        notifytableObserver();
    }

    @Override
    public void skip() { //清空功能，然后分情况给牌
           //统一给一个flag，指示是否能skip
        perform_function();
        next();
        current_function=null;
        clock.reset_10();
        notifytableObserver();
    }

    void update_function(){
        switch (current_card.getFunc()){
            case Forbid:{current_function=cardFunction.Forbid;break;}
            case plusTwo:{current_function=cardFunction.plusTwo;all_plus+=2;break;}
            case Reverse:{direction=!direction;break;}
            case plusFour_ChangeColor:{ all_plus+=4; }
            case ChangeColor:{
                notifycolorObserver();
                break;
            }


        }
    }

    void perform_function(){
        if(current_function==null){
            give_card(pointer,1);
        }
        else
            switch (current_function){
                case Forbid:break;
                case plusTwo:
                case plusFour_ChangeColor: {give_card(pointer,all_plus);break;}
                default:give_card(pointer,1);
            }
            current_function=null;
    }

    @Override
    public ArrayList<unoCard> get_my_card(int num) {
        return card_on_player.get(num);
    }

    @Override
    public void get_others_card() {

    }

    @Override
    public void get_current_card() {

    }

    @Override
    public int get_pointer() {
        return pointer;
    }




}
