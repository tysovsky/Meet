package tysovsky.meet;

/**
 * Created by tysovsky on 4/4/16.
 */
public class PeopleNearMe {
    private String name = "";
    private String username = "";

    public PeopleNearMe(String name, String username){
        this.name = name;
        this.username = username;
    }

    public String getName(){
        return this.name;
    }

    public String getUsername(){
        return this.username;
    }
}
