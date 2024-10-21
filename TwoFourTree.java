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
            this.value1 = value1;
            organizeValues();
        }

        public TwoFourTreeItem(int value1, int value2) {
            values = 2;
            this.value1 = value1;
            this.value2 = value2;
            organizeValues();
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
            values = 3;
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            organizeValues();
        }

        public void organizeValues(){
            //Checks Node and organizes the values from least to greatest
            if(this.isTwoNode()){
                return;
            }
            else if(this.isThreeNode()){
                int tempMin = Math.min(value1, value2);
                int tempMax = Math.max(value1, value2);
                
                value1 = tempMin;
                value2 = tempMax;
                return;
            }
            else if(this.isFourNode()){
                int tempMin = Math.min(value1, Math.min(value2, value3));
                int tempMax = Math.max(value1, Math.max(value2, value3));
                int tempMid = value1 + value2 + value3 - tempMin - tempMax;

                value1 = tempMin;
                value2 = tempMid;
                value3 = tempMax;
                return;
            }
            else{
                System.err.println("\nOrganizeValues() Failed");
                return;
            }
        }

        public void addValue(int value){
            if(this.isTwoNode()){
                this.value2 = value;
                this.values = 2;
            }
            else if(this.isThreeNode()){
                this.value3 = value;
                this.values = 3;
            }
            else{
                System.out.println("Node is full");
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

        public TwoFourTreeItem returnSubChild(int value){
            //Returns the node that may contain the given value
            if(this.hasValue(value)){
                return this;
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
                else if(this.isFourNode()){
                    if(value < this.value1)
                        return this.leftChild;
                    else if(value < this.value2)
                        return this.centerLeftChild;
                    else if(value < this.value3)
                        return this.centerRightChild;
                    else
                        return this.rightChild;
                }
                else{
                    return null;
                }
            }
        }

        public void assignIsLeaf(){
            if(leftChild == null && centerRightChild == null && centerChild == null && centerLeftChild == null && rightChild == null){
                this.isLeaf = true;
            }
            else{
                this.isLeaf = false;
            }
        }

        public void assignChildrenParent(){
            //Assign children's parent as this node if they exist
            if(this.leftChild != null) this.leftChild.parent = this;
            if(this.centerLeftChild != null) this.centerLeftChild.parent = this;
            if(this.centerChild != null) this.centerChild.parent = this;
            if(this.centerRightChild != null) this.centerRightChild.parent = this;
            if(this.rightChild != null) this.rightChild.parent = this;
        }

        public TwoFourTreeItem splitNode(){
            if(this.isFourNode()){
                //Create new nodes for split
                TwoFourTreeItem leftNode = new TwoFourTreeItem(this.value1);
                TwoFourTreeItem rightNode = new TwoFourTreeItem(this.value3);

                //Assign children to new nodes
                leftNode.leftChild = this.leftChild;
                leftNode.rightChild = this.centerLeftChild;
                rightNode.leftChild = this.centerRightChild;
                rightNode.rightChild = this.rightChild;

                //Assign isLeaf status
                leftNode.assignIsLeaf();
                rightNode.assignIsLeaf();

                //Point children back to node as parent
                leftNode.assignChildrenParent();
                rightNode.assignChildrenParent();

                //Execute Split if node is the root
                if(this.isRoot()){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(value2);
                    newRoot.leftChild = leftNode;
                    newRoot.rightChild = rightNode;
                    newRoot.assignIsLeaf();
                    newRoot.assignChildrenParent();
                    return newRoot;
                }
                else{
                    if(this.parent.isTwoNode()){
                        //Parent is a two node
                        if(this.parent.leftChild == this){
                            //Split node is left node of parent
                            this.parent.leftChild = leftNode;
                            this.parent.centerChild = rightNode;
                        }
                        else if(this.parent.rightChild == this){
                            //Split node is right node of parent
                            this.parent.centerChild = leftNode;
                            this.parent.rightChild = rightNode;
                        }
                        this.parent.addValue(this.value2);
                        this.parent.organizeValues();
                    }
                    else if(this.parent.isThreeNode()){
                        //Parent is a three node
                        if(this.parent.leftChild == this){
                            //Split node is left child of parent
                            this.parent.leftChild = leftNode;
                            this.parent.centerLeftChild = rightNode;
                            this.parent.centerRightChild = this.parent.centerChild;
                        }
                        else if(this.parent.centerChild == this){
                            //Split node is center child of parent
                            this.parent.centerLeftChild = leftNode;
                            this.parent.centerRightChild = rightNode;
                        }
                        else if(this.parent.rightChild == this){
                            //Split node is right child of parent
                            this.parent.centerLeftChild = this.parent.centerChild;
                            this.parent.centerRightChild = leftNode;
                            this.parent.rightChild = rightNode;
                        }
                        //Center child is removed when 3node -> 4node
                        this.parent.centerChild = null;
                        this.parent.addValue(this.value2);
                        this.parent.organizeValues();
                    }
                    this.parent.assignChildrenParent();
                    return this.parent;
                }
            }
            System.out.println("Can only split four nodes.\n");
            return this;
        }

        public boolean deleteValue(int value){
            if(this.isLeaf && !this.isTwoNode()){
                if(this.isThreeNode()){
                    if(this.value1 == value)
                        this.value1 = this.value2;
                    this.value2 = 0;
                    this.value3 = 0;
                    this.values = 1;
                }
                else if(this.isFourNode()){
                    if(this.value1 == value){
                        this.value1 = this.value2;
                        this.value2 = this.value3;
                    }
                    else if(this.value2 == value){
                        this.value2 = this.value3;
                    }
                    this.value3 = 0;
                    this.values = 2;
                }
                return true;
            }
            return false;
        }

        public TwoFourTreeItem fuseRoot(){
            if(this.isRoot()){
                if(!this.isLeaf && this.leftChild.isTwoNode() && this.leftChild.isTwoNode()){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(this.leftChild.value1,this.value2,this.rightChild.value1);

                    newRoot.leftChild = this.leftChild.leftChild;
                    newRoot.centerLeftChild = this.leftChild.rightChild;
                    newRoot.centerRightChild = this.rightChild.leftChild;
                    newRoot.rightChild = this.rightChild.rightChild;

                    newRoot.assignChildrenParent();
                    newRoot.assignIsLeaf();

                    return newRoot;
                }
            }
            return this;
        }

        // Dont Touch[
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
        // ]
    }

    TwoFourTreeItem root = null;

    public boolean addValue(int value) {//done
        TwoFourTreeItem tmp = root;
        do{
            if(tmp == null){
                root = new TwoFourTreeItem(value);
                return true;
            }
            else if(tmp.isFourNode()){
                if(tmp.isRoot()){
                    root = root.splitNode();
                    tmp = root;
                }
                else{
                    tmp = tmp.splitNode();
                }
            }
            TwoFourTreeItem next = tmp.returnSubChild(value);
            if(next != null) tmp = next;
            
        }while(!tmp.isLeaf);
        if(tmp.isFourNode()){
            tmp = tmp.splitNode();
            tmp = tmp.returnSubChild(value);
        }
        tmp.addValue(value);
        tmp.organizeValues();
        return true;
    }

    public boolean hasValue(int value) {//done
        TwoFourTreeItem temp = root;
        while(temp != null){
            //If value is in node then return True
            if(temp.hasValue(value))
                return true;
            //Else find possible subroot and repeat check
            else
                temp = temp.returnSubChild(value);
        }
        //If temp == null then value does not exist in tree
        return false;
    }

    public boolean deleteValue(int value) {
        TwoFourTreeItem curr = root;
        while(!curr.hasValue(value)){
            if(curr.isTwoNode()){
                if(curr.isRoot()){
                    root = curr.fuseRoot();
                    curr = root;
                }
                curr.enforce();
            }
            curr = curr.returnSubChild(value);
        }
        if(curr == null){
            return false;
        }
        if(!curr.deleteValue(value)){

        }
        return true;
    }

    public TwoFourTreeItem getNode(int value){
        TwoFourTreeItem curr = root;
        while (curr != null && !curr.hasValue(value)){
            curr = curr.returnSubChild(value);
        }
        return curr;
    }

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {

    }
}
