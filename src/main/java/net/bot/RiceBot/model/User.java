package net.bot.RiceBot.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;

import javax.persistence.*;


@Data
@Entity
@Table(name="users")
@NoArgsConstructor
public class User {
    @Id
    private Long id;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="role")
    @Enumerated(value=EnumType.STRING)
    private Role role;
    @Enumerated(value=EnumType.STRING)
    @Column(name="state")
    private State state;

    public User(Long _id){
        id = _id;
        username = "defaultUN";
        password = "defaultPW";
        role = Role.UNREGISTERED;
        state = State.NULL;
    }
}