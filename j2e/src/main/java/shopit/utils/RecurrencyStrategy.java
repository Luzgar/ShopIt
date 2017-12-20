package shopit.utils;

/**
 * Created by Charly on 13/06/2017.
 */
public enum RecurrencyStrategy
{
    HIGHEST_OCCURRENCE("highest_occurrence");

    private String name;

    public String getName() {return name;}

    RecurrencyStrategy(String name) {this.name = name;}
}
