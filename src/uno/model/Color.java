package uno.model;

public enum Color {
    Red(1), Green(2), Yellow(3), Blue(4), Black(5);

    public final int priority;

    Color(int a){
        priority=a;
    }
}
