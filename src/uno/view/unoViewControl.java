package uno.view;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import uno.ClockObserver;
import uno.ColorObserver;
import uno.ModelInterface;
import uno.tableObserver;

public class unoViewControl implements EventHandler<MouseEvent>, tableObserver, ColorObserver, ClockObserver {
    ModelInterface model;
    final int num;

    public unoViewControl(){
        int n=model.registerObserver((tableObserver) this);
        num=n;
        model.registerObserver((ColorObserver)this);
    }

    public int getNUM(){
        return num;
    }


    @Override
    public void handle(MouseEvent event) {

    }

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
