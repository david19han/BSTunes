/*
 * Shannon Duvall
 * Class UI
 * This class runs the interaction loop with the user.
 * It assumes that BST class holds the Binary Search Tree methods
 * and BSTNode holds the node methods.
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.lang.reflect.*;


public class UI {

	

	// This holds the character to tree mapping for input from the user
	HashMap<Character,BST> letterToTree = new HashMap<Character, BST>();
	
	
	public UI(){
		// You can search based on 3 criteria: track name, artist, and genre.
		// Java 8 Rocks!
		BST nameTree = new BST(Comparator.comparing(Track::getTitle));
		BST artistTree = new BST(Comparator.comparing(Track::getArtist));
		BST genreTree = new BST(Comparator.comparing(Track::getGenre));
		letterToTree.put('T', nameTree);
		letterToTree.put('A', artistTree);
		letterToTree.put('G', genreTree);
		buildTrees();
	}
	
	/*
	 * Builds the Original BSTs from the input xml file.
	 */
	public void buildTrees(){
		// get tracks from local file
		ITunesPlaylistReader reader = new ITunesPlaylistReader();
		reader.read("library.xml");

		// print the titles out just to show we can :)
		List<Track> tracks = reader.getTracks();

		for (int k = 0; k < tracks.size(); k++)
		{
			Track t = tracks.get(k);
			System.out.println("Track " + (k + 1) + " " + t.getTitle());
			for(Character type: letterToTree.keySet()){
				letterToTree.get(type).insert(t);
			}
		}
		
		// You could add a call to a tree walk here if you wanted to make sure the trees are built correctly.
	}

	/*
	 * This method does the main interaction loop.  It gives the user options and 
	 * quits on "Q".
	 */
	public void interact(){
		// Make a map with the menu items.
		HashMap<Character,String> letterToMethod = new HashMap<Character,String>();
		letterToMethod.put('S', "search");
		letterToMethod.put('L', "list");
		letterToMethod.put('F', "first");
		letterToMethod.put('E', "end");
		letterToMethod.put('I', "insert");
		letterToMethod.put('D', "delete");
		char answer = ' ';
		Scanner scan = new Scanner(System.in);
		// Answer being Q means user wants to quit the interaction.
		while(answer!='Q'){
			System.out.println("Enter: \n 'S' to search \n "
					+ "'L' for a complete listing \n "
					+ "'F' for first track \n "
					+ "'E' for ending track \n "
					+ "'I' to insert a track \n "
					+ "'D' to delete a track \n "
					+ "'Q' to quit:");
			answer = scan.nextLine().toUpperCase().charAt(0);
			// Dummy default type
			char type = 'X';
			// 'I' is for insert, and inserting happens into all existing trees, not just one type.
			if(answer != 'Q'&& answer != 'I'){
				System.out.println("Enter 'T' for title, 'A' for artist, or 'G' for genre: ");
				type = scan.nextLine().toUpperCase().charAt(0);
			}
			if(answer != 'Q'){
				for(Character letter: letterToMethod.keySet()){
					// Use Reflection to call the method
					if(answer == letter){
						String methodName = letterToMethod.get(answer);
						Method[] toRun = this.getClass().getMethods();
						for (Method m: toRun){
							if(m.getName().equals(methodName)){
								try {
									m.invoke(this, new Object[]{scan,type});
								} catch (IllegalAccessException
										| IllegalArgumentException
										| InvocationTargetException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		scan.close();
	}
	
	/*
	 * Search does the job of searching.  The
	 * scanner is given for input and the type 
	 * is a character that should correspond to 
	 * a key in the letterToTree instance variable.
	 * It looks in the correct tree for a specific item 
	 * and prints all matching tracks.
	 */
	public void search(Scanner scan, char type){
		System.out.println("Enter the search term: ");
		String searchTerm = scan.nextLine().trim();
		BST tree = letterToTree.get(type);
		Track dummy = makeDummyTrack(searchTerm, type);
		//System.out.println(searchTerm);
		//System.out.println(tree.myRoot.getTrack().getTitle());
		//System.out.println(dummy.getTitle());
		BSTNode found = tree.treeSearch(dummy);
		if(found == null) System.out.println("No matches found.");
		else{
			for(Track track: found.tracks){
				track.prettyPrint();
			}
		}
	}
	
	/*
	 * First finds the minimal value in the tree.  
	 * The scanner is given for input and the type 
	 * is a character that should correspond to 
	 * a key in the letterToTree instance variable.
	 */
	public void first(Scanner scan, char type){
		BST tree = letterToTree.get(type);
		BSTNode first = tree.treeMin(tree.myRoot);
		for(Track t: first.tracks){
			t.prettyPrint();
		}
	}
	
	/*
	 * End finds the maximal value in the tree.  
	 * The scanner is given for input and the type 
	 * is a character that should correspond to 
	 * a key in the letterToTree instance variable.
	 */
	public void end(Scanner scan, char type){
		BST tree = letterToTree.get(type);
		BSTNode end = tree.treeMax(tree.myRoot);
		for(Track t: end.tracks){
			t.prettyPrint();
		}
	}
	
	/*
	 * Insert prompts the user for track information
	 * and adds it to all trees.  The 'type' variable
	 * is not used.  It is added as a parameter for 
	 * uniformity with the other menu functions.
	 */
	public void insert(Scanner scan, char type){
		
		System.out.println("Enter the track's artist:");
		String artist = scan.nextLine().trim();
		System.out.println("Enter the track's title:");
		String title = scan.nextLine().trim();
		System.out.println("Enter the track's album name:");
		String album = scan.nextLine().trim();
		System.out.println("Enter the track's genre:");
		String genre = scan.nextLine().trim();
		System.out.println("Enter the track's year:");
		int year = scan.nextInt();
		System.out.println("Enter the track's size:");
		int size = scan.nextInt();
		System.out.println("Enter the track's time:");
		int time = scan.nextInt();
		System.out.println("Enter the track's bit rate:");
		int bitrate = scan.nextInt();
		System.out.println("Enter the track's ID number:");
		int id = scan.nextInt();
		String throwAway = scan.nextLine();
		System.out.println();
		Track t = new Track(artist,
                  title,
                  album,
                  genre,
                  year,
                  size,
                  time,
                  bitrate,
                  id);
		for(BST tree:letterToTree.values()){
			tree.insert(t);
		}
	}
	
	/*
	 * This method does the job of listing.  The
	 * scanner is given for input and the type 
	 * is a character that should correspond to 
	 * a key in the letterToTree instance variable.
	 * It does an inorder Tree walk on the specified
	 * tree so that all tracks are listed in order.
	 */
	public void list(Scanner scan, char type){
		BST tree = letterToTree.get(type);
		tree.inorderTreeWalk();
	}
	
	/*
	 * Delete does the job of deleting.  The
	 * scanner is given for input and the type 
	 * is a character that should correspond to 
	 * a key in the letterToTree instance variable.
	 * It looks in the correct tree for the specific item 
	 * and deletes matching tracks from all trees.
	 */
	public void delete(Scanner scan, char type){
		BST tree = letterToTree.get(type);
		System.out.println("Enter the track to delete: ");
		String searchTerm = scan.nextLine().trim();
		// I need a track to search for.
		Track dummy = makeDummyTrack(searchTerm, type);
		BSTNode node = tree.treeSearch(dummy);
		ArrayList<Track> matches = node.tracks;
		tree.deleteNode(node);
		for(Track t: matches){
			System.out.println("DELETING!");
			t.prettyPrint();
			for(BST bst:letterToTree.values()){
				bst.delete(t);
			}
		}		
	}
	
	/*
	 * This method makes a dummy track with one piece of information only.  It
	 * is used to find a node with a certain key within a BST.
	 */
	public Track makeDummyTrack(String key, char type){
		if (type == 'T'){
			return new Track("Artist",key,"Album","Genre",0,0,0,0,0);
		}
		else if (type == 'A'){
			return new Track(key,"Title","Album","Genre",0,0,0,0,0);
		}
		else if (type == 'G'){
			return new Track("Artist","Title","Album",key,0,0,0,0,0);
		}
		return null;
	}
	
}