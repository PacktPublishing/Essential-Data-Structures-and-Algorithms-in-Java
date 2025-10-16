import java.util.HashMap;
import java.util.Map;

public class DataWarehouse {
    // Use a HashMap to store data in columns
    private Map<Integer, Object[]> dataMap; 
    public static final int DATA_LENGTH = 3;

    public DataWarehouse() {
        this.dataMap = new HashMap<>();
    }

    // Add data to the warehouse
    public void addData(int id, String name, double grade) {
        Object[] values = new Object[DATA_LENGTH];
        values[0] = id;
        values[1] = name;
        values[2] = grade;
        dataMap.put(id, values);
    }

    // Retrieve data from the warehouse
    public Object[] getData(int id) {
        return dataMap.get(id);
    }

    // Perform a query on the data
    public void query(String condition) {
        if (condition.equals("grade >= 60.0")) {
            for (Object[] values : dataMap.values()) {
                double grade = (double) values[2];
                if (grade >= 60.0) {
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
        dw.addData(1, "Bob", 61);
        dw.addData(2, "Alice", 99);
        dw.addData(3, "John", 59.99);
        dw.addData(4, "Kevin", 88);
        dw.addData(5, "Jane", 30.20);
        // Run query
        dw.query("grade >= 60.0");
    }
}
