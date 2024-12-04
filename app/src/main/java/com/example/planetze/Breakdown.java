package com.example.planetze;
public class Breakdown  {
    String id;
    String category;
    String activity;
    double emission;

    public Breakdown() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getEmission() {
        return emission;
    }

    public void setEmission(double emission) {
        this.emission = emission;
    }
}

class PersonalVehicle extends Breakdown {
    double distance;
    String type;

    public PersonalVehicle() {
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PersonalVehicle (double distance, String type) {
        this.setCategory("Transportation");
        this.setActivity("vehicle");
        double perKm;
        if (type.equals("Diesel")) {
            perKm = 0.27;
        } else if (type.equals("Hybrid")) {
            perKm = 0.16;
        } else if (type.equals("0.05")) {
            perKm = 0.16;
        } else {
            perKm = 0.24;
        }
        this.emission = perKm * distance;
        this.distance = distance;
        this.type = type;
    }
}

class PublicTransportation extends Breakdown {
    String type;
    double duration;

    public PublicTransportation() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public PublicTransportation (String type, double duration) {
        this.setCategory("Transportation");
        this.setActivity("Public Transportation");
        this.emission = duration * 1.5;
        this.type = type;
        this.duration = duration;
    }
}

class Walking extends Breakdown{
    double distance;

    public Walking() {
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Walking (double distance) {
        this.setCategory("Transportation");
        this.setActivity("Cycling or Walking");
        this.emission = 0.21 * distance;
        this.distance = distance;
    }
}

class Flight extends Breakdown {
    double frequency;
    String length;

    public Flight() {
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Flight (double frequency, String length) {
        this.setCategory("Transportation");
        this.setActivity("Flight");
        if (length.equals("Short")) {
            this.emission = frequency * 225;
        } else {
            this.emission = frequency * 825;
        }
        this.frequency = frequency;
        this.length = length;
    }
}

class Food extends Breakdown {
    String type;
    int serving;

    public Food() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public Food(String type, int serving) {
        this.setCategory("Food");
        this.setActivity("Meal");
        double perServing;
        if (type.equals("Beef")) {
            perServing = 6.9;
        } else if (type.equals("Pork")) {
            perServing = 4.0;
        } else if (type.equals("Chicken")) {
            perServing = 2.6;
        } else if (type.equals("Fist")) {
            perServing = 2.1;
        } else {
            perServing = 0.9;
        }
        this.emission = (double)serving * perServing;
        this.type = type;
        this.serving = serving;
    }
}

class Clothes extends Breakdown {
    int amount;

    public Clothes() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Clothes(int amount) {
        this.setCategory("Consumption");
        this.setActivity("Clothes");
        this.emission = amount;
        this.amount = amount;
    }
}

class Electronics extends Breakdown {
    String type;
    int amount;

    public Electronics() {
    }

    public String getType() {
        return type;
    }

    public void setType() {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Electronics(String type, int amount) {
        this.setCategory("Consumption");
        this.setActivity("Electronics");
        this.emission = 300 * amount;
        this.type = type;
        this.amount = amount;
    }
}

class OtherPurchases extends Breakdown {
    String type;
    int amount;

    public OtherPurchases() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public OtherPurchases(String type, int amount) {
        this.setCategory("Consumption");
        this.setActivity("Other Purchases");
        this.emission = 500 * amount;
        this.type = type;
        this.amount = amount;
    }
}

class EnergyBills extends Breakdown {
    String type;
    int bill;

    public EnergyBills() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public EnergyBills(String type, int bill) {
        this.setCategory("Consumption");
        this.setActivity("Energy Bills");
        double perDollar;
        if (type.equals("Electricity")) {
            perDollar = 4;
        } else if (type.equals("Gas")) {
            perDollar = 48;
        } else {
            perDollar = 0.31;
        }
        this.emission = perDollar * bill;
        this.type = type;
        this.bill = bill;
    }
}



