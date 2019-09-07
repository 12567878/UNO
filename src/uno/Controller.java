package uno;

import uno.model.unoModel;
import uno.view.unoViewControl;

public class Controller implements ControllerInterface {

    ModelInterface model;
    unoViewControl view;
    final int num;

    public Controller(int n,ModelInterface model){
        num=n;
        this.model=model;
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
