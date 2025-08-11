package com.premierLeague.premier_zone.Repository;


import com.premierLeague.premier_zone.Model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    void deleteByName(String playerName);
    Page<Player> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Player> findByName(String name);
    boolean existsByName(String playername);
    Page<Player> findByTeamNameIgnoreCase(String teamName, Pageable pageable);

//    List<Player> findByPos(String pos);
//    List<Player> findByNation(String nation);
//    List<Player> findByAgeBetween(Integer minAge, Integer maxAge);
    Page<Player> findAll(Pageable pageable);

    boolean existsByTeamName(String teamName);
}
