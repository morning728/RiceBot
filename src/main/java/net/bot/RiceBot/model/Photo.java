package net.bot.RiceBot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name="photos")
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="owner_username")
    private String owner_username;
    @Column(name="date")
    private Date date;

    public Photo(String _name, String _ownerUsername, Date _date){
        name = _name;
        owner_username = _ownerUsername;
        date = _date;
    }
}
