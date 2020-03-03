package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A Coffee Event is an Class constructed with two timestamps.
 * It uses these timestamps to make different strings, events, and things about the date.
 * That's all!
 */
public class CoffeeEvent implements Comparable{
    private Long startTime;
    private Long endTime;
    public CoffeeEvent(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns a date string like "Monday, March 2, 2020 at 4 PM"
     * @param timeStamp the timestamp in which this is built
     * @return example like: "Monday, March 2, 2020 at 4 PM"
     */
    private String getFullDateString(Long timeStamp){
        SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE", Locale.US);
        SimpleDateFormat dateTime = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.US);

        String dayOfWeek = weekDayFormat.format(timeStamp);
        String dateTimeString = dateTime.format(timeStamp);

        return dayOfWeek + " " + dateTimeString;
    }

    private String getOnlyDateString(Long timestamp){
        SimpleDateFormat dateTime = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return dateTime.format(timestamp);
    }

    private String getOnlyTimeString(Long timestamp){
        SimpleDateFormat timeString = new SimpleDateFormat("h:mm:ss a", Locale.US);
        return timeString.format(timestamp);
    }

    public String getStartTimeString(){
        return getFullDateString(this.startTime);
    }

    public String getEndTimeString(){
        return getFullDateString(this.endTime);
    }

    public String getStartDateOnly(){
        return getOnlyDateString(this.startTime);
    }

    public String getEndDateOnly(){
        return getOnlyDateString(this.endTime);
    }

    public String getStartTimeOnly(){
        return getOnlyTimeString(this.startTime);
    }

    public String getEndTimeOnly(){
        return getOnlyTimeString(this.endTime);
    }

    @Override
    public int compareTo(Object o) {
        return this.startTime.compareTo(((CoffeeEvent) o).startTime);
    }
}
