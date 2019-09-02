package uno;

import uno.model.unoCard;

import java.util.ArrayList;

public interface ModelInterface {

    int registerObserver(tableObserver o);

    void removeObserver(tableObserver o);

    void registerObserver(ColorObserver o);

    void removeObserver(ColorObserver o);

    void registerObserver(ClockObserver o);

    void removeObserver(ClockObserver o);

    void send_card(int num, int a);

    void skip();

    ArrayList<unoCard> get_my_card(int num);

    void get_others_card( );

    void get_current_card(  );

    int get_pointer(  );

    void choose_color(int n);


}

