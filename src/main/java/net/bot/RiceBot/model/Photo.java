package net.bot.RiceBot.model;

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
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="owner_id")
    private Long ownerId;
    @Column(name="date")
    private Date date;

    public Photo(String _name, Long _ownerId, Date _date){
        name = _name;
        ownerId = _ownerId;
        date = _date;
    }
}
