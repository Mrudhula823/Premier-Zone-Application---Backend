package com.premierLeague.premier_zone.Repository;

import com.premierLeague.premier_zone.Model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
