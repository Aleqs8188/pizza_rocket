package org.oleksii.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import org.oleksii.enums.ConsoleColor;

public class DistanceCalculateApi {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private static final String FORMAT_JSON = "format=json";

    public static double getResultOfDistance(String postalCode1, String postalCode2) {
        double distance = 0;
        try {
            distance = getDistanceBetweenPostalCodes(postalCode1, postalCode2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distance;
    }

    private static double getDistanceBetweenPostalCodes(String postalCode1, String postalCode2) throws UnirestException {
        String url1 = buildNominatimUrl(postalCode1);
        String url2 = buildNominatimUrl(postalCode2);

        HttpResponse<JsonNode> response1 = Unirest.get(url1).header("accept", "application/json").asJson();
        JSONObject body1 = response1.getBody().getArray().getJSONObject(0);

        double lat1 = body1.getDouble("lat");
        double lon1 = body1.getDouble("lon");

        try {
            HttpResponse<JsonNode> response2 = Unirest.get(url2).header("accept", "application/json").asJson();
            JSONObject body2 = response2.getBody().getArray().getJSONObject(0);

            double lat2 = body2.getDouble("lat");
            double lon2 = body2.getDouble("lon");

            return calculateDistance(lat1, lon1, lat2, lon2);
        } catch (JSONException ignored) {
            System.out.println(ConsoleColor.RED.getCode() + ConsoleColor.BOLD.getCode() + "You have entered an incorrect postal code" + ConsoleColor.RESET.getCode());
        }
        return -1;
    }

    private static String buildNominatimUrl(String postalCode) {
        return String.format("%s?postalcode=%s&%s", NOMINATIM_URL, postalCode, FORMAT_JSON);
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}