package Builders;
import java.util.HashMap;
import java.awt.Component;

/**
 * A class to store a registry of Components that can be accessed by IDs
 */
public final class Components {
    private static HashMap<String, Component> register = new HashMap<>();

    /**
     * Retrieve the Component by ID and cast it to the desired type. Crashes 
     * the program if the Component does not exist
     * @param <T> The type of the retrieved Component. Must extend Component
     * @param id The ID of the Component to be retrieved
     * @return The retrieved Component, cast in type T
     */
    public static <T extends Component> T get(String id) {
        if (register.containsKey(id)) {
            return (T)(register.get(id));
        }
        System.out.println(id + " is not present in the components register!");
        System.exit(0);
        return null;
    }

    /**
     * Register a Component by its ID
     * @param <T> The type of the Component to be registered
     * @param id The ID of the Component to be registered
     * @param component The Component to be registered
     * @return The Component itself
     */
    public static <T extends Component> T reg(String id, T component) {
        if (!register.containsKey(id) && component!=null) {
            register.put(id, component);
            return component;
        }
        System.out.println(id + " is already present in the components register!");
        System.exit(0);
        return null;
    }
}
