package Builders;
import java.util.ArrayList;

/**
 * A class to build an ArrayList
 * @param <T> The type of elements in the ArrayList
 */
public class ArrayListBuilder<T extends Object> {
	
    private ArrayList<T> arrayList = new ArrayList<>();
    
    /**
     * Creates a new ArrayList with all the elements provided
     * @param elements Any number of elements of type T
     */
    public ArrayListBuilder(T... elements) {
        for (T element: elements) {
            arrayList.add(element);
        }
    }
    
    /**
     * Return the built ArrayList
     * @return The built ArrayList
     */
    public ArrayList<T> build() {
        return this.arrayList;
    }
}
