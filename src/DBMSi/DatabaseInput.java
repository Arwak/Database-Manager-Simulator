package DBMSi;

import java.util.List;
import java.util.Scanner;

/**
 * Created by Albertpv on 31/03/17.
 *
 * Classe encarregada de la lectura de dades per teclat necessàries per tal de realitzar
 * certes opcions de les taules i creació de restriccions.
 */
public class DatabaseInput {

    private static final Scanner scanner = new Scanner(System.in);


    /**
     * Permet la introducció d'un valor del tipus de la columna.
     *
     * @param dataType      El tipus de la columna.
     * @param columnName    El nom de la columna.
     *
     * @return              El valor introduït per teclat.
     *
     * @throws NumberFormatException    Si es dóna un error de format amb les dades introduïdes per teclat.
     */
    public static Object readColumnValue(DataType dataType, String columnName) throws NumberFormatException {

        switch (dataType) {

            case BOOLEAN:

                System.out.print("Enter a boolean value for " + columnName + ": ");
                return Boolean.parseBoolean(scanner.nextLine()); // basura de mètode

            case INT:

                System.out.print("Enter an int value for " + columnName + ": ");
                return Integer.parseInt(scanner.nextLine());

            case LONG:

                System.out.print("Enter a long value for " + columnName + ": ");
                return Long.parseLong(scanner.nextLine());

            case FLOAT:

                System.out.print("Enter a float value for " + columnName + ": ");
                return Float.parseFloat(scanner.nextLine());

            case DOUBLE:

                System.out.print("Enter a double value for " + columnName + ": ");
                return Double.parseDouble(scanner.nextLine());

            case CHAR:

                System.out.print("Enter a char value for " + columnName + ": ");
                String text = scanner.nextLine();   // ens curem en salut amb els \n
                return text.isEmpty()? '\0' : text.charAt(0);

            case TEXT:

                System.out.print("Enter a text value for " + columnName + ": ");
                return scanner.nextLine();

                default:
                    throw new IllegalArgumentException("This data type is not supported by the System.");
        }
    }

    /**
     * S'encarrega d'obtenir el nom de la columna que serà índex de la taula.
     *
     * @return El nom de la columna índex.
     */
    public static String readIndexColumnName() {

        System.out.print("Which field?: ");
        return scanner.nextLine();
    }

    /**
     * Mostra un conjunt d'opcions per tal de demanar el tipus de restricció per un valor donat.
     *
     * @param restrictionValue  El valor a partir del qual aplicar la restricció.
     *
     * @return                  El valor enter associat al tipus de restricció.
     *
     * @throws NumberFormatException Si el format de les dades introduïdes no és enter.
     */
    public static int readRestrictionType(Object restrictionValue) throws NumberFormatException {

        System.out.println("How do you want to restrict the value?");
        System.out.println("1. Less than " + restrictionValue.toString());
        System.out.println("2. Equals to " + restrictionValue.toString());
        System.out.println("3. Greater than " + restrictionValue.toString());

        return Integer.parseInt(scanner.nextLine());
    }

    public static String askYesNo() {
        String tipus;
        Scanner sc = new Scanner(System.in);
        Boolean status;
        do {
            tipus = sc.nextLine();
            status = tipus.equals("Y")|| tipus.equals("N");


            if (!status) {
                System.err.println("Error, this is a yes or no answer! You may only answer with Y or N!");
            }
        } while (!status);

        return tipus;
    }

    public static boolean isCorrectType(DataType possible) {
        return possible.toString().equals("INT") || possible.toString().equals("TEXT");

    }

    /**
     * Funció que mostra per pantalla la petició d'una columna. Serà la petició primera del
     * menú
     * @param taula nom de la taula
     * @return string amb el nom de la columna pel seu posterior tractament
     */
    public static String askForColumnInicial(String taula) {
        System.out.print("Enter a column name for the new table ");
        System.out.print(taula);
        System.out.println(":");
        return scanner.nextLine();
    }

    /**
     * Comprova si existeix DataStructure seleccionat
     * @param tipus tipus de dataStructure introduit per l'usuari
     * @return retornarà el DataStructure addient, null en cas que no existeixi
     */
    public static DataType setiComprovaTipus(String tipus) {
        if(tipus.equals(DataType.BOOLEAN.toString())) {
            return DataType.BOOLEAN;
        }

        if(tipus.equals(DataType.CHAR.toString())) {
            return DataType.CHAR;
        }

        if(tipus.equals(DataType.FLOAT.toString())) {
            return DataType.FLOAT;
        }

        if(tipus.equals(DataType.LONG.toString())) {
            return DataType.LONG;
        }

        if(tipus.equals(DataType.INT.toString())) {
            return DataType.INT;
        }

        if(tipus.equals(DataType.DOUBLE.toString())) {
            return DataType.DOUBLE;
        }

        if(tipus.equals(DataType.INT.toString())) {
            return DataType.INT;
        }

        if(tipus.equals(DataType.TEXT.toString())) {
            return DataType.TEXT;
        }
        return null;
    }

    /**
     * Funció que busca i comprova que el tipus de dada introduida sigui correcte
     * @return tipus de DataType, null si és incorrecte
     */
    public static DataType searchDataType() {
        String tipus;
        Boolean status = false;
        DataType dataType = null;


        while (!status) {
            System.out.println("Which kind of data stores this column?");
            tipus = scanner.nextLine();
            dataType = DatabaseInput.setiComprovaTipus(tipus);
            status = (dataType != null);
            if (!status) {
                System.err.print("Error, type ");
                System.err.print(tipus);
                System.err.println(" doesn't exists");
            }
        }
        return dataType;
    }

    public static String askForColumn(){
        String column;
        System.out.println("Column? ");
        column = scanner.nextLine();
        return column;
    }

    public static int askForaTable() {
        int table;
        System.out.println("Select a table: ");
        table = scanner.nextInt();
        scanner.nextLine();
        return table;
    }
}
