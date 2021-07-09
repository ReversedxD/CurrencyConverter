import org.json.JSONObject;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {
        boolean run = true;
        do {

            HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();
            currencyCodes.put(1, "USD");
            currencyCodes.put(2, "CAD");
            currencyCodes.put(3, "EUR");
            currencyCodes.put(4, "HKD");
            currencyCodes.put(5, "INR");

            String fromCode, toCode;
            double amt;

            Scanner sc = new Scanner(System.in);

            System.out.println("Hola, welcome to Currency Converter");

            System.out.println("Convert Currency Form:");
            System.out.println("1: USD(US DOLLAR) \t2: CAD (CANADIAN DOLLAR) \t3: EUR (EUROS) \t4: HKD(HONK KONG DOLLAR) \t5: INR (INDIAN RUPEE)");
            fromCode = currencyCodes.get(sc.nextInt());

            System.out.println("Convert Currency to:");
            System.out.println("1: USD(US DOLLAR) \t2: CAD (CANADIAN DOLLAR) \t3: EUR (EUROS) \t4: HKD(HONK KONG DOLLAR) \t5: INR (INDIAN RUPEE)");
            toCode = currencyCodes.get(sc.nextInt());

            System.out.println("Enter the amount");
            amt = sc.nextDouble();

            sendHttpGETRequest(fromCode, toCode, amt);
            System.out.println("Go again?");
            System.out.println("press 'y' to GO again and any other key to exit");
            if(sc.next().equalsIgnoreCase("y")){
                run = true;
            }
            else {
                run = false;
            }
        }while (run);
        System.out.println("\t\t\t\t\t Thank You \t\t\t\t\t");
    }

    private static void sendHttpGETRequest(String fromCode, String toCode, double amt) throws IOException {

        DecimalFormat f = new DecimalFormat("00.00");
        String GET_URL = "https://v6.exchangerate-api.com/v6/61e5c35ed102a462b4a9f225/latest/"+fromCode;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();


        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }in.close();

            JSONObject obj = new JSONObject(response.toString());
            Double exchangeRate = obj.getJSONObject("conversion_rates").getDouble(toCode);
            //System.out.println(obj.getJSONObject("conversion_rates"));
            System.out.println(exchangeRate);
            System.out.println();
            System.out.println(f.format(amt)+ fromCode+ "=" +f.format(amt*exchangeRate) + toCode);
        }
        else {
            System.out.println("Get request failed");
        }
    }

}
