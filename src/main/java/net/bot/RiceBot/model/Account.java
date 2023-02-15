package net.bot.RiceBot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;

import javax.persistence.*;

@Data
@Entity
@Table(name="accounts")
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="role")
    @Enumerated(value=EnumType.STRING)
    private Role role;

    public Account(String _username, String _password, Role _role){
        username = _username;
        password = _password;
        role = _role;
    }
}