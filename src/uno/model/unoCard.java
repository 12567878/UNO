package uno.model;


public class unoCard implements Comparable<unoCard> {
    private final Color color;
    private final int Number;
    private cardFunction Func;

    public unoCard(Color color,int Number){
        this.color=color;
        this.Number=Number;
    }

    public unoCard(Color color,cardFunction Func){
        this.color=color;
        this.Func=Func;
        this.Number=-1;
    }

    public int getNumber() {
        return Number;
    }

    public Color getColor() {
        return color;
    }

    public cardFunction getFunc() {
        return Func;
    }

    @Override
    public int compareTo(unoCard C) {
        if(this.color!=C.color) return this.color.priority-C.color.priority;
        else if(this.Func==null&&C.Func==null){    //数字牌排序,注意不能用Number==null,基本数据类型没null(?)
            return C.Number-this.Number;  //小数字在前
        }
        else if (this.Func!=null && C.Func!=null){  //功能牌排序
                return this.Func.priority-C.Func.priority;
            }
        else return C.Number-this.Number;  //功能牌比数字牌大
    }


}


