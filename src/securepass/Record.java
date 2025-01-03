package securepass;

public class Record {
    private int id;
    private String type;
    private String username;
    private String password;
    private String note;
    private String lastUpdatedTime;
    private String createdTime;

    public Record(int id, String type, String username, String password, String note, String lastUpdatedTime, String createdTime) {
        this.id = id;
        this.type = type;
        this.username = username;
        this.password = password;
        this.note = note;
        this.lastUpdatedTime = lastUpdatedTime;
        this.createdTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}

