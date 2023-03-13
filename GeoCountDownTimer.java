import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;   
import java.io.IOException;
import java.util.Scanner;  

/*****************************************************************
@author Steven Streasick
@version Winter 2022
*****************************************************************/
public class GeoCountDownTimer {

    private int month; //Valid between 1 and 12
    private int year; //Valid between years inf >= 2022
    private int day; //Valid values between 1 and last day of month.

    private static final int MIN_YEAR = 2022;

    public GeoCountDownTimer() {
        this.month = 1;
        this.year = MIN_YEAR;
        this.day = 1;
    }

    public GeoCountDownTimer(int year, int month, int day) {
        if(isValidDate(year, month, day)) {
            this.month = month;
            this.day = day;
            this.year = year;
        } else {
            throw new IllegalArgumentException();
        }
    }
   
    public GeoCountDownTimer(GeoCountDownTimer other) {
        if (other != null) {
            if(isValidDate(other.year, other.month, other.day)) {
                this.month = other.month;
                this.day = other.day;
                this.year = other.year;
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public GeoCountDownTimer(String geoDate) {
        if (isStringValidDate(geoDate)) {
            String[] date =  geoDate.split("/"); //Month, day, year

            if ((date.length == 3)) {
                int newMonth = Integer.parseInt(date[0]);
                int newDay = Integer.parseInt(date[1]);
                int newYear = Integer.parseInt(date[2]);
                if (isValidDate(newYear, newMonth, newDay)) {
                    this.day = newDay;
                    this.month = newMonth;
                    this.year = newYear;
                } else {
                    throw new IllegalArgumentException();
                }
            } else{
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /** Determines whether a string contains a date or not. Note: Does not check if a date is valid, just if a string contains a date.
     * @param string
     * @return boolean
     */
    private boolean isStringValidDate(String string) {
        boolean validity = true;

        if(string == null) {
            validity = false;
            return validity;
        }
        char[] acceptableCharacters = {'/', '0', '1','2','3','4','5','6','7','8','9'};
        for(int i = 0; i < string.length(); i++) {
            boolean bool = true;
            for( int i_ = 0; i_ < acceptableCharacters.length; i_++) {
                if(string.charAt(i) == acceptableCharacters[i_]) {
                    bool = false;
                }
            }
            if(bool) {
                validity = false; 
                return validity;
            } 
        }
        return validity;
    }
    
    /** Determines whether a date is valid, especially useful for determining a gap year.
     * @param year
     * @param month
     * @param day
     * @return boolean
     */
    private boolean isValidDate(int year, int month, int day) {
        final int[] daysInMonths = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean leapYear = false;

        if(year % 4 == 0) {
            leapYear = true;
            if(year % 400 == 0) {
                leapYear = true;
            }
            else if(year % 100 == 0) {
                leapYear = false;
            }
        }
        if (leapYear) {
            daysInMonths[2] = 29;
        } else {
            daysInMonths[2] = 28;
        }
        if (year < MIN_YEAR) {
            return false;
        }
        if(month <=0 || month > 12) {
            return false;
        }
        if(day <= 0 || day > daysInMonths[month]){
            return false;
        }

        return true;
    }
    
    /** Handles dates that should not exist.
     * @param date
     * @param 29
     * @return int[]
     */
    private int[] handleDateOverflow(int[] date){ //Date: Year, month, day
        final int[] daysInMonths = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int year = date[0];
        int month = date[1];
        int day = date[2];

        if(isValidDate(year, 2, 29)) {
            daysInMonths[2] = 29;
        } else {
            daysInMonths[2] = 28;
        }

        if(day > daysInMonths[month]) { 
            if( month != 12) {
                month++;
                day = 1;
            }else {
                month = 1;
                day = 1;
                year ++;
            }
        } 

        if(day == 0) {
            if( month > 1) {
                month--;
                day = daysInMonths[month];
                
            } else if(month <= 1) {
                month = 12;
                day = daysInMonths[month]; 
                year --;
            }
        } 

        int[] newDate = {year, month, day};
        return newDate;
    }

    
    /** Determines whether or not the current date is equal to another date.
     * @param other
     * @return boolean
     */
    public boolean equals(Object other) {
        if( other instanceof GeoCountDownTimer) {
            GeoCountDownTimer otherTimer = (GeoCountDownTimer) other;
       
            return ((this.month == otherTimer.month) && (this.year == otherTimer.year) &&
                (this.day == otherTimer.day));
   
        } else {
            throw new IllegalArgumentException();
        }
    }

    
    /** Determines whether the current date is larger, smaller, or equal to another date.
     * @param other
     * @return int
     */
    public int compareTo(GeoCountDownTimer other) {
        if( other == null) {
            throw new IllegalArgumentException();
        }
        int returnVal = 0;
        GeoCountDownTimer otherTimer = (GeoCountDownTimer) other;

        if(otherTimer.year <  this.year) {
            returnVal = 1;
            return returnVal;
        }
        if(otherTimer.year > this.year) {
            returnVal = -1;
            return returnVal;
        }
        if(otherTimer.month < this.month) {
            returnVal = 1;
            return returnVal;
        }
        if(otherTimer.month > this.month){
            returnVal = -1;
            return returnVal;
        }
        
        if(otherTimer.day < this.day) {
            returnVal = 1;
            return returnVal;
        }

        if(otherTimer.day > this.day) {
            returnVal = -1;
            return returnVal;
        }

        return returnVal;
    }

 
    
    /** Decreases the day by a certain amount.
     * @param days
     */
    public void dec(int days) {
        if(days < 0) {
            throw new IllegalArgumentException();
        }

        while (days > 0) {
            int[] date = {year, month, day -= 1 };
            int[] newDate = handleDateOverflow(date);

            days--;

            year = newDate[0];
            month = newDate[1];
            day = newDate[2];
        }
    }

    
    /** Increments the day by a certain amount.
     * @param days
     */
    public void inc(int days) {
        final int[] daysInMonths = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if(days < 0) {
            throw new IllegalArgumentException();
        }
        while (days > 0) { 
            
            if(isValidDate(year, 2, 29)) {
                daysInMonths[2] = 29;
            } else {
                daysInMonths[2] = 28;
            }
            
            int[] date = {year, month, day += 1};
            int[] newDate = handleDateOverflow(date);

            days --;

            year = newDate[0];
            month = newDate[1];
            day = newDate[2];
        }
    }


    
    /** Counts down from one date to another, determining the difference.
     * @param fromDate
     * @return int
     */
    public int daysToGo(String fromDate) {
        int dayCount = 0;

        if(isStringValidDate(fromDate)) {

            String[] date =  fromDate.split("/"); //Month, day, year
                        
            if ((date.length == 3)) {
                int passedMonth = Integer.parseInt(date[0]);
                int passedDay = Integer.parseInt(date[1]);
                int passedYear = Integer.parseInt(date[2]);

                GeoCountDownTimer fromDateTimer = new GeoCountDownTimer(passedYear, passedMonth, passedDay);
                if(compareTo(fromDateTimer) != 0) {
                    while (true) {
                        int fromTimer = compareTo(fromDateTimer);
                        if (this.month == passedMonth && this.day == passedDay && this.year == passedYear) {
                            break;
                        }
                        
                        int[] param = {passedYear, passedMonth, passedDay += fromTimer};
                        int[] newDate = handleDateOverflow(param);

                        dayCount += fromTimer;

                        passedYear = newDate[0];
                        passedMonth = newDate[1];
                        passedDay = newDate[2];
                    }
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
        
        return dayCount;
    }

    
    /** Determines what the day should be given a certain number of days in the future from the current day.
     * @param n
     * @return GeoCountDownTimer
     */
    public GeoCountDownTimer daysInFuture(int n) {
        int year = this.year;
        int month = this.month;
        int day = this.day;
        int i = 0;

        while (i != n) {
            int increment = (n / Math.abs(n));
            int[] param = {year, month, day += increment};
            int[] newDate = handleDateOverflow(param);

            year = newDate[0];
            month = newDate[1];
            day = newDate[2];
            i += increment; 
        }
        GeoCountDownTimer timer = new GeoCountDownTimer(year, month, day);

        return timer;
    }

    /** Increases the day by one.
     * @return int
     */
    public void inc() {
        inc(1);
    }

    /** Decreases the day by one.
     * 
     */
    public void dec() {
        dec(1);
    }

    
    /** Returns the day
     * @return int
     */
    public int getDay() {
        return day;
    }
    
    /** Returns the month
     * @return int
     */
    public int getMonth() {
        return month;
    }

    
    /** Returns the year
     * @return int
     */
    public int getYear() {
        return year;
    }

    
    /** Sets the day
     * @param day
     */
    public void setDay(int day) {
        if(isValidDate(year, month, day)) {
            this.day = day;
        } else {
            throw new IllegalArgumentException();
        }
        
    } 
    
    
    /** Sets the month
     * @param month
     */
    public void setMonth(int month) {
        if(isValidDate(year, month, day)) {
            this.month = month;
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /** Sets the year
     * @param year
     */
    public void setYear(int year) {
        
        if(isValidDate(year, month, day)) {
            this.year = year;
        } else {
            throw new IllegalArgumentException();
        }

    }
    
    /** The string to be printed
     * @return String
     */
    public String toString() {
        //February 10, 2119
        final String[] months = {"", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        String string = new String("");

        string += months[this.month];
        string += " " + this.day;
        string += ", " + this.year;
        return string;
    }

    
    /** Saves the month, day, and year into a file
     * @param fileName
     */
    public void save(String fileName) {
        try {
            String newString = month + " " + day + " " + year;
            FileWriter write = new FileWriter(fileName);

            write.write(newString); //2 28 2022   Month, day, year
            write.close();
        } catch (IOException e) {

        }
    }

    
    /** Loads the variables with the information stored within the file.
     * @param fileName
     */
    public void load(String fileName) { 
        try {
            File file = new File(fileName);
            Scanner scnr = new Scanner(file);
            String str = scnr.nextLine();
            String[] date =  str.split(" ");

            scnr.close();            
            if(date.length == 3) {
                int day = Integer.parseInt(date[1]);
                int month = Integer.parseInt(date[0]);
                int year = Integer.parseInt(date[2]);

                if(isValidDate(year, month, day)) {

                    this.day = day;
                    this.month = month;
                    this.year = year;

                }else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
        } catch(FileNotFoundException e) {

        }
    }

    
    /** Returns a string that is formatted in a typical date format.
     * @return String
     */
    public String toDateString() {
        String string = "";
        
        string += this.month + "/";
        string += this.day + "/";
        string += this.year;
        return string;
    }
}
