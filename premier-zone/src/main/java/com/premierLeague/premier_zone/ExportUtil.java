package com.premierLeague.premier_zone;

import com.premierLeague.premier_zone.Model.Player;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Component
public class ExportUtil {
    public static void writePlayersToCsv(List<Player> players, Writer writer){
        try(CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Name", "Nation","Position","Age","MP","Starts","Min","Gls","Ast","PK","Crdy","Crdr","Xg","Xag","Team"))) {
            for (Player player: players){
                printer.printRecord(
                        player.getName(),
                        player.getNation(),
                        player.getPos(),
                        player.getAge(),
                        player.getMp(),
                        player.getStarts(),
                        player.getMin(),
                        player.getGls(),
                        player.getAst(),
                        player.getPk(),
                        player.getCrdy(),
                        player.getCrdr(),
                        player.getXg(),
                        player.getXag(),
                        player.getTeam()

                );
            }
            printer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
