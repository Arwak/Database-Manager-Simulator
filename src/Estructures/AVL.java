package Estructures;

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
    private ArrayList<NodeAVL> historic;


    public AVL( String index, NodeAVL node) {
        root = node;
        size = 0;
        historic = new ArrayList<NodeAVL>();
        super.setIndex(index);
    }

    @Override
    protected void showHistoric(String field, Object valor) {
        for (int i = 0; i < historic.size(); i++) {
            if (historic.get(i).getRoot().compareTo(field, valor) == 0) {
                System.out.println(historic.get(i).getRoot().toString());
            }
        }
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
                    reCalculHeigh(actual);
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
                reCalculHeigh(actual);
                calculHeight(actual, true);

            }

        }

    }

    private void reCalculHeigh(NodeAVL actual) {
        int height;
        int right;
        int left;

        right = heightRight(actual);
        left = heightLeft(actual);

        height = (right >= left) ? right : left;
        actual.setHeight(height);

        if (actual.getParent() != null) {
            reCalculHeigh(actual.getParent());
        }
    }

    private void calculHeight(NodeAVL actual, Boolean endarrere) {
        int left = heightLeft(actual);
        int right = heightRight(actual);
        int balance = left - right;

        if (Math.abs(balance) < 2) {
            actual.setBalance(balance);
            if (actual.getParent() != null) {
                if (endarrere) {
                    calculHeight(actual.getParent(), true);
                }
            }

        } else {
            quinMoviment(actual, right, left);
        }

    }

    private void quinMoviment (NodeAVL actual, int right, int left) {
        if (right > left) {
            NodeAVL quinAMirar = actual.getChildRight();
            right = heightRight(quinAMirar);
            left = heightLeft(quinAMirar);

            if (right > left) {
                movimentRR(actual, true);
            } else {
                //System.out.println("Toca moviment RL de: " + actual.getRoot().toString());
                movimentRL(actual);
            }

        } else {
            NodeAVL quinAMirar = actual.getChildLeft();
            right = heightRight(quinAMirar);
            left = heightLeft(quinAMirar);

            if (right  > left) {
                //System.out.println("Toca moviment LR de: " + actual.getRoot().toString());
                movimentLR(actual);

            } else {
                movimentLL(actual, true);
            }


        }

    }

    private int heightLeft (NodeAVL actual) {
        int i = 0;
        if (actual.getChildLeft() != null) {
            i = actual.getChildLeft().getHeight() + 1;
        }
        return i;
    }

    private int heightRight (NodeAVL actual) {
        int i = 0;
        if (actual.getChildRight() != null) {
            i = actual.getChildRight().getHeight() + 1;
        }
        return i;
    }


    private void movimentRL (NodeAVL actual) {
        movimentLL(actual.getChildRight(), false);
        movimentRR(actual, true);
    }

    private void movimentLR (NodeAVL actual) {
        movimentRR(actual.getChildLeft(), false);
        movimentLL(actual, true);
    }

    private void movimentRR (NodeAVL actual, Boolean balanceig) {
        //System.out.println("Toca moviment RR! De: " + actual.getRoot().toString());

        NodeAVL row = new NodeAVL(actual);
        NodeAVL pare = actual.getParent();

        actual = actual.getChildRight();
        actual.setParent(pare);

        NodeAVL childLeft = null;

        if (actual.getChildLeft() != null) {
            childLeft = actual.getChildLeft();
            actual.setChildLeft(null);
        }

        actual.setChildLeft(row);
        actual.getChildLeft().setParent(actual);
        actual.getChildLeft().setChildRight(null);

        assignaEnRoot(actual);

        if (childLeft != null) {
            recolocate(actual.getChildLeft(), childLeft);
        }

        if (childLeft == null && actual.getChildRight() != null) {
            reCalculHeigh(actual.getChildRight());
        }
        reCalculHeigh(actual.getChildLeft());

        if (balanceig) {
            calculHeight(actual.getChildRight(), false);
            calculHeight(actual.getChildLeft(), false);
            calculHeight(actual, false);
        }
    }

    private void movimentLL (NodeAVL actual, Boolean balanceig) {
        //System.out.println("Toca moviment LL! De: " + actual.getRoot().toString());

        NodeAVL row = new NodeAVL(actual);
        NodeAVL pare = actual.getParent();

        actual = actual.getChildLeft();
        actual.setParent(pare);

        NodeAVL childRight = null;

        if (actual.getChildRight() != null) {
            childRight = actual.getChildRight();
            actual.setChildRight(null);
        }

        actual.setChildRight(row);
        actual.getChildRight().setParent(actual);
        actual.getChildRight().setChildLeft(null);

        assignaEnRoot(actual);

        if (childRight != null) {
            recolocate(actual.getChildRight(), childRight);
        }

        if (childRight == null && actual.getChildLeft()!= null) {
            reCalculHeigh(actual.getChildLeft());

        }
        reCalculHeigh(actual.getChildRight());

        if (balanceig) {
            calculHeight(actual.getChildLeft(), false);
            calculHeight(actual.getChildRight(), false);
            calculHeight(actual, false);
        }
    }

    private void assignaEnRoot(NodeAVL actual) {
        if  (actual.getParent() == null) {
            root = actual;
        } else {
            if (actual.getParent().getRoot().compareTo(index, actual.getRoot()) < 1) {
                actual.getParent().setChildRight(actual);
            } else {
                actual.getParent().setChildLeft(actual);
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
            System.out.println(actual.toString());

            //System.out.println(actual.getRoot().toString());


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
            System.out.println("peto aqui1");
            System.out.println("Element not found!");
            return !problems;
        }
        int where = actual.getRoot().compareTo(field, tableRow);
        if(where < 1){
            if(where == 0) {
                historic.add(actual);
                TableRow updatedRow = getUpdated(actual, tableRow);
                actual.setRoot(updatedRow);
                historic.add(actual);
                problems = false;
            } else {
                if(actual.getChildRight() != null){
                    whatToModify(actual.getChildRight(), tableRow, field);
                } else {
                    System.out.println("peto aqui 3");
                    problems = true;
                }
            }
        } else {
            if(actual.getChildLeft() != null){
                whatToModify(actual.getChildLeft(), tableRow, field);
            } else {
                System.out.println("peto aqui 2");
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
                if (actual.getChildLeft() == null && actual.getChildRight() == null) {
                    if (actual.getParent() != null) {
                        if (right) {
                            actual.getParent().setChildRight(null);

                        } else {
                            actual.getParent().setChildLeft(null);

                        }
                        size--;
                        calculHeight(actual.getParent(), true);
                    }

                } else {
                    if (actual.getChildLeft() != null) {
                        actual.setRoot(buscaQuin(actual.getChildLeft(), false));
                        if (actual.getParent() != null) {
                            actualitzaCalculBalanceig(right, actual.getParent().getChildRight(), actual.getParent().getChildLeft());
                        } else {
                            reCalculHeigh(actual);
                            size--;
                            calculHeight(actual, true);
                        }

                    } else {
                        if (actual.getParent() != null) {
                            actual.getParent().setChildRight(actual.getChildRight());
                            actual.getChildRight().setParent(actual.getParent());
                            size--;
                            actualitzaCalculBalanceig(right, actual.getParent().getChildRight(), actual.getParent().getChildLeft());
                        } else {
                            root.setRoot(root.getChildRight().getRoot());
                            root.setChildRight(null);
                            size--;
                            calculHeight(root, true);
                        }


                    }
                }
            } else {
                whatToDelete(actual.getChildRight(), field, value, true);
            }
        } else {
            whatToDelete(actual.getChildLeft(), field, value, false);
        }



    }

    private void actualitzaCalculBalanceig (Boolean right, NodeAVL childRight, NodeAVL childLeft) {
        if (right) {
            calculHeight(childRight, true);
        } else {
            calculHeight(childLeft, true);
        }
    }

    private TableRow buscaQuin(NodeAVL childLeft, Boolean right) {
        TableRow what;
        if (childLeft.getChildRight() != null) {
            what = buscaQuin(childLeft.getChildRight(), true);
        } else {
            if (right) {
                if (childLeft.getParent().getParent() != null) {
                    childLeft.getParent().setChildRight(null);
                } else {
                    childLeft.getParent().setChildRight(null);
                    movimentRR(childLeft.getParent(), true);
                }

            } else {
                if (childLeft.getParent().getParent() != null) {
                    childLeft.getParent().setChildLeft(childLeft.getChildLeft());
                } else {
                    childLeft.getParent().setChildLeft(null);
                    movimentLL(childLeft.getParent(), true);
                }

            }
            what = childLeft.getRoot();
        }
        return what;
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

    private void recolocate (NodeAVL actual, NodeAVL find) {
        int where = actual.getRoot().compareTo(index, find.getRoot());

        if (where < 1) {
            if (where == 0) {
                reCalculHeigh(actual);

            } else {
                if (actual.getChildRight() != null) {
                    recolocate(actual.getChildRight(), find);
                } else {
                    actual.setChildRight(find);
                    actual.getChildRight().setParent(actual);
                    if (actual.getChildLeft() != null) {
                        actual.getChildLeft().getParent().setChildRight(find);
                    }
                    reCalculHeigh(actual);
                }

            }

        } else {
            if (actual.getChildLeft() != null) {
                recolocate(actual.getChildLeft(), find);
            } else {
                actual.setChildLeft(find);
                actual.getChildLeft().setParent(actual);
                if (actual.getChildRight() != null) {
                    actual.getChildRight().getParent().setChildLeft(find);
                }
                reCalculHeigh(actual);
            }

        }
    }

}
