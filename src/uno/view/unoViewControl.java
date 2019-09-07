package uno.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Polygon;
import uno.*;
import uno.model.Color;
import uno.model.cardFunction;
import uno.model.unoCard;
import uno.model.unoModel;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

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
    private FlowPane card_flow;

    @FXML
    private Polygon tri_pointer;

    @FXML
    private Label time_label;


    @FXML
    private ImageView current_card;

    @FXML
    void skip(MouseEvent event) {//要改成Mouseevent  argument type mismatch
        controller.skip();
    }

    public unoViewControl(){
        model= unoModel.getSingleModel();
        //System.out.println(num_of_2==null); // 也许是先构造这个然后后将fxml实现
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
        ArrayList<unoCard> my_card=model.get_my_card(num);
        ArrayList<Integer>others_card_number=model.get_others_card(num);
        unoCard unocard=model.get_current_card();

        String lo=get_card_image_location(unocard);//需要重构
        File fi=new File(lo);
        Image im=null;
        try {
            im = new Image(fi.toPath().toUri().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        current_card.setImage(im);
        card_flow.getChildren().clear();
        for (unoCard c:my_card
             ) {
            String location=get_card_image_location(c);//需要重构
            File f=new File(location);
            //System.out.println(f.canRead());//尝试是否可读路径 可以
            Image image= null;
            try {
                image = new Image(f.toPath().toUri().toURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ImageView imageView=new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(180);
            imageView.setFitWidth(125);
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
                ImageView i= (ImageView) event.getSource();
                int n=search_in_flow(card_flow,i);
                if(n!=-1){
                    controller.send_card(n);
                }
            });
            card_flow.getChildren().add(imageView);
        }
        num_of_1.textProperty().setValue(Integer.toString(others_card_number.get(0)));
        num_of_2.textProperty().setValue(Integer.toString(others_card_number.get(1)));
        num_of_3.textProperty().setValue(Integer.toString(others_card_number.get(2)));
    }

    @Override
    public void update_pointer() {
        int n=model.get_pointer();
        switch (n){
            case 0:tri_pointer.rotateProperty().setValue(180);break;
            case 1:tri_pointer.rotateProperty().setValue(90);break;
            case 2:tri_pointer.rotateProperty().setValue(0);break;
            case 3:tri_pointer.rotateProperty().setValue(270);break;
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
        time_label.setText(Integer.toString(time));
    }

    private String get_card_image_location(unoCard c){//此方法正确
        String ans="src/uno/jpg/";
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

    private int search_in_flow(FlowPane f,Node n){
        for (int i=0;i<f.getChildren().size();++i){
            if(n==f.getChildren().get(i))return i;
        }
        return -1;
    }

}

