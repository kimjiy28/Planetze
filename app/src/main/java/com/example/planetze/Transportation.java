package com.example.planetze;

import android.app.Activity;

public class Transportation {
    String activity;
    String date;
    double emission;

    public Transportation(String activity, String date) {
        this.activity = activity;
        this.date = date;
    }
}

class PersonalVehicle extends Transportation {
    double distance;
    String vehicle;

    public PersonalVehicle (String date, double distance, String vehicle) {
        super("vehicle", date);
        this.distance = distance;
        this.vehicle = vehicle;
    }
}

class PublicTransportation extends Transportation {
    String type;
    double duration;

    public PublicTransportation (String type, String date, double duration) {
        super("public transportation", date);
        this.type = type;
        this.duration = duration;
    }
}

class NoTransportation extends Transportation{
    double distance;

    public NoTransportation (double distance, String date) {
        super("cycling or walking", date);
        this.distance = distance;
    }
}

class Flight extends Transportation {
    double frequency;
    String relativeDistance;

    Flight (double frequency, String date, String relativeDistance) {
        super("flight", date);
        this.frequency = frequency;
        this.relativeDistance = relativeDistance;
    }
}
