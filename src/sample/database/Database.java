package sample.database;

import sample.Model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Database instance;
    private final List<Player> observableList;

    private static final String INPUT_FILE_NAME = "players.txt";
    private static List<Player> playerList = new ArrayList<>();


    private Database(){

        try {
            playerList = readFromFile(INPUT_FILE_NAME);
        } catch (Exception e) {
            System.out.println("Read failed");
            System.out.println(e);
        }
          observableList = playerList;
    }

    private static List<Player> readFromFile(String inputFileName) throws Exception {

        List<Player> playerList = new ArrayList<>();
        String line;

        BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        while(true){

            line = br.readLine();
            if(line == null) break;
            //System.out.println(line);
            String[] tokens = line.split(",");

            Player player = new Player();
            player.setName(tokens[0]);
            player.setCountry(tokens[1]);
            player.setAge(Integer.parseInt(tokens[2]));
            player.setHeight(Double.parseDouble(tokens[3]));
            player.setClubName(tokens[4]);
            player.setPosition(tokens[5]);
            player.setNumber(Integer.parseInt(tokens[6]));
            player.setSalary(Double.parseDouble(tokens[7]));
            player.setOnSell(Boolean.parseBoolean(tokens[8]));

            playerList.add(player);
        }

        br.close();

        return playerList;
    }

    public void writeToFile(String oldInfo,String newInfo){

        String oldContent = "";

        BufferedReader reader = null;

        FileWriter writer = null;

        try
        {
            reader = new BufferedReader(new FileReader(INPUT_FILE_NAME));

            //Reading all the lines of input text file into oldContent

            String line = reader.readLine();

            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Replacing oldString with newString in the oldContent

            String newContent = oldContent.replaceAll(oldInfo, newInfo);

            //Rewriting the input text file with newContent

            writer = new FileWriter(INPUT_FILE_NAME);

            writer.write(newContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources

                reader.close();

                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static Database getInstance(){
        if( instance == null ){
            instance = new Database();
        }
        return instance;
    }
    public List<Player> getObservableList(){
        return observableList;
    }

    public void addItems(Player  s){
        observableList.add(s);
    }
    public void deleteItems(int index){
        observableList.remove(index);
    }
}