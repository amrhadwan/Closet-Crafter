package com.example.signuploginfirebase;

import java.util.ArrayList;
import java.util.List;

public class Outfit {
    // In Outfit.java
    private String outfitName;
    private List<String> outfitItems;
        private String outfitId;
        private String userId;
        private ArrayList<String> itemIds; // List of Item IDs in the outfit

    public Outfit(String outfitId, String outfitName, String imageUrl, String uid) {
    }

    public String getOutfitName() {
        return outfitName;
    }

    public List<String> getOutfitItems() {
        return outfitItems;
    }

    public void setOutfitItems(List<String> outfitItems) {
        this.outfitItems = outfitItems;
    }

    public void setOutfitName(String outfitName) {
        this.outfitName = outfitName;
    }

    public void setUserId(String uid) {
        this.userId=uid;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<String> getItemIds() {
        return itemIds;
    }

    public String getOutfitId() {
        return outfitId;
    }

    public void setOutfitId(String outfitId) {
        this.outfitId = outfitId;
    }

    public void setItemIds(ArrayList<String> itemIds) {
        this.itemIds = itemIds;
    }
    // Constructors, getters, setters
        // ...
    }


