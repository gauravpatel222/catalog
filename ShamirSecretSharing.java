import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecretSharing {

    public static void main(String[] args) {
        try {
            // Step 1: Read the JSON file
            String content = new String(Files.readAllBytes(Paths.get("polynomialRoots.json")));
            JSONObject jsonObject = new JSONObject(content);

            int n = ((Object) jsonObject.getJSONObject("keys")).getInt("n");
            int k = jsonObject.getJSONObject("keys").getInt("k");

            List<Integer> xValues = new ArrayList<>();
            List<Integer> yValues = new ArrayList<>();

            // Step 2: Parse the roots and decode Y values
            for (String key : jsonObject.keySet()) {
                if (!key.equals("keys")) {
                    int x = Integer.parseInt(key);
                    String base = jsonObject.getJSONObject(key).getString("base");
                    String value = jsonObject.getJSONObject(key).getString("value");
                    int y = decodeBaseValue(value, Integer.parseInt(base));

                    xValues.add(x);
                    yValues.add(y);
                }
            }

            // Step 3: Use Lagrange interpolation to find the constant term 'c'
            int constantTerm = findConstantTermUsingLagrange(xValues, yValues, k);
            System.out.println("The constant term (c) of the polynomial is: " + constantTerm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to decode the base value into a decimal number
    public static int decodeBaseValue(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Method to find the constant term using Lagrange interpolation
    public static int findConstantTermUsingLagrange(List<Integer> xValues, List<Integer> yValues, int k) {
        double result = 0.0;

        for (int i = 0; i < k; i++) {
            double term = yValues.get(i);
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= (0 - xValues.get(j)) / (double) (xValues.get(i) - xValues.get(j));
                }
            }
            result += term;
        }
        return (int) Math.round(result);
    }
}
