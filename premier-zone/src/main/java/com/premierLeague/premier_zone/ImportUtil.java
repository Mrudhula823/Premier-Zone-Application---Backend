package com.premierLeague.premier_zone;

import com.premierLeague.premier_zone.Model.Player;
import com.premierLeague.premier_zone.Model.Team;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ImportUtil {
    public static List<Player> readPlayersFromCsv(InputStream inputStream) throws IOException {
        List<Player> players = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.mark(1);
        int firstChar = reader.read();
        if(firstChar != 0xFEFF) {
            reader.reset();
        }
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();
            try(CSVParser csvParser = new CSVParser(reader,csvFormat)){
                for(CSVRecord record: csvParser){
                    Player player = new Player();

                    player.setName(record.get("name"));
                    player.setNation(record.get("nation"));
                    player.setPos(record.get("pos"));
                    player.setAge(parseInt(record.get("age")));
                    player.setMp(parseInt(record.get("mp")));
                    player.setStarts(parseInt(record.get("starts")));
                    player.setMin(parseDouble(record.get("min")));
                    player.setGls(parseDouble(record.get("gls")));
                    player.setAst(parseDouble(record.get("ast")));
                    player.setPk(parseDouble(record.get("pk")));
                    player.setCrdy(parseDouble(record.get("crdy")));
                    player.setCrdr(parseDouble(record.get("crdr")));
                    player.setXg(parseDouble(record.get("xg")));
                    player.setXag(parseDouble(record.get("xag")));
                    String teamName = record.get("team");
                    player.setTeam(new Team(teamName));
                    players.add(player);


                }
            }

        return players;
    }
    private static Double parseDouble(String value){
        try{
            return Double.parseDouble(value);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
