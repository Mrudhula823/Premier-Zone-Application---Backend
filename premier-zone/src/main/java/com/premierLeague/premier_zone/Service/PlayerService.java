package com.premierLeague.premier_zone.Service;

import com.premierLeague.premier_zone.Model.Player;
import com.premierLeague.premier_zone.Model.Team;
import com.premierLeague.premier_zone.Repository.PlayerRepository;
import com.premierLeague.premier_zone.Repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerrepo;
    private final TeamRepository teamrepo;

    @Autowired
    public PlayerService(PlayerRepository playerrepo, TeamRepository teamrepo) {
        this.playerrepo = playerrepo;
        this.teamrepo = teamrepo;
    }

    public Page<Player> getPlayers(int page, int size, String sortBy, String direction) {
        Sort.Direction sorted = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorted, sortBy));
        return playerrepo.findAll(pageable);
    }

    public Page<Player> getPlayersfromTeam(String teamName, int page, int size, String sortBy, String direction) {
        Sort.Direction sorted = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size,Sort.by(sorted, sortBy));
        if(!playerrepo.existsByTeamName(teamName)){
            throw new EntityNotFoundException("team not found: " + teamName);
        }
        return playerrepo.findByTeamNameIgnoreCase(teamName,pageable);
    }

    public Page<Player> getPlayersByName(String name, int page, int size, String sortBy, String direction) {
        Sort.Direction sorted = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size,Sort.by(sorted,sortBy));
        if(!playerrepo.existsByName(name)){
            throw new EntityNotFoundException("player not found: " + name);
        }
        return playerrepo.findByNameContainingIgnoreCase(name,pageable);
    }

    public Player addPlayer(Player player) {
        String teamName = player.getTeam().getName();
        Team team = teamrepo.findByNameIgnoreCase(teamName)
                .orElseThrow(() -> new EntityNotFoundException("Team not found: " + teamName));
        player.setTeam(team);
        return playerrepo.save(player);
    }

    public Optional<Player> updatePlayer(Player updatedPlayer) {
        return playerrepo.findByName(updatedPlayer.getName())
                .map(existing -> {
                    existing.setNation(updatedPlayer.getNation());
                    existing.setPos(updatedPlayer.getPos());

                    if (updatedPlayer.getTeam() != null) {
                        Team team = teamrepo.findByNameIgnoreCase(updatedPlayer.getTeam().getName())
                                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
                        existing.setTeam(team);
                    }

                    return playerrepo.save(existing);
                });
    }
    public List<Player> saveAllPlayers(List<Player> players){
        for(Player player: players){
            String teamName = player.getTeam().getName();
            Team team = teamrepo.findByNameIgnoreCase(teamName)
                    .orElseThrow(() -> new EntityNotFoundException("team not found: " + teamName));
            player.setTeam(team);
            player.setTeamName(team.getName());
        }
        return playerrepo.saveAll(players);
    }

//    public List<Player> filterByPosition(String pos) {
//        return playerrepo.findByPos(pos);
//    }

//    public List<Player> filterByNation(String nation) {
//        return playerrepo.findByNation(nation);
//    }

//    public List<Player> filterByAgeRange(int min, int max) {
//        return playerrepo.findByAgeBetween(min, max);
//    }

    @Transactional
    public void deletePlayer(String playername) {
        boolean exists = playerrepo.existsByName(playername);
        if (!exists) {
            throw new EntityNotFoundException("Player not found: " + playername);
        }
        playerrepo.deleteByName(playername);
    }

    @Transactional
    public List<Player> deletePlayersfromTeam(String teamName) {
        String normalizedTeamName = teamName.trim();
        Team team = teamrepo.findByNameIgnoreCase(normalizedTeamName)
                .orElseThrow(() -> new EntityNotFoundException("Team not found: " + teamName));
        List<Player> playersToDelete = playerrepo.findAll().stream()
                .filter(player -> team.equals(player.getTeam()))
                .toList();
        playerrepo.deleteAll(playersToDelete);
        return playersToDelete;
    }

}



