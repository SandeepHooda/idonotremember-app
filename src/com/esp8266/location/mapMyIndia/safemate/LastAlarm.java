
package com.esp8266.location.mapMyIndia.safemate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastAlarm {

    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("utc_end")
    @Expose
    private Long utcEnd;
    @SerializedName("utc_start")
    @Expose
    private Long utcStart;
    @SerializedName("alarm_id")
    @Expose
    private Long alarmId;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getUtcEnd() {
        return utcEnd;
    }

    public void setUtcEnd(Long utcEnd) {
        this.utcEnd = utcEnd;
    }

    public Long getUtcStart() {
        return utcStart;
    }

    public void setUtcStart(Long utcStart) {
        this.utcStart = utcStart;
    }

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

}
