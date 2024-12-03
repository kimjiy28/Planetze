package com.example.planetze;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DailyEmission {
   int dailyEmission;
   ArrayList<Breakdown> activities;

   public DailyEmission() {

   }

   public DailyEmission (int dailyEmission, ArrayList<Breakdown> activities) {
       // calculate total emission
       this.dailyEmission = dailyEmission;
       this.activities = activities;
   }

   public int getDailyEmission() {
       return dailyEmission;
   }

   public void setDailyEmission(int dailyEmission) {
       this.dailyEmission = dailyEmission;
   }

   public ArrayList<Breakdown> getActivities() {
       return activities;
   }

   public void setActivities(ArrayList<Breakdown> activities) {
       this.activities = activities;
   }

}