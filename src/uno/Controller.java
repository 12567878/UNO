package uno;

import uno.model.unoModel;
import uno.view.unoView;

public class Controller implements ControllerInterface {

    ModelInterface model= unoModel.getSingleModel();
    unoView view;
    final int num;

    public Controller(){
        view=new unoView();
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
