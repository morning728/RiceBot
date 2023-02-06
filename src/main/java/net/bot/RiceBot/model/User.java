package net.bot.RiceBot.model;


import lombok.Data;
import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;

import javax.persistence.*;


@Data
@Entity
@Table(name="users")
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
}