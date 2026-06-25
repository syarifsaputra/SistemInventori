package model;

public class User {
    private String username;
    private String password;
    private String role;
    private String nama;

    public User(String username, String password,
                String role, String nama) {
        this.username = username;
        this.password = password;
        this.role     = role;
        this.nama     = nama;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role;     }
    public String getNama()     { return nama;     }
}