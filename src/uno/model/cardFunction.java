package uno.model;

public enum cardFunction {
    ChangeColor(1),plusTwo(2),plusFour_ChangeColor(3),Skip(4),Reverse(5);

    public final int priority; //变量

    cardFunction(int a){
        priority=a;
    }
}
