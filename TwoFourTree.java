public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        
        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;          
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        public boolean isTwoNode() {
            return values == 1;
        }

        public boolean isThreeNode() {
            return values == 2;
        }

        public boolean isFourNode() {
            return values == 3;
        }

        public boolean isRoot() {
            return parent == null;
        }

        public TwoFourTreeItem(int value1) {
            values = 1;
            this.value1 = value1;
        }

        public TwoFourTreeItem(int value1, int value2) {
            values = 2;
            this.value1 = value1;
            this.value2 = value2;
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
            values = 3;
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        public void organizeValues(){
            //Checks Node and organizes the values from least to greatest
            if(isTwoNode()){
                value1 = value1;
            }
            else if(isThreeNode()){
                int tempMin = min(value1, value2);
                int tempMax = max(value1, value2);
                
                value1 = tempMin;
                value2 = tempMax;
            }
            else if(isFourNode()){
                int tempMin = min(value1, min(value2, value3));
                int tempMax = max(value1, max(value2, value3));
                int tempMid = value1 + value2 + value3 - tempMin - tempMax;

                value1 = tempMin;
                value2 = tempMid;
                value3 = tempMax;
            }
            else{
                System.err.println("\nOrganizeValues() Failed");
            }
        }

        public boolean hasValue(int value){
            if(value1 == value || value2 == value || value3 == value){
                return true;
            }
            else{
                return false;
            }
        }

        public TwoFourTreeItem findSubRoot(int value){
            //Returns the node that may contain the given value
            if(hasValue(value)){
                return this;
            }
            else if(isLeaf){
                return null;
            }
            else{
                if(this.isTwoNode()){
                    if(value < this.value1)
                        return this.leftChild;
                    else
                        return this.rightChild;
                }
                else if(this.isThreeNode()){
                    if(value < this.value1)
                        return this.leftChild;
                    else if(value < this.value2)
                        return this.center;
                    else
                        return this.rightChild;
                }
                else{
                    if(value < this.value1)
                        return this.leftChild;
                    else if(value < this.value2)
                        return this.centerLeftChild;
                    else if(value < this.value3)
                        return this.centerRightChild;
                    else
                        return this.rightChild;
                }
            }
        }

        public TwoFourTreeItem splitNode(){
            if(this.isFourNode()){
                TwoFourTreeItem leftNode = new TwoFourTreeItem(value1);
                leftNode.leftChild = this.leftChild;
                leftNode.rightChild = this.centerLeftChild;
                leftNode.parent = this.parent;
                leftNode.isLeaf = this.isLeaf;

                TwoFourTreeItem rightNode = new TwoFourTreeItem(value3);
                rightNode.leftChild = this.centerRightChild;
                rightNode.rightChild = this.rightNode;
                rightNode.parent = this.parent;
                rightNode.isLeaf = this.isLeaf;

                if(isRoot()){
                    TwoFourTreeItem rootNode = new TwoFourTreeItem(value2);
                    rootNode.leftChild = leftNode;
                    rootNode.rightChild = rightNode;
                    rootNode.isLeaf = false
                    leftNode.parent = rootNode;
                    rightNode.parent = rootNode;
                    return rootNode;
                }
                else{
                    if(parent.isTwoNode()){
                        parent.value2 = this.value2;
                        parent.values = 2;
                        parent.organizeValues()
                        if(parent.value1 == this.value2){
                            parent.leftChild = leftNode;
                            parent.centerChild = rightNode;
                        }
                        else{
                            parent.centerChild = leftNode;
                            parent.rightChild = rightNode; 
                        }
                    }
                    else if(parent.isThreeNode()){
                        parent.value3 = this.value2;
                        parent.values = 3;
                        parent.organizeValues();
                        if(parent.value1 == this.value2){
                            parent.leftChild = leftNode;
                            parent.centerLeftChild = rightNode;
                            parent.centerRightChild = parent.center;
                            parent.center = null;
                        }
                        else if(parent.value2 = this.value2){
                            parent.centerLeftChild = leftNode;
                            parent.centerRightChild = rightNode;
                            parent.center = null;
                        }
                        else{
                            parent.centerLeftChild = parent.center;
                            parent.center = null;
                            parent.centerRightChild = leftNode;
                            parent.rightChild = rightNode;
                        }
                    }
                    return parent;
                }

            }
        }

        private void printIndents(int indent) {
            for(int i = 0; i < indent; i++) System.out.printf("  ");
        }

        public void printInOrder(int indent) {
            if(!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if(isThreeNode()) {
                if(!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if(isFourNode()) {
                if(!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if(!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if(!isLeaf) rightChild.printInOrder(indent + 1);
        }
    }

    TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        if(root == null){
            root = new TwoFourTreeItem(value);
        }
        else if(!this.hasValue()){
            TwoFourTreeItem temp = root;
            while(!temp.isLeaf){
                temp = temp.findSubRoot(value);
                if(temp.isFourNode()){
                    if()
                    temp = temp.splitNode();
                }
            }
            if(temp.isRoot() && temp.isFourNode()){
                root = temp.splitNode;
                temp = root;
            }

        }
        return false;
    }

    public boolean hasValue(int value) {
        TwoFourTreeItem temp = root;
        while(temp != null){
            //If value is in node then return True
            if(temp.hasValue(value))
                return true;
            //Else find possible subroot and repeat check
            else
                temp = temp.findSubRoot(value);
        }
        //If temp == null then value does not exist in tree
        return false;
    }

    public boolean deleteValue(int value) {
        return false;
    }

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {

    }
}
