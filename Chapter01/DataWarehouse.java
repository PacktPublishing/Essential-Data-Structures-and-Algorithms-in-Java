import java.util.HashMap;
import java.util.Map;

public class DataWarehouse {
    // Use a HashMap to store data in columns
    private Map<Integer, Object[]> dataMap; 
    public static final int DATA_LENGTH = 4;

    public DataWarehouse() {
        this.dataMap = new HashMap<>();
    }

    // Add data to the warehouse
    public void addData(int id, String name, int age, String city) {
        Object[] values = new Object[DATA_LENGTH];
        values[0] = id;
        values[1] = name;
        values[2] = age;
        values[3] = city;
        dataMap.put(id, values);
    }

    // Retrieve data from the warehouse
    public Object[] getData(int id) {
        return dataMap.get(id);
    }

    // Perform a query on the data
    public void query(String condition) {
        if (condition.equals("age > 30")) {
            for (Object[] values : dataMap.values()) {
                int age = (int) values[2];
                if (age > 30) {
                    System.out.println(values[0] + ", " + values[1] + ", " + values[2]);
                }
            }
        } else {
            throw new RuntimeException("Invalid condition");
        }
    }

    public static void main(String[] args) {
        DataWarehouse dw = new DataWarehouse();
        // Hydrate data warehouse
        dw.addData(1, "Alice", 30, "NYC");
        dw.addData(2, "Bob", 25, "LA");
        dw.addData(3, "Carol", 35, "SEA");
        // Run query
        dw.query("age > 30");
        // Fetch data by id
        System.out.println(dw.getData(1));
    }
}
