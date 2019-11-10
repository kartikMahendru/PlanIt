package com.example.kartik.foodmanagement;

public class Organization {
        private String name,address;
        private double range,latitude,longitude;

        public Organization() {
        }

        public Organization(String address, double latitude, double longitude , String name, double range) {
            this.name = name;
            this.address = address;
            this.range = range;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String add) {
            this.address = address;
        }

        public double getRange() {
            return range;
        }

        public void setRange(double range) {
            this.range = range;
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
