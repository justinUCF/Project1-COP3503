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
            if(isThreeNode()){
                int tempMin = Math.min(value1, value2);
                int tempMax = Math.max(value1, value2);
                
                value1 = tempMin;
                value2 = tempMax;
            }
            else if(isFourNode()){
                int tempMin = Math.min(value1, Math.min(value2, value3));
                int tempMax = Math.max(value1, Math.max(value2, value3));
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
                        return this.centerChild;
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
            if(this.isFourNode() == true){
                //Create new nodes for split
                TwoFourTreeItem leftNode = new TwoFourTreeItem(this.value1);
                TwoFourTreeItem rightNode = new TwoFourTreeItem(this.value3);
                //keep isLeaf value
                leftNode.isLeaf = this.isLeaf;
                rightNode.isLeaf = this.isLeaf;
                //Assign children to new nodes
                leftNode.leftChild = this.leftChild;
                leftNode.rightChild = this.centerLeftChild;
                rightNode.leftChild = this.centerRightChild;
                rightNode.rightChild = this.rightChild;
                //If this has non null children then reassign parents
                if(this.isLeaf == false){
                    this.leftChild.parent = leftNode;
                    this.centerLeftChild.parent = leftNode;
                    this.centerRightChild.parent = rightNode;
                    this.rightChild.parent = rightNode;
                }
                //Execute Split if node is the root
                if(this.isRoot() == true){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(value2);
                    newRoot.leftChild = leftNode;
                    newRoot.rightChild = rightNode;
                    newRoot.isLeaf = false;
                    newRoot.leftChild.parent = newRoot;
                    newRoot.rightChild.parent = newRoot;
                    return newRoot;
                }
                else{
                    if(this.parent.isFourNode() == false){
                        if(this.parent.isTwoNode() == true){
                            this.parent.value2 = this.value2;
                            this.parent.values = 2;
                            this.parent.organizeValues();
                            if(this.parent.value1 == this.value2){
                                //Split node is left node of parent
                                this.parent.leftChild = leftNode;
                                this.parent.centerChild = rightNode;
                            }
                            else{
                                //Split node is right node of parent
                                this.parent.centerChild = leftNode;
                                this.parent.rightChild = rightNode;
                            }
                            //Ensure each child has is assigned to parent
                            this.parent.leftChild.parent = this.parent;
                            this.parent.centerChild.parent = this.parent;
                            this.parent.rightChild.parent = this.parent;
                        }
                        else if(this.parent.isThreeNode() == true){
                            this.parent.value3 = this.value2;
                            this.parent.values = 3;
                            this.parent.organizeValues();
                            if(this.parent.value1 == this.value2){
                                //Split node is left child of parent
                                this.parent.leftChild = leftNode;
                                this.parent.centerLeftChild = rightNode;
                                this.parent.centerRightChild = this.parent.centerChild;
                                this.parent.centerChild = null;
                            }
                            else if(this.parent.value2 == this.value2){
                                //Split node is center child of parent
                                this.parent.centerLeftChild = leftNode;
                                this.parent.centerRightChild = rightNode;
                                this.centerChild = null;
                            }
                            else{
                                //Split node is right child of parent
                                this.parent.centerLeftChild = this.parent.centerChild;
                                this.parent.centerChild = null;
                                this.parent.centerRightChild = leftNode;
                                this.parent.rightChild = rightNode;
                            }
                            //Ensure each child has is assigned to parent
                            this.parent.leftChild.parent = this.parent;
                            this.parent.centerLeftChild.parent = this.parent;
                            this.parent.centerRightChild.parent = this.parent;
                            this.parent.rightChild.parent = this.parent;
                        }
                    }
                    return this.parent;
                }
            }
            else{
                System.out.println("Can not split non 4 nodes");
                return this;
            }
        }

        public void addValue(int value){
            if(isTwoNode()){
                this.value2 = value;
                this.values = 2;
            }
            else if(isThreeNode()){
                this.value3 = value;
                this.values = 3;
            }
            else{
                System.out.println("Node is full");
            }
            this.organizeValues();
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
            return true;
        }
        else if(root.isLeaf){
            if(root.isFourNode()){
                root = root.splitNode();
                TwoFourTreeItem temp = root.findSubRoot(value);
                temp.addValue(value); //inserts value into node if node is leaf
            }
            else{
                root.addValue(value);
            }
            return true;
        }
        else{
            TwoFourTreeItem temp = root;
            while(!temp.isLeaf){
                if(temp.isFourNode()){
                    if(temp.isRoot()){
                        temp = temp.splitNode();
                        root = temp;
                    }
                    else{
                        temp = temp.splitNode();
                    }
                }
                temp = temp.findSubRoot(value);
            }
            if(temp.isFourNode()){
                temp = temp.splitNode();
                temp = temp.findSubRoot(value);
            }
            temp.addValue(value);
            return true;
        }
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
