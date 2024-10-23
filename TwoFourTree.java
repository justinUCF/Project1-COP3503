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
            this.organizeValues();
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
            //Assign children's parent to this node if that child exists
            if(this.leftChild != null) this.leftChild.parent = this;
            if(this.centerLeftChild != null) this.centerLeftChild.parent = this;
            if(this.centerChild != null) this.centerChild.parent = this;
            if(this.centerRightChild != null) this.centerRightChild.parent = this;
            if(this.rightChild != null) this.rightChild.parent = this;
        }

        public TwoFourTreeItem getLeftSibling(){
            if(this.parent.leftChild == this) return null;
            if(this.parent.centerLeftChild == this) return this.parent.leftChild;
            if(this.parent.centerChild == this) return this.parent.leftChild;
            if(this.parent.centerRightChild == this) return this.parent.centerLeftChild;
            if(this.parent.rightChild == this){
                if(this.parent.isTwoNode()) return this.parent.leftChild;
                if(this.parent.isThreeNode()) return this.parent.centerChild;
                if(this.parent.isFourNode()) return this.parent.centerRightChild;
            }
            return null;
        }

        public TwoFourTreeItem getRightSibling(){
            if(this.parent.leftChild == this){
                if(this.parent.isTwoNode()) return this.parent.rightChild;
                if(this.parent.isThreeNode()) return this.parent.centerChild;
                if(this.parent.isFourNode()) return this.parent.centerLeftChild;
            }
            if(this.parent.centerLeftChild == this) return this.parent.centerRightChild;
            if(this.parent.centerChild == this) return this.parent.rightChild;
            if(this.parent.centerRightChild == this) return this.parent.rightChild;
            if(this.parent.rightChild == this) return null;
            return null;
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

        public int removeLeftFace(){
            int retval = 0;
            if(this.isThreeNode()){
                retval = this.value1;
                this.value1 = this.value2;
                this.value2 = 0;
                values = 1;
                this.leftChild = this.centerChild;
                this.centerChild = null;
            }
            else if(this.isFourNode()){
                retval = this.value1;
                this.value1 = this.value2;
                this.value2 = this.value3;
                this.value3 = 0;
                this.values = 2;
                this.leftChild = this.centerLeftChild;
                this.centerChild = this.centerRightChild;
                this.centerLeftChild = null;
                this.centerRightChild = null;
            }
            return retval;

        }

        public int removeRightFace(){
            int retval = 0;
            if(this.isThreeNode()){
                retval = this.value2;
                this.value2 = 0;
                this.values = 1;
                this.rightChild = this.centerChild;
                this.centerChild = null;
            }
            else if(this.isFourNode()){
                retval = this.value3;
                this.value3 = 0;
                this.values = 2;
                this.centerChild = this.centerLeftChild;
                this.rightChild = this.centerRightChild;
                this.centerLeftChild = null;
                this.centerRightChild = null;
            }
            return retval;
        }

        public TwoFourTreeItem rotateRight(TwoFourTreeItem rightSibling){
            //Rotating with right sibling
            this.centerChild = this.rightChild;
            this.rightChild = rightSibling.leftChild;
            if(this.parent.leftChild == this){
                //rightsibling is either rightchild, centerchild, centerleftchild or parent
                this.addValue(this.parent.value1);
                this.parent.value1 = rightSibling.removeLeftFace();
            }
            if(this.parent.centerChild == this || this.parent.centerLeftChild == this){
                //rightsibling is centerrightchild of parent
                this.addValue(this.parent.value2);
                this.parent.value2 = rightSibling.removeLeftFace();
            }
            if(this.parent.centerRightChild == this){
                //rightsibling is rightchild of parent
                this.addValue(this.parent.value3);
                this.parent.value3 = rightSibling.removeLeftFace();
            }
            if(this.parent.rightChild == this){
                //rightsibling is null
                return null;
            } 
            this.assignChildrenParent();
            this.parent.assignChildrenParent();
            rightSibling.assignChildrenParent();
            return this;
        }

        public TwoFourTreeItem rotateLeft(TwoFourTreeItem leftSibling){
            this.centerChild = this.leftChild;
            this.leftChild = leftSibling.rightChild;
            if(this.parent.leftChild == this){
                //leftsibling is null
                return null;
            }
            if(this.parent.centerChild == this || this.parent.centerLeftChild == this){
                //leftsibling is leftchild of parent
                this.addValue(this.parent.value1);
                this.parent.value1 = leftSibling.removeRightFace();
            }
            if(this.parent.centerRightChild == this){
                //leftsibling is centerleftchild of parent
                this.addValue(this.parent.value2);
                this.parent.value2 = leftSibling.removeRightFace();
            }
            if(this.parent.rightChild == this){
                //leftsibling is leftchild, centerchild, or centerrightchild of parent
                if(this.parent.isTwoNode()){
                    this.addValue(this.parent.value1);
                    this.parent.value1 = leftSibling.removeRightFace();
                }
                else if(this.parent.isThreeNode()){
                    this.addValue(this.parent.value2);
                    this.parent.value2 = leftSibling.removeRightFace();
                }
                else if(this.parent.isFourNode()){
                    this.addValue(this.parent.value3);
                    this.parent.value3 = leftSibling.removeRightFace();
                }
            } 
            this.assignChildrenParent();
            this.parent.assignChildrenParent();
            leftSibling.assignChildrenParent();
            return this;
        }

        public TwoFourTreeItem merge(){
            //case 1
            if((this.parent.leftChild == this || this.parent.centerChild == this) && this.parent.isThreeNode()){
                TwoFourTreeItem newFourNode = new TwoFourTreeItem(this.parent.leftChild.value1, this.parent.value1, this.parent.centerChild.value1);
                newFourNode.leftChild = this.parent.leftChild.leftChild;
                newFourNode.centerLeftChild = this.parent.leftChild.rightChild;
                newFourNode.centerRightChild = this.parent.centerChild.leftChild;
                newFourNode.rightChild = this.parent.centerChild.rightChild;

                this.parent.leftChild = newFourNode;
                this.parent.centerChild = null;
                this.parent.value1 = this.parent.value2;
                this.parent.value2 = 0;
                this.parent.values = 1;
            }
            //case 2
            if(this.parent.rightChild == this && this.parent.isThreeNode()){
                TwoFourTreeItem newFourNode = new TwoFourTreeItem(this.parent.centerChild.value1, this.parent.value2, this.parent.rightChild.value1);
                newFourNode.leftChild = this.parent.centerChild.leftChild;
                newFourNode.centerLeftChild = this.parent.centerChild.rightChild;
                newFourNode.centerRightChild = this.parent.rightChild.leftChild;
                newFourNode.rightChild = this.parent.rightChild.rightChild;

                this.parent.rightChild = newFourNode;
                this.parent.centerChild = null;
                this.parent.value2 = 0;
                this.parent.values = 1;
            }
            //case 3
            if((this.parent.leftChild == this || this.parent.centerLeftChild == this) && this.parent.isFourNode()){
                TwoFourTreeItem newFourNode = new TwoFourTreeItem(this.parent.leftChild.value1, this.parent.value1, this.parent.centerLeftChild.value1);
                newFourNode.leftChild = this.parent.leftChild.leftChild;
                newFourNode.centerLeftChild = this.parent.leftChild.rightChild;
                newFourNode.centerRightChild = this.parent.centerLeftChild.leftChild;
                newFourNode.rightChild = this.parent.centerLeftChild.rightChild;

                this.parent.leftChild = newFourNode;
                this.parent.centerChild = this.parent.centerRightChild;
                this.parent.centerLeftChild = null;
                this.parent.centerRightChild = null;
                this.parent.value1 = this.parent.value2;
                this.parent.value2 = this.parent.value3;
                this.parent.value3 = 0;
                this.parent.values = 2;
            }
            //case 4
            if((this.parent.rightChild == this || this.parent.centerRightChild == this) && this.parent.isFourNode()){
                TwoFourTreeItem newFourNode = new TwoFourTreeItem(this.parent.centerRightChild.value1, this.parent.value3, this.parent.rightChild.value1);
                newFourNode.leftChild = this.parent.centerRightChild.leftChild;
                newFourNode.centerLeftChild = this.parent.centerRightChild.rightChild;
                newFourNode.centerRightChild = this.parent.rightChild.leftChild;
                newFourNode.rightChild = this.parent.rightChild.rightChild;

                this.parent.rightChild = newFourNode;
                this.centerChild = this.parent.centerLeftChild;
                this.parent.centerLeftChild = null;
                this.parent.centerRightChild = null;
                this.parent.value3 = 0;
                this.parent.values = 2;
            }
            newFourNode.parent = this.parent;
            newFourNode.assignChildrenParent();
            newFourNode.assignIsLeaf();
            return this.parent;
        }

        public TwoFourTreeItem mergeRoot(){
            if(this.isRoot() && this.isTwoNode() && this.leftChild.isTwoNode() && this.rightChild.isTwoNode()){
                TwoFourTreeItem newRoot = new TwoFourTreeItem(this.leftChild.value1, this.value1, this.rightChild.value1);
                newRoot.leftChild = this.leftChild.leftChild;
                newRoot.centerLeftChild = this.leftChild.rightChild;
                newRoot.centerRightChild = this.rightChild.leftChild;
                newRoot.rightChild = this.rightChild.leftChild;
                newRoot.parent = this.parent;
                newRoot.assignChildrenParent();
                newRoot.assignIsLeaf();
                return newRoot;
            }
            return this;
        }

        public TwoFourTreeItem mingle(){
            if(this.isRoot()){
                return this.mergeRoot();
            }
            TwoFourTreeItem leftSibling = this.getLeftSibling();
            TwoFourTreeItem rightSibling = this.getRightSibling();
            if(!rightSibling.isTwoNode()){
                return this.rotateRight(rightSibling);
            }
            else if(!leftSibling.isTwoNode()){
                return this.rotateLeft(leftSibling);
            }
            else{
                return this.merge();
            }

        }

        public boolean removeLeafValue(int value){
            if(this.isLeaf && !this.isTwoNode() && this.hasValue(value)){
                if(value == value1){
                    value1 = value2;
                    value2 = value3;
                }
                else if(value == value2){
                    value2 = value3;
                }
                else if(value == value3){
                }
                value3 = 0;
                values--;
                return true;
            }
            return false;
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

    public TwoFourTreeItem getSuccessor(int val){
        TwoFourTreeItem curr = this.rightChild;
        while(!curr.isLeaf){
            curr = curr.returnSubChild(val);
        }
        return curr;
    }

    public TwoFourTreeItem getPredecessor(int val){
        TwoFourTreeItem curr = this.leftChild;
        while(!curr.isLeaf){
            curr = curr.returnSubChild(val);
        }
        return curr;
    }


    public boolean deleteValue(int value) {
        if(!hasValue(value)){
            return false;
        }
        TwoFourTreeItem toDelete = root;
        while(!toDelete.hasValue(value)){
            if(toDelete.isTwoNode()){
                toDelete = toDelete.mingle();
                if(toDelete.isRoot()){
                    root = toDelete;
                }
            }
            toDelete = curr.returnSubChild(value);
        }
        if(!toDelete.removeLeafValue(value)){
            TwoFourTreeItem successor = getSuccessor(value);
            if(!successor.isTwoNode()){

                toDelete = successor;
            }
            TwoFourTreeItem predecessor = getPredecessor(value);
            else if(!predecessor.isTwoNode()){

                toDelete = predecessor;
            }



        }
        return toDelete.removeLeafValue(value);
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
