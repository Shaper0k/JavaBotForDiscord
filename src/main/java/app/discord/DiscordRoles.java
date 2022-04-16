package app.discord;

import java.util.LinkedList;
import java.util.List;

public class DiscordRoles {
    private DiscordRoles() {
    }

    private static List<String> roleList = new LinkedList<>();

    public static boolean allRolesFromSite(String nameRole) {
        if (roleList.contains(nameRole)) {
            return true;
        }
        return false;
    }
    public static String delPreviousRole(String roleDiscord){
        roleList.add("Студент Академии");
        roleList.add("Генин");
        roleList.add("Чуунин");
        roleList.add("Джоунин");
        roleList.add("Каге");
        roleList.add("Анбу");
        roleList.add("Акацки");

        if (roleDiscord.equals("Студент Академии")){
            return roleDiscord;
        }
        for (int i = 1; i <= roleList.size(); i++){
            if (roleList.get(i).equals(roleDiscord)){
                return roleList.get(i-1);
            }
        }
        return null;
    }
}

