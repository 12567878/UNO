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

    void skip(int n);

    ArrayList<unoCard> get_my_card(int num);

    ArrayList<Integer> get_others_card(int num);

    unoCard get_current_card(  );

    int get_pointer(  );

    void choose_color(int n);


}

