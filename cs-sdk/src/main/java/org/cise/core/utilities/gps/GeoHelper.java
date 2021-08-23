package org.cise.core.utilities.gps;

import org.cise.core.utilities.format.NFormat;

public class GeoHelper {

    public static String unitMeter = "m";
    public static String unitMiles = "M";
    public static String unitKilometer = "K";
    public static String unitNauticalMiles = "N";

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unitMeter.equals(unit)) {
            dist = (dist * 1.609344) * 1000.0;
        } else if (unitKilometer.equals(unit)) {
            dist = dist * 1.609344;
        } else if (unitNauticalMiles.equals(unit)) {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    public static String distance(double lat1, double lon1, double lat2, double lon2) {
        double distanceInKM = distance(lat1, lon1, lat2, lon2, unitMeter);
        StringBuilder sb = new StringBuilder();
        if (1000.0 < distanceInKM) {
            double inKM = distanceInKM / 1000.0;
            sb.append(NFormat.currencyFormat(inKM));
            sb.append(" ");
            sb.append("Km");
        } else {
            sb.append(NFormat.currencyFormat(distanceInKM));
            sb.append(" ");
            sb.append("m");
        }
        return sb.toString();
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians			:*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees			:*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    public static boolean notInOneZero(double lat, double lon) {
        return (((lat > 1) || (lat < 0)) || ((lon > 1) || (lon < 0)));
        //return (lat > 1 && lon > 1 && lat < 0 && lon < 0);
    }
}
