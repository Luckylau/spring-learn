package luckylau.spring.jwt.entity;


import javax.persistence.*;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
@Entity
@Table(name = "users")
public class UserPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "enabled", nullable = false)
    private int enabled;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public UserInfo toUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(this.getUsername());
        userInfo.setUserId(this.getUsername());
        userInfo.setEmail(this.getEmail());
        return userInfo;
    }
}