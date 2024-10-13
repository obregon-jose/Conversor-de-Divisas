import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyService {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/00effe2cd1500024d8324567/latest/";

    // Método para cargar las monedas disponibles desde la API
    public String[] loadCurrencies() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "USD")) // Usamos USD como referencia
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonResponse = parseJson(response.body());

        if (jsonResponse.get("result").getAsString().equals("success")) {
            return jsonResponse.getAsJsonObject("conversion_rates").keySet().toArray(new String[0]);
        } else {
            throw new Exception("Error al obtener las monedas disponibles.");
        }
    }

    // Método para realizar la conversión de moneda
    public double convertCurrency(String baseCurrency, String targetCurrency, double amount) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String url = BASE_URL + baseCurrency;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonResponse = parseJson(response.body());

        if (jsonResponse.get("result").getAsString().equals("success")) {
            double conversionRate = jsonResponse.getAsJsonObject("conversion_rates").get(targetCurrency).getAsDouble();
            return amount * conversionRate;
        } else {
            throw new Exception("Error al obtener los datos de la API.");
        }
    }

    // Método para parsear JSON usando Gson
    private JsonObject parseJson(String responseBody) {
        Gson gson = new Gson();
        return gson.fromJson(responseBody, JsonObject.class);
    }
}
