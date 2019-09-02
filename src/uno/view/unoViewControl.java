package uno.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Polygon;
import uno.*;
import uno.model.Color;
import uno.model.cardFunction;
import uno.model.unoCard;
import uno.model.unoModel;

/*
*
* 虽然看起来是controller，实际是view
* 由于先生成view，所以要先生成model获取num，再生成相应controller
* 对controller要传入自身num值和所控制model
* */
public class unoViewControl implements EventHandler<MouseEvent>, tableObserver, ColorObserver, ClockObserver {
    ModelInterface model;
    final int num;
    ControllerInterface controller;
    @FXML
    private Label num_of_2;

    @FXML
    private Label num_of_1;

    @FXML
    private Button button_skip;

    @FXML
    private Label num_of_3;

    @FXML
    private FlowPane card_flow;//往里面加卡

    @FXML
    private Polygon tri_pointer;

    public unoViewControl(){
        model= unoModel.getSingleModel();
        //System.out.println("successfully get model");  OK
        int n=model.registerObserver((tableObserver) this);
        num=n;
        controller=new Controller(num,model);
        model.registerObserver((ColorObserver)this);
        model.registerObserver((ClockObserver)this);
    }

    public int getNUM(){
        return num;
    }


    @Override
    public void handle(MouseEvent event) {//作为action listener的方法
        event.getSource();

    }//getSource然后send_card

    /*接下来的update都是作为观察者的方法
    *
    * */

    @Override
    public void update_card() {//自己的，各个桌上牌的数量，当前牌

    }

    @Override
    public void update_pointer() {
        int n=model.get_pointer();
        switch (n){
            case 0:tri_pointer.rotateProperty().setValue(180);
            case 1:tri_pointer.rotateProperty().setValue(90);
            case 2:tri_pointer.rotateProperty().setValue(0);
            case 3:tri_pointer.rotateProperty().setValue(270);
        }
    }

    @Override
    public void update_color_chooser() {

    }

    @Override
    public void update_color() {//current_color

    }

    @Override
    public void update_clock(int num,int time) {

    }

    private String get_card_image_location(unoCard c){
        String ans="file:../jpg/";
        switch (c.getColor()){
            case Black:{
                if(c.getFunc()== cardFunction.ChangeColor){
                    ans+="CH.jpg";
                    break;
                }
                else ans+="BP4.jpg";
                break;
            }
            case Red:ans+="R";break;
            case Blue:ans+="B";break;
            case Green:ans+="G";break;
            case Yellow:ans+="Y";break;
        }
        switch (c.getNumber()){
            case -1:{
                if(c.getColor()== Color.Black)break;
                else {
                    switch (c.getFunc()){
                    case Reverse:ans+="R.jpg";break;
                    case Forbid:ans+="F.jpg";break;
                    case plusTwo:ans+="P2.jpg";break;
                    }
                    break;
                }
            }
            default:ans+=c.getNumber()+".jpg";break;
        }
        return ans;
    }

}

