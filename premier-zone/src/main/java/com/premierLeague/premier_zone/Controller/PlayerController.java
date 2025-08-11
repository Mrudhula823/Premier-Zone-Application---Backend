package com.premierLeague.premier_zone.Controller;

import com.premierLeague.premier_zone.ExportUtil;
import com.premierLeague.premier_zone.ImportUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.premierLeague.premier_zone.Model.Player;
import com.premierLeague.premier_zone.Model.PlayerDTO;
import com.premierLeague.premier_zone.Service.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    private final PlayerService service;
    @Autowired
    public PlayerController(PlayerService service) {
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<Page<PlayerDTO>> getPlayers(@RequestParam(required = false) String team, @RequestParam(required = false) String name,
                                      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "name") String sortBy,
                                      @RequestParam(defaultValue = "asc") String direction){
        Page<Player> players;
        if(team != null){
           players = (Page<Player>) service.getPlayersfromTeam(team,page,size,sortBy,direction);
        }else if(name != null){
            players = (Page<Player>) service.getPlayersByName(name,page,size,sortBy,direction);
        }else{
            players = service.getPlayers(page,size,sortBy,direction);
        }
        Page<PlayerDTO> playerDTOPage = players.map(PlayerDTO::new);
        return ResponseEntity.ok(playerDTOPage);
    }
    @GetMapping(value = "/export/csv", produces = "text/csv")
    public void exportToCSV(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "100") int size,
                            @RequestParam(defaultValue = "name") String sortBy,
                            @RequestParam(defaultValue = "asc") String direction,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=players.csv");

        List<Player> players = service.getPlayers(page,size,sortBy,direction).getContent();
        ExportUtil.writePlayersToCsv(players, response.getWriter());
    }


//    @GetMapping("/age")
//    public List<PlayerDTO> filterByAgeBetween(@RequestParam int min, @RequestParam int max){
//        return service.filterByAgeRange(min,max).stream().map(PlayerDTO::new).toList();
//    }
//    @GetMapping("/position")
//    public List<PlayerDTO> filterByPosition(@RequestParam String pos){
//        return service.filterByPosition(pos).stream().map(PlayerDTO::new).toList();
//    }
//    @GetMapping("/nation")
//    public List<PlayerDTO> filterByNation(@RequestParam String nation){
//        return service.filterByNation(nation).stream().map(PlayerDTO::new).toList();
//    }
    @PostMapping(value = "/import/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importPlayersFromCsv(@RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("csv file is empty.");
        }
        try{
            List<Player> players = ImportUtil.readPlayersFromCsv(file.getInputStream());
            List<Player> saved = service.saveAllPlayers(players);
            List<PlayerDTO> dtos = saved.stream().map(player -> {
                String teamName = player.getTeam() != null ? player.getTeam().getName() : null;
                PlayerDTO dto = new PlayerDTO(player);
                dto.setTeamName(teamName);
                return dto;
            }).toList();
            return ResponseEntity.ok(dtos);
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed to read CSV file.");
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody Player player){
        Player savedPlayer = service.addPlayer(player);
        return new ResponseEntity<>(new PlayerDTO(savedPlayer), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<PlayerDTO> updatedPlayer(@RequestBody Player updatedplayer){
        Optional<Player> result = service.updatePlayer(updatedplayer);
        return result
                .map(player -> new ResponseEntity<>(new PlayerDTO(player),HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/{playername}")
    public ResponseEntity<String> deletePlayer(@PathVariable String playername){
        service.deletePlayer(playername);
        return new ResponseEntity<>("deleted successfully",HttpStatus.OK);

    }
    @DeleteMapping("/teamname/{teamName}")
    public ResponseEntity<?> deletedPlayers(@PathVariable String teamName) {
        try {
            List<Player> deletedPlayers = service.deletePlayersfromTeam(teamName);
            List<PlayerDTO> deletedDTOs = deletedPlayers.stream().map(PlayerDTO::new).toList();
            return ResponseEntity.ok(deletedDTOs);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + ex.getMessage());
        }
    }
}
