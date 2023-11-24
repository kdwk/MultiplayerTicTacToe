package Builders;
import java.util.ArrayList;

public class ArrayListBuilder<T extends Object> {
    private ArrayList<T> arrayList = new ArrayList<>();

    public ArrayListBuilder(T... elements) {
        for (T element: elements) {
            arrayList.add(element);
        }
    }

    public ArrayList<T> build() {
        return this.arrayList;
    }
}
