package com.premierLeague.premier_zone.Model;

import lombok.Data;

@Data
public class PlayerDTO {
    private String name;
    private String nation;
    private String pos;
    private Integer age;
    private Integer mp;
    private Integer starts;
    private Double min;
    private Double gls;
    private Double ast;
    private Double pk;
    private Double crdy;
    private Double crdr;
    private Double xg;
    private Double xag;
    private String teamName;

    public PlayerDTO(Player player) {
        this.name = player.getName();
        this.nation = player.getNation();
        this.pos = player.getPos();
        this.age = player.getAge();
        this.mp = player.getMp();
        this.starts = player.getStarts();
        this.min = player.getMin();
        this.gls = player.getGls();
        this.ast = player.getAst();
        this.pk = player.getPk();
        this.crdy = player.getCrdy();
        this.crdr = player.getCrdr();
        this.xg = player.getXg();
        this.xag = player.getXag();
        this.teamName = player.getTeam() != null ? player.getTeam().getName() : null;
    }
}