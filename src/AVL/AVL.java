package AVL;

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
                    actual.getChildRight().updateHeight();
                    calculHeight(actual, true);
                }
            }
        } else {
            if(actual.isChildLeft()){
                whereToPlace(actual.getChildLeft(), tableRow);
            } else {
                size++;
                actual.setChildLeft(new NodeAVL(tableRow));
                actual.getChildLeft().setParent(actual);
                actual.getChildLeft().updateHeight();
                calculHeight(actual, true);
            }

        }

    }

    private void calculHeight(NodeAVL actual, Boolean endarrere) {
        int left = heightLeft(actual);
        int right = heightRight(actual);
        int balance = Math.abs(left - right);

        if (balance < 2) {
            actual.setBalance(balance);
            if (actual.getHeight() != 0) {
                if (endarrere) {
                    calculHeight(actual.getParent(), true);
                }
            }

        } else {
            if (Math.abs(right) > Math.abs(left)) {
                NodeAVL quinAMirar = actual.getChildRight();
                if (heightRight(quinAMirar) > heightLeft(quinAMirar)) {
                    movimentRR(actual, true);
                } else {
                    movimentRL(actual);
                }

            } else {
                if (Math.abs(left)  > Math.abs(right)) {
                    NodeAVL quinAMirar = actual.getChildLeft();
                    if (heightLeft(quinAMirar)  > heightRight(quinAMirar)) {
                        movimentLL(actual, true);
                    } else {
                        movimentLR(actual);
                    }

                }
            }

        }

    }

    public int heightLeft (NodeAVL actual) {
        int balance = actual.getBalance() == 0 ? -actual.getHeight() : actual.getBalance();
        if (actual.getChildLeft() != null) {
            actual.getChildLeft().updateHeight();
        }
        return actual.getChildLeft() == null ? 0 : actual.getChildLeft().getHeight() + balance;
    }

    public int heightRight (NodeAVL actual) {
        int balance = actual.getBalance() == 0 ? -actual.getHeight() : actual.getBalance();
        if (actual.getChildRight() != null) {
            actual.getChildRight().updateHeight();
        }
        return actual.getChildRight() == null ? 0 : actual.getChildRight().getHeight() + balance;
    }

    private void movimentRL (NodeAVL actual) {
        System.out.println("Toca moviment RL! De: " + actual.getRoot().toString());
        movimentLL(actual.getChildRight(), false);
        movimentRR(actual, true);
    }

    private void movimentLR (NodeAVL actual) {
        System.out.println("Toca moviment LR! De: " + actual.getRoot().toString());
        movimentRR(actual.getChildLeft(), false);
        movimentLL(actual, true);
    }

    private void movimentRR (NodeAVL actual, Boolean balanceig) {
        System.out.println("Toca moviment RR! De: " + actual.getRoot().toString());

        NodeAVL row = new NodeAVL(actual.getRoot());
        NodeAVL pare = actual.getParent();

        actual = actual.getChildRight();
        row.setParent(actual);
        row.updateHeight();
        actual.setChildLeft(row);
        actual.setParent(pare);
        actual.updateHeight();
        if  (actual.getHeight() == 0) {
            root = actual;
        }
        if (actual.getParent() != null) {
            if (actual.getParent().getRoot().compareTo(index, actual.getRoot()) < 1) {
                actual.getParent().setChildRight(actual);
            } else {
                actual.getParent().setChildLeft(actual);
            }

        }

        actual.setBalance(actual.getBalance() - 1);
        actual.getChildLeft().setBalance( actual.getChildLeft().getBalance() - 1);

        if (balanceig) {
            actual.getChildRight().setBalance( actual.getChildRight().getBalance() - 1);
        }

        if (balanceig) {
            calculHeight(actual.getChildRight(), false);
            calculHeight(actual.getChildLeft(), false);
            calculHeight(actual, false);
        }


    }

    private void movimentLL (NodeAVL actual, Boolean balanceig) {
        System.out.println("Toca moviment LL! De: " + actual.getRoot().toString());

        NodeAVL row = new NodeAVL(actual.getRoot());
        NodeAVL pare = actual.getParent();

        actual = actual.getChildLeft();
        row.setParent(actual);
        row.updateHeight();
        actual.setChildRight(row);
        actual.setParent(pare);
        actual.updateHeight();
        if  (actual.getHeight() == 0) {
            root = actual;
        }
        if (actual.getParent() != null) {
            if (actual.getParent().getRoot().compareTo(index, actual.getRoot()) < 1) {
                actual.getParent().setChildRight(actual);
            } else {
                actual.getParent().setChildLeft(actual);
            }

        }

        System.out.println(root.getRoot().toString());
        System.out.println(root.getChildRight().getRoot().toString());
        System.out.println(root.getChildRight().getChildRight().getRoot().toString());

        actual.setBalance(actual.getBalance() - 1);
        if (balanceig) {
            actual.getChildLeft().setBalance( actual.getChildLeft().getBalance() - 1);
        }
        actual.getChildRight().setBalance( actual.getChildRight().getBalance() - 1);

        if (balanceig) {
            calculHeight(actual.getChildLeft(), false);
            calculHeight(actual.getChildRight(), false);
            calculHeight(actual, false);
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
