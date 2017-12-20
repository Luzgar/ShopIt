package shopit.utils;

/**
 * Created by Charly on 06/06/2017.
 */
public enum NotificationType
{
    INVITATION("list_invitation"),
    LIST_MODIFICATION("list_modification"),
    EXPULSION("list_expulsion"),
    REPARTITION("list_repartition");

    private String name;

    public String getName() {return name;}

    NotificationType(String name)
    {
        this.name = name;
    }
}
