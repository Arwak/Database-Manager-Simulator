package Estructures;

import DBMSi.TableRow;

/**
 * Created by ClaudiaPeiro on 21/6/17
 *
 * Classe que gestionarà els nodes d'AVL.AVL. Contindrà la informació relativa a la fila
 * i cursors als fills esquerres i dretes respectivament.
 */
public class NodeAVL {
    private TableRow root;
    private NodeAVL childRight, childLeft, parent;
    private int balance;
    private int height;


    public NodeAVL(TableRow root) {
        this.root = root;
        balance = 0;
        height = 0;
        childRight = null;
        childLeft = null;
        parent = null;
    }

    public NodeAVL(NodeAVL node) {
        this.root = node.getRoot();
        balance = 0;
        height = 0;
        childRight = null;
        childLeft = null;

        if (node.getChildRight() != null) {
            childRight = node.getChildRight();
            node.getChildRight().setParent(this);
        }
        if (node.getChildLeft() != null) {
            childLeft = node.getChildLeft();
            node.getChildLeft().setParent(this);
        }

        parent = null;
    }


    public TableRow getRoot() {
        return root;
    }

    public void setRoot(TableRow row) {
        this.root = row;
    }

    public NodeAVL getParent () {
        return parent;
    }

    public NodeAVL getChildRight() {
        return childRight;
    }

    public void setChildRight(NodeAVL childRight) {
        this.childRight = childRight;
    }

    public NodeAVL getChildLeft() {
        return childLeft;
    }

    public void setChildLeft(NodeAVL childLeft) {
        this.childLeft = childLeft;
    }

    public void setParent (NodeAVL parent) { this.parent = parent; }

    public int getHeight () {
        return height;
    }

    public void setHeight (int height) {
        this.height = height;
    }

    public void setBalance (int balance) {
        this.balance = balance;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append(root.toString());
        sb.append("Height: ").append(height);
        sb.append(" Parent: ");
        if (parent != null) {
            sb.append(parent.getRoot().toString());
        } else {
            sb.append("-\t\t\t");
        }
        sb.append("Child Right: ");
        if (childRight != null) {
            sb.append(childRight.getRoot().toString());
            sb.append("Height: ").append(childRight.getHeight()).append(" ");
        } else {
            sb.append("-\t\t\t");
            sb.append("Height: 0 ");
        }
        sb.append("Child Left: ");
        if (childLeft != null) {
            sb.append(childLeft.getRoot().toString());
            sb.append("Height: ").append(childLeft.getHeight()).append(" ");
        } else {
            sb.append("-\t\t\t");
            sb.append("Height: 0 ");
        }

        return sb.toString();
    }

}
