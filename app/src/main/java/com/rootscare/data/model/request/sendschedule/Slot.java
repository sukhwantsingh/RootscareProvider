
package com.rootscare.data.model.request.sendschedule;

 import androidx.annotation.Keep;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Keep
 public class Slot {
    public Slot(String slotDay, String slotStartTime, String slotEndTime, String slotDayEnable) {
       this.slotDay = slotDay;
       this.slotStartTime = slotStartTime;
       this.slotEndTime = slotEndTime;
       this.slotDayEnable = slotDayEnable;
    }

    @SerializedName("slot_day")
    @Expose
    public String slotDay;
    @SerializedName("slot_start_time")
    @Expose
    public String slotStartTime;
    @SerializedName("slot_end_time")
    @Expose
    public String slotEndTime;
    @SerializedName("slot_day_enable")
    @Expose
    public String slotDayEnable;

}
