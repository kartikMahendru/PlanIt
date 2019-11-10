package com.example.kartik.foodmanagement;

public class Food {

    private String description;
    private double latitude;
    private double longitude;
    private String suffFor;
    private String url;
    //private String mobileNumber;
    public String getSuffFor() {
        return suffFor;
    }

    public void setSuffFor(String suffFor) {
        this.suffFor = suffFor;
    }

    //public String getMobileNumber() {
       // return mobileNumber;
    //}//

    //public void setMobileNumber(String mobileNumber) {
      //  this.mobileNumber = mobileNumber;
    //}

    public Food(){
        //empty
    }

    public Food(String description, double latitude, double longitude,String suffFor,String url)
    {
        if(description.trim().equals("")){
            description = "No Name";
        }
        this.description = description;
        this.suffFor = suffFor;
        this.latitude=latitude;
        this.longitude=longitude;
        this.url=url;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
