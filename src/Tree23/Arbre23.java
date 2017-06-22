package Tree23;

import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

/**
 * Created by xavierromacastells on 18/6/17.
 */
public class Arbre23 extends TableDataStructure{

    public static String getNameOfStructure() {
        return "2-3 Tree";
    }

    @Override
    protected boolean add(TableRow tableRow) {
        return false;
    }

    @Override
    protected void select(TableRowRestriction restrictions) {

    }

    @Override
    protected boolean update(String field, TableRow row) {
        return false;
    }

    @Override
    protected boolean remove(String field, Object value) {
        return false;
    }

    @Override
    protected long size() {
        return 0;
    }
}
