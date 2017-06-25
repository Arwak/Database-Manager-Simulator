package AVL;

import DBMSi.Table;
import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

import java.util.*;

/**
 * Created by ClaudiaPeiro on 21/6/17.
 */
public class AVL extends TableDataStructure {
    private NodeAVL root;
    private long size;
    private ArrayList<TableRow> historic;


    public AVL( String index, NodeAVL node) {
        root = node;
        size = 0;
        super.setIndex(index);
    }
    @Override
    protected boolean add(TableRow tableRow) {
        whereToPlace(root,tableRow);
        return false;
    }

    @Override
    protected void select(TableRowRestriction restrictions) {
        whatToShow(root, restrictions);


    }

    @Override
    protected void selectUnique(TableRowRestriction restriction, String column) {
        whatToShowUnique(root, restriction, column);
    }

    @Override
    protected boolean update(String field, TableRow row) {
        return whatToModify(root, row, field);
    }

    @Override
    protected boolean remove(String field, Object value) {
        whatToDelete(root, field, value, false);
        whatToShow(root, null);
        return true;
    }

    @Override
    protected long size() {
        return size;
    }

    /**
     * Mètode que s'encarregarà de buscar en quina posició s'ha de situar
     * @param tableRow
     */
    private void whereToPlace(NodeAVL actual, TableRow tableRow) {
        if(actual == null){
            root = new NodeAVL(tableRow);
            size++;
            return;
        }
        int where = actual.getRoot().compareTo(index, tableRow);
        if(where < 1){
            if(where == 0) {
                //Element duplicat no fer res
                System.out.println("Element duplicat!");
            } else {
                if(actual.isChildRight()){
                    whereToPlace(actual.getChildRight(), tableRow);
                } else {
                    size++;
                    actual.setChildRight(new NodeAVL(tableRow));
                    actual.getChildRight().setParent(actual);
                }
            }
        } else {
            if(actual.isChildLeft()){
                whereToPlace(actual.getChildLeft(), tableRow);
            } else {
                size++;
                actual.setChildLeft(new NodeAVL(tableRow));
                actual.getChildLeft().setParent(actual);
            }

        }

    }

    /**
     * Algorisme per buscar segons una restricció realitza una búsqueda inOrdre
     * @param actual node actual que sesta buscant
     * @param restriction restricció segons la que s'esta buscant
     */
    private void whatToShow (NodeAVL actual, TableRowRestriction restriction) {
        if(actual == null) {
            return;
        }
        if (restriction.test(actual.getRoot())) {
            System.out.println(actual.getRoot().toString());
        }

        whatToShow(actual.getChildLeft(), restriction);
        whatToShow(actual.getChildRight(), restriction);

    }

    private void whatToShowUnique (NodeAVL actual, TableRowRestriction restriction, String column) {
        if(actual == null) {
            return;
        }
        if (restriction.test(actual.getRoot())) {
            System.out.print(actual.getRoot().getContent().get(column).toString());
        }
        whatToShowUnique(actual.getChildLeft(), restriction, column);
        whatToShowUnique(actual.getChildRight(), restriction, column);
    }

    private Boolean whatToModify (NodeAVL actual, TableRow tableRow, String field) {
        Boolean problems = false;
        if(actual == null){
            problems = true;
            System.out.println("Element not found!");
            return !problems;
        }
        int where = actual.getRoot().compareTo(field, tableRow);
        if(where < 1){
            if(where == 0) {
                TableRow updatedRow = getUpdated(actual, tableRow);
                actual.setRoot(updatedRow);
                problems = false;
            } else {
                if(actual.isChildLeft()){
                    whatToModify(actual.getChildLeft(), tableRow, field);
                } else {
                    problems = true;
                }
            }
        } else {
            if(actual.isChildRight()){
                whatToModify(actual.getChildRight(), tableRow, field);
            } else {
                problems = true;
            }

        }

        if (problems) {
            System.out.println("Element not found!");
        }
        return !problems;

    }

    private void whatToDelete (NodeAVL actual, String field, Object value, Boolean right) {
        if(actual == null){
            return;
        }
        int where = actual.getRoot().compareTo(field, value);
        if (where < 1) {
            if (where == 0) {
                if (!actual.isChildLeft() && !actual.isChildRight()) {
                    if (right) {
                        actual.getParent().setExistanceChildRight();
                    } else {
                        actual.getParent().setExistanceChildLeft();
                    }
                } else {
                    if (actual.isChildLeft() ) {
                        if (actual.isChildRight()) {
                            actual.getChildRight().setParent(actual.getChildLeft());
                        }
                        actual.getChildLeft().setParent(actual.getParent());
                        if (right) {
                            actual.getParent().setChildRight(actual.getChildLeft());

                        } else {
                            actual.getParent().setChildLeft(actual.getChildLeft());
                        }

                    }
                }
            } else {
                whatToDelete(actual.getChildRight(), field, value, false);
            }
        } else {
            whatToDelete(actual.getChildLeft(), field, value, true);
        }



    }
    private TableRow getUpdated (NodeAVL actual, TableRow tableRow) {
        Set<String> keys = tableRow.getContent().keySet();
        TableRow updatedRow;
        updatedRow = actual.getRoot();
        for (String key : keys) {
            updatedRow.getContent().put(key, tableRow.getContent().get(key));
        }
        return updatedRow;
    }

    public static String getName() {
        return "AVL";
    }

    public NodeAVL InOrdre (NodeAVL actual) {
        if (actual.isChildLeft()) {
            return actual.getChildLeft();
        } else {
            if (actual.isChildRight()) {
                return actual.getChildRight();
            } else {
                return actual;
            }
        }
    }

}
