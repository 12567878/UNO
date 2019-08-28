package uno;

import uno.model.unoModel;
import uno.view.unoViewControl;

public class Controller implements ControllerInterface {

    ModelInterface model= unoModel.getSingleModel();
    unoViewControl view;
    final int num;

    public Controller(){
        view=new unoViewControl();
        num=view.getNUM();
    }

    @Override
    public void send_card(int a) {
        model.send_card(num,a);
    }

    @Override
    public void skip() {

    }
}
