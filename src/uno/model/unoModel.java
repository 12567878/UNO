package uno.model;


import javafx.application.Platform;
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
    private boolean choosing_color=false;

    private class Clock{//pass
        private volatile int CLOCK=10;
        private Timer timer=new Timer(1000,event -> {
            CLOCK-=1;
            notifyclockObserver();
            if(CLOCK<=0){
                if(choosing_color){
                    int n= (int) (Math.random()*4+1);
                    choose_color(n);
                }
                else {
                    skip(pointer);
                }
            }
        });

        public Clock(){}

        public synchronized int getCLOCK() {
            return CLOCK;
        }

        public void start(){
            timer.start();
            notifyclockObserver();
        }
        public void reset_10(){
            getCLOCK();
            CLOCK=10;
            timer.stop();
            timer.start();
        }

        public void reset_5(){
            getCLOCK();
            CLOCK=5;
            timer.stop();
            timer.start();
        }
    }


    private unoModel(){

        //洗牌
        for(int i=0;i<4;++i){
            card_on_player.add(new ArrayList<>());//需要先加4个空列表才行
        }
        for (ArrayList<unoCard> array:card_on_player
             ) {
            array.addAll(table_card.getCard(7));
            array.sort(null);
        }//初始化二维数组,每人七张牌
        do {//开局不能拿到功能牌
            current_card = table_card.getCard(1).get(0);
        }while(current_card.getNumber()==-1);

        clock.start();

        //notifytableObserver();  还没注册观察者
    }


    public void single_start(){
        autoplay_array.add(new unoThread());
        autoplay_array.add(new unoThread());
        autoplay_array.add(new unoThread());
        for (unoThread t:autoplay_array
             ) {
            t.start();
        }
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

    public void notifytableObserver(){
        Platform.runLater(()->{
            for (tableObserver o:tableObservers
            ) {
                o.update_card();
                o.update_pointer();
            }
        });
    }

    void notifycolorObserver() {
        if(!choosing_color){
            ColorObservers.get(0).update_color(current_color);
            //全关了,根据currentcolor显示颜色
        }
        else if(pointer!=0){
            int n=(int)(Math.random()*4+1);
            choose_color(n);
            ColorObservers.get(0).update_color(current_color);
        }
        else{//是玩家
            ColorObservers.get(0).update_color_chooser();
        }
    }

    void notifyclockObserver(){//需要考虑clock的线程问题,需要借助runlater方法
        Platform.runLater(()->{
            for(int i=0;i<clockObservers.size();++i){
                clockObservers.get(i).update_clock(i,clock.getCLOCK());
            }
        });

    }

    @Override
    public synchronized int registerObserver(tableObserver o) {
        tableObservers.add(o);
        //notifytableObserver(); //牌还没发
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
        if(a>=card_on_player.get(num).size())return;
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
                    choosing_color=true;
                    notifycolorObserver();
                    notifytableObserver();
                }
                else{
                    next();
                    clock.reset_10();
                    notifytableObserver();
                }//notify一定是最后一步
                current_color=null;
                ColorObservers.get(0).update_color(current_color);//让颜色框归零
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
        choosing_color=false;
        notifytableObserver();
        notifycolorObserver();
    }

    @Override
    public void skip(int n) { //清空功能，然后分情况给牌
           //统一给一个flag，指示是否能skip
        if(n==pointer) {
            perform_function();
            next();
            current_function=null;
            clock.reset_10();
            notifytableObserver();
        }
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
        assert card_on_player.size()>0;
        return card_on_player.get(num);
    }

    @Override
    public ArrayList<Integer> get_others_card(int num) {
        ArrayList<Integer> others_card_array=new ArrayList<>(3);
        for(int i=0;i<3;++i){
            if(num<card_on_player.size()-1){
                num++;
                others_card_array.add(card_on_player.get(num).size());
            }
            else{
                num=0;
                others_card_array.add(card_on_player.get(num).size());
            }
        }
        return others_card_array;
    }

    @Override
    public unoCard get_current_card() {
        return current_card;
    }

    @Override
    public Color get_color() {
        return current_color;
    }

    @Override
    public int get_pointer() {
        return pointer;
    }




}
