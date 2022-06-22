
package com.rootscare.data.model.response.fetchscheduleresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slot {

    @SerializedName("slot_day")
    @Expose
    public String slotDay;
    @SerializedName("slot_day_enable")
    @Expose
    public String slotDayEnable;
    @SerializedName("slot_start_time")
    @Expose
    public String slotStartTime;
    @SerializedName("slot_end_time")
    @Expose
    public String slotEndTime;

}
