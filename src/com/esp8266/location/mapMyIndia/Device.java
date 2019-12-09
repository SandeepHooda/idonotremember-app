
package com.esp8266.location.mapMyIndia;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entityId",
    "latitude",
    "longitude",
    "gprsTime",
    "heading",
    "mainPower",
    "address",
    "dtcCount",
    "dtcDistance",
    "greenDriveType",
    "greenDriveValue",
    "altitude",
    "coolantTemp",
    "engineRPM",
    "gpsSignal",
    "internalBatteryVolt",
    "internalBatteryLevel",
    "satellites",
    "canOdometer",
    "gpsOdometer",
    "canTimestamp",
    "gearState",
    "stateOfCharge",
    "distanceToEmpty",
    "ignition",
    "power",
    "accelPedal",
    "parkBrake",
    "breakPedal",
    "fuelLevel",
    "driverDoor",
    "passDoor",
    "headLights",
    "blinker",
    "deviceOdometer",
    "intakeAirTemp",
    "intakeabsolutePress",
    "airFlowRate",
    "clutchPedal",
    "mainFoldPress",
    "ctrlModleVol",
    "errorCodes",
    "atapahMeterData",
    "atapahUIEvent",
    "deviceId",
    "timestamp",
    "speedkph",
    "engineStatus",
    "panic",
    "digitalInput3",
    "digitalOutput1",
    "digitalOutput2"
})
public class Device {

    @JsonProperty("entityId")
    private Integer entityId;
    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;
    @JsonProperty("gprsTime")
    private Integer gprsTime;
    @JsonProperty("heading")
    private Double heading;
    @JsonProperty("mainPower")
    private Object mainPower;
    @JsonProperty("address")
    private String address;
    @JsonProperty("dtcCount")
    private Object dtcCount;
    @JsonProperty("dtcDistance")
    private Object dtcDistance;
    @JsonProperty("greenDriveType")
    private Object greenDriveType;
    @JsonProperty("greenDriveValue")
    private Object greenDriveValue;
    @JsonProperty("altitude")
    private Double altitude;
    @JsonProperty("coolantTemp")
    private Object coolantTemp;
    @JsonProperty("engineRPM")
    private Object engineRPM;
    @JsonProperty("gpsSignal")
    private Integer gpsSignal;
    @JsonProperty("internalBatteryVolt")
    private Double internalBatteryVolt;
    @JsonProperty("internalBatteryLevel")
    private Integer internalBatteryLevel;
    @JsonProperty("satellites")
    private Integer satellites;
    @JsonProperty("canOdometer")
    private Object canOdometer;
    @JsonProperty("gpsOdometer")
    private Object gpsOdometer;
    @JsonProperty("canTimestamp")
    private Object canTimestamp;
    @JsonProperty("gearState")
    private Object gearState;
    @JsonProperty("stateOfCharge")
    private Object stateOfCharge;
    @JsonProperty("distanceToEmpty")
    private Object distanceToEmpty;
    @JsonProperty("ignition")
    private Integer ignition;
    @JsonProperty("power")
    private Object power;
    @JsonProperty("accelPedal")
    private Object accelPedal;
    @JsonProperty("parkBrake")
    private Object parkBrake;
    @JsonProperty("breakPedal")
    private Object breakPedal;
    @JsonProperty("fuelLevel")
    private Object fuelLevel;
    @JsonProperty("driverDoor")
    private Object driverDoor;
    @JsonProperty("passDoor")
    private Object passDoor;
    @JsonProperty("headLights")
    private Object headLights;
    @JsonProperty("blinker")
    private Object blinker;
    @JsonProperty("deviceOdometer")
    private Object deviceOdometer;
    @JsonProperty("intakeAirTemp")
    private Object intakeAirTemp;
    @JsonProperty("intakeabsolutePress")
    private Object intakeabsolutePress;
    @JsonProperty("airFlowRate")
    private Object airFlowRate;
    @JsonProperty("clutchPedal")
    private Object clutchPedal;
    @JsonProperty("mainFoldPress")
    private Object mainFoldPress;
    @JsonProperty("ctrlModleVol")
    private Object ctrlModleVol;
    @JsonProperty("errorCodes")
    private Object errorCodes;
    @JsonProperty("atapahMeterData")
    private Object atapahMeterData;
    @JsonProperty("atapahUIEvent")
    private Object atapahUIEvent;
    @JsonProperty("deviceId")
    private Integer deviceId;
    @JsonProperty("timestamp")
    private Integer timestamp;
    @JsonProperty("speedkph")
    private Double speedkph;
    @JsonProperty("engineStatus")
    private Integer engineStatus;
    @JsonProperty("panic")
    private Integer panic;
    @JsonProperty("digitalInput3")
    private Integer digitalInput3;
    @JsonProperty("digitalOutput1")
    private Object digitalOutput1;
    @JsonProperty("digitalOutput2")
    private Object digitalOutput2;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("entityId")
    public Integer getEntityId() {
        return entityId;
    }

