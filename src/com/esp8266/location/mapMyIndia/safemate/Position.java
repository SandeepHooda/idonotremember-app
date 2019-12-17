
package com.esp8266.location.mapMyIndia.safemate;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Position {

    @SerializedName("gprs_status")
    @Expose
    private Boolean gprsStatus;
    @SerializedName("gps_status")
    @Expose
    private Boolean gpsStatus;
    @SerializedName("engine_status")
    @Expose
    private Boolean engineStatus;
    @SerializedName("panic_time")
    @Expose
    private Long panicTime;
    @SerializedName("equipment_status")
    @Expose
    private Long equipmentStatus;
    @SerializedName("evt")
    @Expose
    private Long evt;
    @SerializedName("gprs_status_time")
    @Expose
    private Long gprsStatusTime;
    @SerializedName("gps_status_time")
    @Expose
    private Long gpsStatusTime;
    @SerializedName("engine_status_time")
    @Expose
    private Long engineStatusTime;
    @SerializedName("course")
    @Expose
    private Long course;
    @SerializedName("speed")
    @Expose
    private Long speed;
    @SerializedName("last_state_id")
    @Expose
    private Long lastStateId;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("last_alarm")
    @Expose
    private LastAlarm lastAlarm;
    @SerializedName("alarm_log")
    @Expose
    private List<Object> alarmLog = null;
    @SerializedName("current_route_state")
    @Expose
    private CurrentRouteState currentRouteState;
    @SerializedName("pt")
    @Expose
    private Pt_ pt;
    @SerializedName("battery_percent")
    @Expose
    private Long batteryPercent;
    @SerializedName("id")
    @Expose
    private String id;

    public Boolean getGprsStatus() {
        return gprsStatus;
    }

    public void setGprsStatus(Boolean gprsStatus) {
        this.gprsStatus = gprsStatus;
    }

    public Boolean getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(Boolean gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public Boolean getEngineStatus() {
        return engineStatus;
    }

    public void setEngineStatus(Boolean engineStatus) {
        this.engineStatus = engineStatus;
    }

    public Long getPanicTime() {
        return panicTime;
    }

    public void setPanicTime(Long panicTime) {
        this.panicTime = panicTime;
    }

    public Long getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(Long equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public Long getEvt() {
        return evt;
    }

    public void setEvt(Long evt) {
        this.evt = evt;
    }

    public Long getGprsStatusTime() {
        return gprsStatusTime;
    }

    public void setGprsStatusTime(Long gprsStatusTime) {
        this.gprsStatusTime = gprsStatusTime;
    }

    public Long getGpsStatusTime() {
        return gpsStatusTime;
    }

    public void setGpsStatusTime(Long gpsStatusTime) {
        this.gpsStatusTime = gpsStatusTime;
    }

    public Long getEngineStatusTime() {
        return engineStatusTime;
    }

    public void setEngineStatusTime(Long engineStatusTime) {
        this.engineStatusTime = engineStatusTime;
    }

    public Long getCourse() {
        return course;
    }

    public void setCourse(Long course) {
        this.course = course;
    }

    public Long getSpeed() {
        return speed;
    }

    public void setSpeed(Long speed) {
        this.speed = speed;
    }

    public Long getLastStateId() {
        return lastStateId;
    }

    public void setLastStateId(Long lastStateId) {
        this.lastStateId = lastStateId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LastAlarm getLastAlarm() {
        return lastAlarm;
    }

    public void setLastAlarm(LastAlarm lastAlarm) {
        this.lastAlarm = lastAlarm;
    }

    public List<Object> getAlarmLog() {
        return alarmLog;
    }

    public void setAlarmLog(List<Object> alarmLog) {
        this.alarmLog = alarmLog;
    }

    public CurrentRouteState getCurrentRouteState() {
        return currentRouteState;
    }

    public void setCurrentRouteState(CurrentRouteState currentRouteState) {
        this.currentRouteState = currentRouteState;
    }

    public Pt_ getPt() {
        return pt;
    }

    public void setPt(Pt_ pt) {
        this.pt = pt;
    }

    public Long getBatteryPercent() {
        return batteryPercent;
    }

    public void setBatteryPercent(Long batteryPercent) {
        this.batteryPercent = batteryPercent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
