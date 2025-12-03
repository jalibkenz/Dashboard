package in.kenz.dashboard.entity;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String userUsername;
    private String userPassword;
    @ManyToOne
    private Role userRoleName;

    public User() {
    }

    public User(Long userId, String userName, String userUsername, String userPassword, Role userRoleName) {
        this.userId = userId;
        this.userName = userName;
        this.userUsername = userUsername;
        this.userPassword = userPassword;
        this.userRoleName = userRoleName;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userUsername='" + userUsername + '\'' +
                ", userRoleName=" + userRoleName +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Role getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(Role userRoleName) {
        this.userRoleName = userRoleName;
    }
}