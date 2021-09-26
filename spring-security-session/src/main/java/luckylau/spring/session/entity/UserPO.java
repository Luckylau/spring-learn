package luckylau.spring.session.entity;


import luckylau.spring.session.vo.UserInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
@Entity
@Table(name = "Users")
public class UserPO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;
    @Column(name = "Username", nullable = false)
    private String username;
    @Column(name = "Password", nullable = false)
    private String password;
    @Column(name = "Email", nullable = false)
    private String email;
    @Column(name = "Enabled", nullable = false)
    private int enabled;

    @Column(name = "Role", nullable = false)
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserInfo toUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(this.getUsername());
        userInfo.setUserId(this.getUsername());
        userInfo.setEmail(this.getEmail());
        return userInfo;
    }
}