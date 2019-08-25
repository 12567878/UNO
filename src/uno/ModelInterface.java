package uno;

public interface ModelInterface {

    int registerObserver(tableObserver o);

    void removeObserver(tableObserver o);

    void registerObserver(ColorObserver o);

    void removeObserver(ColorObserver o);

    void registerObserver(ClockObserver o);

    void removeObserver(ClockObserver o);

    void send_card(int num, int a);

    void skip(int num);

    void get_my_card();

    void get_others_card( );

    void get_current_card(  );

    void get_pointer(  );


}

