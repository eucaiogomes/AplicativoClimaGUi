import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// recuperar dados meteorológicos da API - esta lógica de backend buscará os dados mais recentes de clima
// da API externa e os retornará. A interface gráfica exibirá esses dados ao usuário
public class WeatherApp {
    // busca dados meteorológicos para a localização fornecida
    public static JSONObject getWeatherData(String locationName){
        // obtém as coordenadas da localização usando a API de geolocalização
        JSONArray locationData = getLocationData(locationName);

        // extrai dados de latitude e longitude
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // constrói a URL de requisição da API com as coordenadas da localização
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&timezone=America%2FLos_Angeles";

        try{
            // chama a API e obtém a resposta
            HttpURLConnection conn = fetchApiResponse(urlString);

            // verifica o status da resposta
            // 200 - significa que a conexão foi bem-sucedida
            if(conn.getResponseCode() != 200){
                System.out.println("Erro: Não foi possível conectar à API");
                return null;
            }

            // armazena os dados JSON resultantes
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while(scanner.hasNext()){
                // lê e armazena na StringBuilder
                resultJson.append(scanner.nextLine());
            }

            // fecha o scanner
            scanner.close();

            // fecha a conexão URL
            conn.disconnect();

            // faz o parsing dos dados
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            // recupera os dados horários
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            // queremos obter os dados da hora atual
            // então precisamos encontrar o índice da nossa hora atual
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            // obtém a temperatura
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            // obtém o código do clima
            JSONArray weathercode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            // obtém a umidade
            JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            // obtém a velocidade do vento
            JSONArray windspeedData = (JSONArray) hourly.get("windspeed_10m");
            double windspeed = (double) windspeedData.get(index);

            // constrói o objeto JSON com os dados meteorológicos que vamos acessar na interface gráfica
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);

            return weatherData;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    // recupera as coordenadas geográficas para o nome da localização fornecida
    public static JSONArray getLocationData(String locationName){
        // substitui qualquer espaço no nome da localização por + para aderir ao formato de requisição da API
        locationName = locationName.replaceAll(" ", "+");

        // constrói a URL da API com o parâmetro de localização
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=pt&format=json";

        try{
            // chama a API e obtém a resposta
            HttpURLConnection conn = fetchApiResponse(urlString);

            // verifica o status da resposta
            // 200 significa conexão bem-sucedida
            if(conn.getResponseCode() != 200){
                System.out.println("Erro: Não foi possível conectar à API");
                return null;
            }else{
                // armazena os resultados da API
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                // lê e armazena os dados JSON resultantes na nossa StringBuilder
                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }

                // fecha o scanner
                scanner.close();

                // fecha a conexão URL
                conn.disconnect();

                // faz o parsing da string JSON para um objeto JSON
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                // obtém a lista de dados de localização que a API gerou a partir do nome da localização
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        // não foi possível encontrar a localização
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString){
        try{
            // tenta criar a conexão
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // define o método de requisição para GET
            conn.setRequestMethod("GET");

            // conecta à nossa API
            conn.connect();
            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }

        // não foi possível fazer a conexão
        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList){
        String currentTime = getCurrentTime();

        // itera pela lista de horários e vê qual corresponde ao nosso horário atual
        for(int i = 0; i < timeList.size(); i++){
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                // retorna o índice
                return i;
            }
        }

        return 0;
    }

    private static String getCurrentTime(){
        // obtém a data e hora atuais
        LocalDateTime currentDateTime = LocalDateTime.now();

        // formata a data para ser 2023-09-02T00:00 (é assim que a API lê)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        // formata e imprime a data e hora atuais
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    // converte o código do clima para algo mais legível
    private static String convertWeatherCode(long weathercode){
        String weatherCondition = "";
        if(weathercode == 0L){
            // claro
            weatherCondition = "Claro";
        }else if(weathercode > 0L && weathercode <= 3L){
            // nublado
            weatherCondition = "Nublado";
        }else if((weathercode >= 51L && weathercode <= 67L)
                    || (weathercode >= 80L && weathercode <= 99L)){
            // chuva
            weatherCondition = "Chuva";
        }else if(weathercode >= 71L && weathercode <= 77L){
            // neve
            weatherCondition = "Neve";
        }

        return weatherCondition;
    }
}
