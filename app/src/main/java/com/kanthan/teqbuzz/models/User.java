package com.kanthan.teqbuzz.models;

/**
 * Created by user on 9/20/2015.
 */
public class User {

    public static final String DEFAULT_USER_ID = "-1";
    String user_id, name, email, password, phone_number, temp_latitude, temp_longitude,
            gps_latitude, gps_longitude, work_latitude, work_longitude, home_latitude, home_longitude,
            address, image_url, image_path;

    int mode, distanceRange;

    public int getDistanceRange() {
        return distanceRange;
    }

    public void setDistanceRange(int distanceRange) {
        this.distanceRange = distanceRange;
    }

    public static int NONE = 0, GPS = 1, PICKER = 2, HOME = 3, WORK = 4;

    public String getGps_latitude() {
        return gps_latitude;
    }

    public void setGps_latitude(String gps_latitude) {
        this.gps_latitude = gps_latitude;
    }

    public String getGps_longitude() {
        return gps_longitude;
    }

    public void setGps_longitude(String gps_longitude) {
        this.gps_longitude = gps_longitude;
    }

    public User(String user_id, String name, String email, String password, String phone_number, String temp_latitude, String temp_longitude, String work_latitude, String work_longitude, String latitude, String longitude, String address, String image_url, String image_path, String gps_latitude, String gps_longitude) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.gps_latitude = gps_latitude;
        this.gps_longitude = gps_longitude;
        this.temp_latitude = temp_latitude;
        this.temp_longitude = temp_longitude;
        this.work_latitude = work_latitude;
        this.work_longitude = work_longitude;
        this.home_latitude = latitude;
        this.home_longitude = longitude;
        this.address = address;
        this.image_url = image_url;
        this.image_path = image_path;

    }

    public User() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getTemp_latitude() {
        return temp_latitude;
    }

    public void setTemp_latitude(String temp_latitude) {
        this.temp_latitude = temp_latitude;
    }

    public String getTemp_longitude() {
        return temp_longitude;
    }

    public void setTemp_longitude(String temp_longitude) {
        this.temp_longitude = temp_longitude;
    }

    public String getWork_latitude() {
        return work_latitude;
    }

    public void setWork_latitude(String work_latitude) {
        this.work_latitude = work_latitude;
    }

    public String getWork_longitude() {
        return work_longitude;
    }

    public void setWork_longitude(String work_longitude) {
        this.work_longitude = work_longitude;
    }

    public String getHomeLatitude() {
        return home_latitude;
    }

    public void setHomeLatitude(String latitude) {
        this.home_latitude = latitude;
    }

    public String getHomeLongitude() {
        return home_longitude;
    }

    public void setHomeLongitude(String longitude) {
        this.home_longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
