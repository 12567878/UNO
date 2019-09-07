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
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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
    private Rectangle color_rect;

    @FXML
    private HBox color_chooser;


    @FXML
    private Rectangle green;

    @FXML
    private Rectangle yellow;
    @FXML
    private Rectangle red;
    @FXML
    private Rectangle blue;

    @FXML
    void choose_red(MouseEvent event) {
        controller.choose_color(1);
        close_color_chooser();
    }

    @FXML
    void choose_yellow(MouseEvent event) {
        controller.choose_color(2);
        close_color_chooser();
    }

    @FXML
    void choose_blue(MouseEvent event) {
        controller.choose_color(3);
        close_color_chooser();
    }

    @FXML
    void skip(MouseEvent event) {//要改成Mouseevent  argument type mismatch
        controller.skip();
    }

    @FXML
    void choose_green(MouseEvent event) {
        controller.choose_color(4);
        close_color_chooser();
    }

    boolean choosing=false;

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

    //设置四个点击事件，分别选择颜色并关闭选色器

    @Override
    public void update_color_chooser() {
        //点击以后关掉!!!
        color_chooser.opacityProperty().setValue(1);
        choosing=true;

    }

    void close_color_chooser(){
        color_chooser.opacityProperty().setValue(0);
        choosing=false;
    }

    @Override
    public void update_color(Color color) {//current_color
        if(color==null)color_rect.opacityProperty().setValue(0);
        else {
            color_rect.opacityProperty().setValue(1);
            switch (color){
                case Yellow:color_rect.setFill(javafx.scene.paint.Color.YELLOW);break;
                case Blue:color_rect.setFill(javafx.scene.paint.Color.BLUE);break;
                case Green:color_rect.setFill(javafx.scene.paint.Color.GREEN);break;
                case Red:color_rect.setFill(javafx.scene.paint.Color.RED);break;
            }
        }
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

