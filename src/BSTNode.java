import java.util.ArrayList;


public class BSTNode {
	public BSTNode parent;
	public BSTNode leftChild;
	public BSTNode rightChild;
	public ArrayList<Track> tracks;
	public int id;
	
	public BSTNode(int idNum){
		tracks = new ArrayList<Track>();
		id = idNum;
	}
	
	public BSTNode(BSTNode p, Track t, int idNum){
		this(idNum);
		parent = p;
		tracks.add(t);
	}
	
	
	
	public ArrayList<Track> getTracks(){
		return tracks;
	}
	
	public Track getTrack(){
		return tracks.get(0);
	}
	
	public void addTrack(Track t){
		tracks.add(t);
	}
	
    public void printOut(){
        System.out.println("My id is "+id);
        System.out.println("My track is: ");
        tracks.get(0).prettyPrint();
        if(leftChild == null){
            System.out.println("No left child");
        }
        else{
            System.out.println("My left child is "+leftChild.id);
        }
        if(rightChild == null){
            System.out.println("No right child");
        }
        else{
            System.out.println("My right child is "+rightChild.id);
        }
    }
    
    public boolean contains(Track item){
        for(int i =0; i<tracks.size(); i++){
            Track t = tracks.get(i);
            if (t.equals(item)){
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(String key){
    	for (Track t: tracks){
    		if (t.getAlbum().equals(key) || t.getArtist().equals(key) || t.getGenre().equals(key)){
    			return true;
    		}
    	}
    	return false;
    }
    
    public void deleteTrack(Track item){
        int index = -1;
        for(int i =0; i<tracks.size(); i++){
            Track t = tracks.get(i);
            if (t.equals(item)){
                index = i;
            }
        }
        if(index != -1){
            tracks.remove(index);
        }
    }
    
    public int numTracks(){
        return tracks.size();
    }
}

