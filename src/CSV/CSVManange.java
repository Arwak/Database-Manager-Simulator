package CSV;

import DBMSi.DataType;
import DBMSi.TableRow;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ClaudiaPeiro on 4/7/17.
 */
public class CSVManange {
    private static List<DataType> typeColumns;
    private static String columns;
    private static List<String> columnNames;
    private static String path = "./files";
    private static final String WHEREARECSV = "./files/";
    private static FileWriter w;

    private static final String SPLITBY = ";";
    private static final char COMMA = ',';

    public static void setPath (String pathEntered) {
        path = WHEREARECSV + pathEntered;
    }

    public static void prepareCSVClass (List<String> columnNoms, List<DataType> type) {
        columnNames = columnNoms;
        typeColumns = type;
    }

    public static ArrayList<TableRow> readCSV () {
        ArrayList<TableRow> rows = new ArrayList<>();

        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new FileReader(path));
            columns = br.readLine();

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] what = line.split(String.valueOf(COMMA));
                rows.add(creaTableRow(what));
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return rows;
    }

    public static void prepareFileToExport(String name) {
        try {
            w = new FileWriter(path + "/" + name + ".csv");
        } catch (IOException e) {
            System.err.println("Ups something went wrong! " + e.getMessage());
        }
    }

    public static void writeLines (ArrayList<HashMap> values) {
        StringBuilder sb = new StringBuilder();
        int howManyColumns = columnNames.size();
        for (HashMap value : values) {
            for (int a = 0; a < howManyColumns; a++) {
                String column = columnNames.get(a);
                if (a > 0) {
                    sb.append(COMMA);
                }
                sb.append(value.get(column));
            }
            try {
                sb.append("\n");
                w.append(sb.toString());
            } catch (IOException e) {
                System.err.println("Ups something went wrong! " + e.getMessage());
            }
            sb.setLength(0);
        }
    }

    public static void endFileToExport () {
        try {
            w.flush();
            w.close();
        } catch (IOException e) {
            System.err.println("Ups something went wrong! " + e.getMessage());
        }

    }
    private static TableRow creaTableRow (String[] line) {
        int tamany = columnNames.size();
        TableRow row = new TableRow();
        for (int aux = 0; aux < tamany; aux++) {
           addColumnWithTypeCorrect(aux, line, row);
        }
        return row;
    }

    private static void addColumnWithTypeCorrect (int aux, String[] line, TableRow row) {
        if (typeColumns.get(aux).equals(DataType.INT)) {
            row.addColumn(columnNames.get(aux), Integer.parseInt(line[aux]));
        }
        if (typeColumns.get(aux).equals(DataType.BOOLEAN)) {
            row.addColumn(columnNames.get(aux), Boolean.getBoolean(line[aux]));
        }
        if (typeColumns.get(aux).equals(DataType.LONG)) {
            row.addColumn(columnNames.get(aux),Long.parseLong(line[aux]));
        }
        if (typeColumns.get(aux).equals(DataType.FLOAT)) {
            row.addColumn(columnNames.get(aux), Float.parseFloat(line[aux]));
        }
        if (typeColumns.get(aux).equals(DataType.DOUBLE)) {
            row.addColumn(columnNames.get(aux), Double.parseDouble(line[aux]));
        }
        if (typeColumns.get(aux).equals(DataType.CHAR)) {
            row.addColumn(columnNames.get(aux), line[aux]);
        }
        if (typeColumns.get(aux).equals(DataType.TEXT)) {
            row.addColumn(columnNames.get(aux), line[aux]);
        }

    }

}
