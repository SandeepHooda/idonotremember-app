package health.vo;

public class Device {
	private String _id;
	private String _import_id;
	private int displayOrder;
	private String name;
	private long healthCheckTime;
	private long lastAlarmTime;
	private long heatBeatTimeout =30000;
	private boolean turnOnHealthCheck;
	private boolean alarmTriggered;
	private String deviceType ;//camera, doorSensor
	private String alermNotificationText = "There is an suspicious activity on house number 55 Sector 27. Please call police to check.";
	private boolean alertType_emailOnly;
	private boolean disabledHelthCheckbox;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getHealthCheckTime() {
		return healthCheckTime;
	}
	public void setHealthCheckTime(long healthCheckTime) {
		this.healthCheckTime = healthCheckTime;
	}
	public boolean isTurnOnHealthCheck() {
		return turnOnHealthCheck;
	}
	public void setTurnOnHealthCheck(boolean turnOnHealthCheck) {
		this.turnOnHealthCheck = turnOnHealthCheck;
	}
	public boolean isAlarmTriggered() {
		return alarmTriggered;
	}
	public void setAlarmTriggered(boolean alarmTriggered) {
		this.alarmTriggered = alarmTriggered;
	}
	public String getAlermNotificationText() {
		return alermNotificationText;
	}
	public void setAlermNotificationText(String alermNotificationText) {
		this.alermNotificationText = alermNotificationText;
	}
	public long getLastAlarmTime() {
		return lastAlarmTime;
	}
	public void setLastAlarmTime(long lastAlarmTime) {
		this.lastAlarmTime = lastAlarmTime;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public long getHeatBeatTimeout() {
		return heatBeatTimeout;
	}
	public void setHeatBeatTimeout(long heatBeatTimeout) {
		this.heatBeatTimeout = heatBeatTimeout;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public boolean isAlertType_emailOnly() {
		return alertType_emailOnly;
	}
	public void setAlertType_emailOnly(boolean alertType_emailOnly) {
		this.alertType_emailOnly = alertType_emailOnly;
	}
	public boolean isDisabledHelthCheckbox() {
		return disabledHelthCheckbox;
	}
	public void setDisabledHelthCheckbox(boolean disabledHelthCheckbox) {
		this.disabledHelthCheckbox = disabledHelthCheckbox;
	}
	public String get_import_id() {
		return _import_id;
	}
	public void set_import_id(String _import_id) {
		this._import_id = _import_id;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}

}
