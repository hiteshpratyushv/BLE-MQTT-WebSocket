package com.sendrecv.ble.blesendandrecieve;

public class Device {
    private String name;
    private int rssi;
    private String mac;

    public Device(String name, int rssi, String mac) {
        this.name = name;
        this.rssi = rssi;
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public int getRSSI() {
        return rssi;
    }

    public String getMac() {
        return mac;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRSSI(int rssi) {
        this.rssi = rssi;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
