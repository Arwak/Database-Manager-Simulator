package Estructures;

import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XRoma i Clupspv on 21/6/17.
 */
public abstract class Taula extends TableDataStructure {

    protected final static int MIDA_TAULA_R = 1000000;

    protected long size;

    @Override
    protected abstract void showHistoric(String field, Object valor);

    @Override
    protected abstract boolean add(TableRow tableRow);

    @Override
    protected abstract ArrayList<String> select(TableRowRestriction restrictions);

    @Override
    protected abstract String selectUnique(TableRowRestriction restriction, String column);

    @Override
    protected abstract ArrayList<HashMap> selectAllInformation(TableRowRestriction restriction);

    @Override
    protected abstract boolean update(String field, TableRow row);

    @Override
    protected abstract boolean remove(String field, Object value);

    @Override
    protected long size() {
        return size;
    }

    protected int hashString(String clau) {
        int mida = clau.length();
        int suma = 0;

        clau = clau.toLowerCase();

        for (int i = 0; i < mida; i++) {

            suma+= (clau.charAt(i) - 'a');

        }

        return suma % MIDA_TAULA_R;
    }


    protected int hashInt(int clau) {
        return clau % MIDA_TAULA_R;
    }

    protected long DJBHash (String clau) {
        long hash = 5381;
        int size = clau.length();
        for (int i = 0; i < size; i++) {
            hash = ((hash << 5) + hash) + clau.charAt(i);
        }
        return hash;
    }

    protected int otherHashInt (int clau) {
        int hash = 0;
        for (int i = 0; i < clau; i++) {

        }

        return hash;
    }

}







