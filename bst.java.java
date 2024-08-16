import java.util.ArrayList;
import java.io.*;
import java.util.*;

class Project2Testing {
    BufferedReader fileReader;
    BinarySearchTree bst;
    public static void main(String [] args) {
      Project2Testing Test = new Project2Testing("datafile.txt");
      Test.bst.print();
    }

    public Project2Testing(String filename) {
      try {
        bst = new BinarySearchTree();
        fileReader = new BufferedReader(new FileReader(filename));

        ArticleData article;
        while ((article = readNextRecord()) != null) {
          bst.addArticle(article);
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    public ArticleData readNextRecord() {
      if (fileReader == null) {
        System.out.println("Error: You must open the file first.");
        return null;
      }
      else {
        ArticleData article;
        try{
          String data = fileReader.readLine();
          if (data==null)
            return null;
          int titleId = Integer.parseInt(data);
          String title = fileReader.readLine();
          String author = fileReader.readLine();
          int numKeys = Integer.parseInt(fileReader.readLine());
          article = new ArticleData(titleId, title, author, numKeys);

          String keyword;
          for (int  i=0; i<numKeys; i++) {
            keyword = fileReader.readLine();
            article.addKeyword(keyword);
          }
          // we can add testing for space later; for now read the blank line
          fileReader.readLine();
        }
        catch(NumberFormatException e) {
          System.out.println("Error: Number expected!");
          return null;
        }
        catch(Exception e){
          System.out.println("Fatal Error: " + e);
          return null;
        }
        return article;
      }
    }
  }

  class BinarySearchTree {
      TreeNode root;

      public void addArticle(ArticleData article) {
        for (String key:article.keywords) {
          ArticleNode newArticleNode = new ArticleNode(article.id, article.title, article.author, null);
          boolean inserted = insertTreeNode(key, newArticleNode);
          System.out.printf("%s %s\n", key, inserted);
          if (!inserted) {
            // append the ArticleNode
            appendArticleNode(key, newArticleNode);
          }
        }
      }

      public void appendArticleNode(String keyword, ArticleNode newArticleNode) {
        TreeNode current = root;
        while (current != null) {
          if (keyword.compareTo(current.keyword) < 0) {
            System.out.printf("Test Left %s %s %d\n", keyword, current.keyword, newArticleNode.id);
            current = current.leftChild;
          }
          else if (keyword.compareTo(current.keyword) > 0) {
            System.out.printf("Test Right %s %s %d\n", keyword, current.keyword, newArticleNode.id);
            current = current.rightChild;
          }
          else {
            System.out.printf("Test Equal %s %s %d\n", keyword, current.keyword, newArticleNode.id);
            newArticleNode.next = current.head;
            current.head = newArticleNode;
            return;
          }
        }
      }

      public boolean insertTreeNode(String keyword, ArticleNode newArticleNode) {
        if (root == null) {
          root = new TreeNode(keyword, newArticleNode);
          return true;
        }
        else {
          TreeNode parent = null;
          TreeNode current = root;
          while (current != null) {
            if (keyword.compareTo(current.keyword) < 0) {
              System.out.printf("Test Left %s %s %d\n", keyword, current.keyword, newArticleNode.id);
              parent = current;
              current = current.leftChild;
            }
            else if (keyword.compareTo(current.keyword) > 0) {
              System.out.printf("Test Right %s %s %d\n", keyword, current.keyword, newArticleNode.id);
              parent = current;
              current = current.rightChild;
            }
            else {
              System.out.printf("Test Equal %s %s %d\n", keyword, current.keyword, newArticleNode.id);
              return false;
            }
          }
          if (keyword.compareTo(parent.keyword) < 0) {
            parent.leftChild = new TreeNode(keyword, newArticleNode);
          }
          else if (keyword.compareTo(parent.keyword) > 0) {
            parent.rightChild = new TreeNode(keyword, newArticleNode);
          }
        }
        return true;
      }
      public void print() {
        inOrderPrint(root);
      }

      private void inOrderPrint(TreeNode node) {
        if (node == null) return;
        inOrderPrint(node.leftChild);
        System.out.printf("\n%s\n", node.keyword);
        ArticleNode article = node.head;
        while (article != null) {
          System.out.printf("\t %d | %s | %s-->\n", article.id, article.title, article.author);
          article = article.next;
        }
        inOrderPrint(node.rightChild);
      }

      private void preOrderPrint(TreeNode root) {
        if (root == null) return;
        System.out.printf("%s\n\t", root.keyword);
        ArticleNode article = root.head;
        while (article != null) {
          System.out.printf("%d %s %s-->\n", article.id, article.title, article.author);
          article = article.next;
        }
        inOrderPrint(root.leftChild);
        inOrderPrint(root.rightChild);
      }
  }

  class ArticleData {
    int id;
  	String title;
  	String author;
  	ArrayList<String> keywords;

  	ArticleData(int id, String title, String author, int keywordCount){
  		this.id=id;
  		this.title=title;
  		this.author=author;
  		keywords=new ArrayList<String>(keywordCount);
  	}

    void addKeyword(String keyword) {
      keywords.add(keyword);
    }
  }

  class TreeNode {
      TreeNode leftChild;
      String keyword;
      TreeNode rightChild;
      ArticleNode head;

      public TreeNode(String key, ArticleNode rec){
          leftChild = null;
          keyword = key;
          rightChild = null;
          head = rec;
      }
  }

  class ArticleNode {
      int id;
      String title;
      String author;
      ArticleNode next;

      ArticleNode(int i, String t, String a, ArticleNode r){
          id = i;
          title = t;
          author = a;
          next = r;
      }
  }
