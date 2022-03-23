package org.tanzu.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.concurrent.ThreadLocalRandom;


@Entity
public class Sensor {

    @Id
    @GeneratedValue
    int id;
    String type;
    String mood;

    public Sensor() {
        
        int rand = ThreadLocalRandom.current().nextInt(1, 4);
        if (rand == 1) {
            this.type = "Devs";
        } else if (rand == 2) {
            this.type = "AppOps";
        } else if (rand == 3) {
            this.type = "ITOps";
        }
        
        rand = ThreadLocalRandom.current().nextInt(1, 6);
        if (rand == 1) {
            this.mood = "Happy";
        } else if (rand == 2) {
            this.mood = "Sad";
        } else if (rand == 3) {
            this.mood = "Angry";
        } else if (rand == 4) {
            this.mood = "Scared";
        } else if (rand == 5) { 
            this.mood = "Depressed";
        }
    }

    public Sensor(String type, String mood) {
        this.type = type;
        this.mood = mood;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getMood() {
        return mood;
    }
    
}
