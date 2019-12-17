
package com.esp8266.location.mapMyIndia.safemate;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SafeMateDevice {

    @SerializedName("functions_stamps_total_counter")
    @Expose
    private Long functionsStampsTotalCounter;
    @SerializedName("positions")
    @Expose
    private List<Position> positions = null;

    public Long getFunctionsStampsTotalCounter() {
        return functionsStampsTotalCounter;
    }

    public void setFunctionsStampsTotalCounter(Long functionsStampsTotalCounter) {
        this.functionsStampsTotalCounter = functionsStampsTotalCounter;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

}
