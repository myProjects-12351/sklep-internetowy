package sklepy.sklep1.entities;

public class Address {
    private Long task_id;
    private Long user_id;
    private String fname;
    private String lname;
    private String country;
    private String city;
    private String road;
    private String postal_code;

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    @Override
    public String toString() {
        return "Adress{" +
                "task_id=" + task_id +
                ", user_id=" + user_id +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", road='" + road + '\'' +
                ", postal_code='" + postal_code + '\'' +
                '}';
    }
}
