package uno.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import uno.*;
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


    public unoViewControl(){
        model= unoModel.getSingleModel();
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

    }

    /*接下来的update都是作为观察者的方法
    *
    * */

    @Override
    public void update_card() {

    }

    @Override
    public void update_pointer() {
        model.get_pointer();
    }

    @Override
    public void update_color_chooser() {

    }

    @Override
    public void update_color() {

    }

    @Override
    public void update_clock(int n) {

    }
}
