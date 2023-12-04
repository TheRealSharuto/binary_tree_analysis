import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BinaryTree {

    public Node root;

    /* First Constructor */
    public BinaryTree(String userInput) throws InvalidSyntaxException {
        // A constructor that accepts a string containing the preorder representation of a binary tree
        // and constructs a binary tree
        String firstDelimiter = "\\(";
        String secondDelimiter = "\\)";
        String thirdDelimiter = "\\*";
        userInput = userInput.replaceAll(firstDelimiter, "");
        userInput = userInput.replaceAll(secondDelimiter, "");
        userInput = userInput.replaceAll(thirdDelimiter, "");
        userInput = userInput.replaceAll("   ", " ");
        userInput = userInput.replaceAll("  ", " ");
        String[] splitUserInput = userInput.split(" ");
        List<Integer> preOrderList = new ArrayList<>();
        for (String str : splitUserInput) {
            try {
                int preOrderValue = Integer.parseInt(str);
                preOrderList.add(preOrderValue);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer: " + str);
            }
        }
        BuildBinaryTree binaryTree = new BuildBinaryTree();
        int[] pre = new int[preOrderList.size()];
        for (int i = 0; i < preOrderList.size(); i++) {
            pre[i] = preOrderList.get(i);
        }
        root = binaryTree.constructTree(pre, pre.length);
        binaryTree.printInorder(root);
    }

    private static class Node {
        int userData;
        Node left, right;

        public Node(int userData) {
            this.userData = userData;
            this.left = this.right = null;
        }
    }

    static class Index {
        int index = 0;
    }
    // Getter


    static class BuildBinaryTree {
        Index index = new Index();

        Node constructTree(int[] pre, Index preIndex,
                           int low, int high, int size) {
            // Base
            if (preIndex.index >= size || low > high) {

                return null;
            }
            // First node in preorder = root. take the node at preIndex from pre[] and make it
            // root, and increment preIndex.
            Node root = new Node(pre[preIndex.index]);

            preIndex.index = preIndex.index + 1;

            // if current array has one element, no recur
            if (low == high) {
                return root;
            }
            // Search for the first element that is greater than the root
            int i;
            for (i = low; i <= high; ++i) {
                if (pre[i] > root.userData) {
                    break;
                }
            }

            root.left = constructTree(
                    pre, preIndex, preIndex.index, i - 1, size);
            root.right = constructTree(pre, preIndex, i, high, size);

            return root;

        }

        Node constructTree(int pre[], int size) {
            return constructTree(pre, index, 0, size - 1,
                    size);
        }

        // A utility function to print inorder traversal of a
        // Binary Tree
        void printInorder(Node node) {
            if (node == null) {
                return;
            }
            printInorder(node.left);
            // Debug:
            // System.out.println(node.userData + " ");
            printInorder(node.right);
        }

    }

    public void displayIndented() {
        displayIndented(root, 0);
    }

    private void displayIndented(Node node, int indentLevel) {
        if (node != null) {
            for (int i = 0; i < indentLevel * 2; i++) {
                System.out.print(" ");
            }

            System.out.println(node.userData);
            displayIndented(node.left, indentLevel + 1);
            displayIndented(node.right, indentLevel + 1);
        }
    }

    public boolean isBinarySearchTree() {
        return isBinarySearchTree(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isBinarySearchTree(Node node, int minValue, int maxValue) {
        if (node == null) {
            return true;
        }

        if (node.userData < minValue || node.userData > maxValue) {
            return false;
        }

        return isBinarySearchTree(node.left, minValue, node.userData - 1) &&
                isBinarySearchTree(node.right, node.userData + 1, maxValue);
    }

    public boolean isBalanced() {
        return isBalanced(root) != -1;
    }

    private int isBalanced(Node node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = isBalanced(node.left);
        int rightHeight = isBalanced(node.right);

        if (leftHeight == -1 || rightHeight == -1 || Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }

        return Math.max(leftHeight, rightHeight) + 1;
    }

    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

    /* Second Constructor for BinaryTree */
    public BinaryTree(List<Integer> balancedValues) {
        int[] sortedArray = balancedValues.stream().mapToInt(Integer::intValue).toArray();
        root = buildBalancedTree(sortedArray, 0, sortedArray.length - 1);
    }

    private Node buildBalancedTree(int[] sortedArray, int start, int end) {
        if (start > end) {
            return null;
        }

        int mid = (start + end) / 2;
        Node newNode = new Node(sortedArray[mid]);
        newNode.left = buildBalancedTree(sortedArray, start, mid - 1);
        newNode.right = buildBalancedTree(sortedArray, mid + 1, end);
        return newNode;
    }

    public List<Integer> traverseBalancedTree(Node node) {
        List<Integer> result = new ArrayList<>();
        balancedTreeNodes(node, result);
        return result;
    }

    private void balancedTreeNodes(Node node, List<Integer> result) {
        if (node != null) {
            balancedTreeNodes(node.left, result);
            result.add(node.userData);
            balancedTreeNodes(node.right, result);
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            try {

                System.out.println("Please input your preordered binary tree [press N to close program]: ");
                String userInput = scan.nextLine();
                if (userInput.equalsIgnoreCase("N")) {
                    System.out.println("Exiting the program.");
                    break;
                }

                BinaryTree tree = new BinaryTree(userInput);
                tree.displayIndented();
                List<Integer> balancedValues = tree.traverseBalancedTree(tree.root);

                if (tree.isBinarySearchTree()) {
                    if (tree.isBalanced()) {
                        System.out.println("It is a balanced binary search tree");
                    } else {
                        System.out.println("It is a binary search tree but it is not balanced");
                        BinaryTree balancedTree = new BinaryTree(balancedValues);
                        System.out.println("Balanced Tree:");
                        balancedTree.displayIndented();
                        System.out.println("Original tree has height " + ((tree.getHeight()) - 1));
                        System.out.println("Balanced tree has height " + ((balancedTree.getHeight()) - 1));
                    }
                } else {
                    System.out.println("It is not a binary search tree");
                    BinaryTree balancedTree = new BinaryTree(balancedValues);
                    System.out.println("Balanced Tree:");
                    balancedTree.displayIndented();
                    System.out.println("Original tree has height " + ((tree.getHeight()) - 1));
                    System.out.println("Balanced tree has height " + ((balancedTree.getHeight()) - 1));
                }

                System.out.print("More trees? Y or N: ");
                String moreTrees = scan.nextLine();
                if (!moreTrees.equalsIgnoreCase("Y")) {
                    System.out.println("Exiting the program.");
                    break;
                }
            } catch (InvalidSyntaxException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}