    @JsonProperty("entityId")
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("gprsTime")
    public Integer getGprsTime() {
        return gprsTime;
    }

    @JsonProperty("gprsTime")
    public void setGprsTime(Integer gprsTime) {
        this.gprsTime = gprsTime;
    }

    @JsonProperty("heading")
    public Double getHeading() {
        return heading;
    }

    @JsonProperty("heading")
    public void setHeading(Double heading) {
        this.heading = heading;
    }

    @JsonProperty("mainPower")
    public Object getMainPower() {
        return mainPower;
    }

    @JsonProperty("mainPower")
    public void setMainPower(Object mainPower) {
        this.mainPower = mainPower;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("dtcCount")
    public Object getDtcCount() {
        return dtcCount;
    }

    @JsonProperty("dtcCount")
    public void setDtcCount(Object dtcCount) {
        this.dtcCount = dtcCount;
    }

    @JsonProperty("dtcDistance")
    public Object getDtcDistance() {
        return dtcDistance;
    }

    @JsonProperty("dtcDistance")
    public void setDtcDistance(Object dtcDistance) {
        this.dtcDistance = dtcDistance;
    }

    @JsonProperty("greenDriveType")
    public Object getGreenDriveType() {
        return greenDriveType;
    }

    @JsonProperty("greenDriveType")
    public void setGreenDriveType(Object greenDriveType) {
        this.greenDriveType = greenDriveType;
    }

    @JsonProperty("greenDriveValue")
    public Object getGreenDriveValue() {
        return greenDriveValue;
    }

    @JsonProperty("greenDriveValue")
    public void setGreenDriveValue(Object greenDriveValue) {
        this.greenDriveValue = greenDriveValue;
    }

    @JsonProperty("altitude")
    public Double getAltitude() {
        return altitude;
    }

    @JsonProperty("altitude")
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @JsonProperty("coolantTemp")
    public Object getCoolantTemp() {
        return coolantTemp;
    }

    @JsonProperty("coolantTemp")
    public void setCoolantTemp(Object coolantTemp) {
        this.coolantTemp = coolantTemp;
    }

    @JsonProperty("engineRPM")
    public Object getEngineRPM() {
        return engineRPM;
    }

    @JsonProperty("engineRPM")
    public void setEngineRPM(Object engineRPM) {
        this.engineRPM = engineRPM;
    }

    @JsonProperty("gpsSignal")
    public Integer getGpsSignal() {
        return gpsSignal;
    }

    @JsonProperty("gpsSignal")
    public void setGpsSignal(Integer gpsSignal) {
        this.gpsSignal = gpsSignal;
    }

    @JsonProperty("internalBatteryVolt")
    public Double getInternalBatteryVolt() {
        return internalBatteryVolt;
    }

    @JsonProperty("internalBatteryVolt")
    public void setInternalBatteryVolt(Double internalBatteryVolt) {
        this.internalBatteryVolt = internalBatteryVolt;
    }

    @JsonProperty("internalBatteryLevel")
    public Integer getInternalBatteryLevel() {
        return internalBatteryLevel;
    }

    @JsonProperty("internalBatteryLevel")
    public void setInternalBatteryLevel(Integer internalBatteryLevel) {
        this.internalBatteryLevel = internalBatteryLevel;
    }

    @JsonProperty("satellites")
    public Integer getSatellites() {
        return satellites;
    }

    @JsonProperty("satellites")
    public void setSatellites(Integer satellites) {
        this.satellites = satellites;
    }

    @JsonProperty("canOdometer")
    public Object getCanOdometer() {
        return canOdometer;
    }

    @JsonProperty("canOdometer")
    public void setCanOdometer(Object canOdometer) {
        this.canOdometer = canOdometer;
    }

    @JsonProperty("gpsOdometer")
    public Object getGpsOdometer() {
        return gpsOdometer;
    }

    @JsonProperty("gpsOdometer")
    public void setGpsOdometer(Object gpsOdometer) {
        this.gpsOdometer = gpsOdometer;
    }

    @JsonProperty("canTimestamp")
    public Object getCanTimestamp() {
        return canTimestamp;
    }

    @JsonProperty("canTimestamp")
    public void setCanTimestamp(Object canTimestamp) {
        this.canTimestamp = canTimestamp;
    }

    @JsonProperty("gearState")
    public Object getGearState() {
        return gearState;
    }

    @JsonProperty("gearState")
    public void setGearState(Object gearState) {
        this.gearState = gearState;
    }

    @JsonProperty("stateOfCharge")
    public Object getStateOfCharge() {
        return stateOfCharge;
    }

    @JsonProperty("stateOfCharge")
    public void setStateOfCharge(Object stateOfCharge) {
        this.stateOfCharge = stateOfCharge;
    }

    @JsonProperty("distanceToEmpty")
    public Object getDistanceToEmpty() {
        return distanceToEmpty;
    }

    @JsonProperty("distanceToEmpty")
    public void setDistanceToEmpty(Object distanceToEmpty) {
        this.distanceToEmpty = distanceToEmpty;
    }

    @JsonProperty("ignition")
    public Integer getIgnition() {
        return ignition;
    }

    @JsonProperty("ignition")
    public void setIgnition(Integer ignition) {
        this.ignition = ignition;
    }

    @JsonProperty("power")
    public Object getPower() {
        return power;
    }

    @JsonProperty("power")
    public void setPower(Object power) {
        this.power = power;
    }

    @JsonProperty("accelPedal")
    public Object getAccelPedal() {
        return accelPedal;
    }

    @JsonProperty("accelPedal")
    public void setAccelPedal(Object accelPedal) {
        this.accelPedal = accelPedal;
    }

    @JsonProperty("parkBrake")
    public Object getParkBrake() {
        return parkBrake;
    }

    @JsonProperty("parkBrake")
    public void setParkBrake(Object parkBrake) {
        this.parkBrake = parkBrake;
    }

    @JsonProperty("breakPedal")
    public Object getBreakPedal() {
        return breakPedal;
    }

    @JsonProperty("breakPedal")
    public void setBreakPedal(Object breakPedal) {
        this.breakPedal = breakPedal;
    }

    @JsonProperty("fuelLevel")
    public Object getFuelLevel() {
        return fuelLevel;
    }

    @JsonProperty("fuelLevel")
    public void setFuelLevel(Object fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    @JsonProperty("driverDoor")
    public Object getDriverDoor() {
        return driverDoor;
    }

    @JsonProperty("driverDoor")
    public void setDriverDoor(Object driverDoor) {
        this.driverDoor = driverDoor;
    }

    @JsonProperty("passDoor")
    public Object getPassDoor() {
        return passDoor;
    }

    @JsonProperty("passDoor")
    public void setPassDoor(Object passDoor) {
        this.passDoor = passDoor;
    }

    @JsonProperty("headLights")
    public Object getHeadLights() {
        return headLights;
    }

    @JsonProperty("headLights")
    public void setHeadLights(Object headLights) {
        this.headLights = headLights;
    }

    @JsonProperty("blinker")
    public Object getBlinker() {
        return blinker;
    }

    @JsonProperty("blinker")
    public void setBlinker(Object blinker) {
        this.blinker = blinker;
    }

    @JsonProperty("deviceOdometer")
    public Object getDeviceOdometer() {
        return deviceOdometer;
    }

    @JsonProperty("deviceOdometer")
    public void setDeviceOdometer(Object deviceOdometer) {
        this.deviceOdometer = deviceOdometer;
    }

    @JsonProperty("intakeAirTemp")
    public Object getIntakeAirTemp() {
        return intakeAirTemp;
    }

    @JsonProperty("intakeAirTemp")
    public void setIntakeAirTemp(Object intakeAirTemp) {
        this.intakeAirTemp = intakeAirTemp;
    }

    @JsonProperty("intakeabsolutePress")
    public Object getIntakeabsolutePress() {
        return intakeabsolutePress;
    }

    @JsonProperty("intakeabsolutePress")
    public void setIntakeabsolutePress(Object intakeabsolutePress) {
        this.intakeabsolutePress = intakeabsolutePress;
    }

    @JsonProperty("airFlowRate")
    public Object getAirFlowRate() {
        return airFlowRate;
    }

    @JsonProperty("airFlowRate")
    public void setAirFlowRate(Object airFlowRate) {
        this.airFlowRate = airFlowRate;
    }

    @JsonProperty("clutchPedal")
    public Object getClutchPedal() {
        return clutchPedal;
    }

    @JsonProperty("clutchPedal")
    public void setClutchPedal(Object clutchPedal) {
        this.clutchPedal = clutchPedal;
    }

    @JsonProperty("mainFoldPress")
    public Object getMainFoldPress() {
        return mainFoldPress;
    }

    @JsonProperty("mainFoldPress")
    public void setMainFoldPress(Object mainFoldPress) {
        this.mainFoldPress = mainFoldPress;
    }

    @JsonProperty("ctrlModleVol")
    public Object getCtrlModleVol() {
        return ctrlModleVol;
    }

    @JsonProperty("ctrlModleVol")
    public void setCtrlModleVol(Object ctrlModleVol) {
        this.ctrlModleVol = ctrlModleVol;
    }

    @JsonProperty("errorCodes")
    public Object getErrorCodes() {
        return errorCodes;
    }

    @JsonProperty("errorCodes")
    public void setErrorCodes(Object errorCodes) {
        this.errorCodes = errorCodes;
    }

    @JsonProperty("atapahMeterData")
    public Object getAtapahMeterData() {
        return atapahMeterData;
    }

    @JsonProperty("atapahMeterData")
    public void setAtapahMeterData(Object atapahMeterData) {
        this.atapahMeterData = atapahMeterData;
    }

    @JsonProperty("atapahUIEvent")
    public Object getAtapahUIEvent() {
        return atapahUIEvent;
    }

    @JsonProperty("atapahUIEvent")
    public void setAtapahUIEvent(Object atapahUIEvent) {
        this.atapahUIEvent = atapahUIEvent;
    }

    @JsonProperty("deviceId")
    public Integer getDeviceId() {
        return deviceId;
    }

    @JsonProperty("deviceId")
    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("timestamp")
    public Integer getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("speedkph")
    public Double getSpeedkph() {
        return speedkph;
    }

    @JsonProperty("speedkph")
    public void setSpeedkph(Double speedkph) {
        this.speedkph = speedkph;
    }

    @JsonProperty("engineStatus")
    public Integer getEngineStatus() {
        return engineStatus;
    }

    @JsonProperty("engineStatus")
    public void setEngineStatus(Integer engineStatus) {
        this.engineStatus = engineStatus;
    }

    @JsonProperty("panic")
    public Integer getPanic() {
        return panic;
    }

    @JsonProperty("panic")
    public void setPanic(Integer panic) {
        this.panic = panic;
    }

    @JsonProperty("digitalInput3")
    public Integer getDigitalInput3() {
        return digitalInput3;
    }

    @JsonProperty("digitalInput3")
    public void setDigitalInput3(Integer digitalInput3) {
        this.digitalInput3 = digitalInput3;
    }

    @JsonProperty("digitalOutput1")
    public Object getDigitalOutput1() {
        return digitalOutput1;
    }

    @JsonProperty("digitalOutput1")
    public void setDigitalOutput1(Object digitalOutput1) {
        this.digitalOutput1 = digitalOutput1;
    }

    @JsonProperty("digitalOutput2")
    public Object getDigitalOutput2() {
        return digitalOutput2;
    }

    @JsonProperty("digitalOutput2")
    public void setDigitalOutput2(Object digitalOutput2) {
        this.digitalOutput2 = digitalOutput2;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
