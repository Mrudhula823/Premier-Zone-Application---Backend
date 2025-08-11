package com.premierLeague.premier_zone.Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player_statistic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @Column(name = "player_name", unique = true)
    private String name;
    private String nation;
    @Column(name = "position")
    private String pos;
    private Integer age;
    @Column(name = "matches_played")
    private Integer mp;
    private Integer starts;
    @Column(name = "minutes_played")
    private Double min;
    @Column(name = "goals")
    private Double gls;
    @Column(name = "assists")
    private Double ast;
    @Column(name = "penalties_scored")
    private Double pk;
    @Column(name = "yellow_cards")
    private Double crdy;
    @Column(name = "red_cards")
    private Double crdr;
    @Column(name = "expected_goals")
    private Double xg;
    @Column(name = "expected_assists")
    private Double xag;
    @Column(name = "team_name")
    private String teamName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Team team;

    public Player(String name) {
        this.name = name;
    }
}


