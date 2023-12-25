package org.denys.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

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

    private static double getDistanceBetweenPostalCodes(String postalCode1, String postalCode2) throws Exception {
        String url1 = buildNominatimUrl(postalCode1);
        String url2 = buildNominatimUrl(postalCode2);

        HttpResponse<JsonNode> response1 = Unirest.get(url1).header("accept", "application/json").asJson();
        kong.unirest.HttpResponse<kong.unirest.JsonNode> response2 = Unirest.get(url2).header("accept", "application/json").asJson();

        double lat1 = response1.getBody().getArray().getJSONObject(0).getDouble("lat");
        double lon1 = response1.getBody().getArray().getJSONObject(0).getDouble("lon");

        double lat2 = response2.getBody().getArray().getJSONObject(0).getDouble("lat");
        double lon2 = response2.getBody().getArray().getJSONObject(0).getDouble("lon");

        return calculateDistance(lat1, lon1, lat2, lon2);
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