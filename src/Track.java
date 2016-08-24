/**
 * A Track represents one track from an album.
 *
 * Each track has a name, artist, genre, size (in bytes),
 * length (duration in milliseconds), year it was produced,
 * and album on which it appeared.
 *
 * All the tracks on an album share
 *
 *    genre, artist, album, year
 *
 * e.g., "rock", "The Beatles", "Revolver", 1966
 *
 * A track is immutable, i.e., once it has been constructed, 
 * its state cannot be changed.
 */
public class Track
{
    private String myArtist;
    private String myTitle;
    private String myAlbum;
    private String myGenre;
    private int myYear;
    private int mySize;
    private int myTime;
    private int myBitrate;
    private int myID;

    /**
     * Create a track from its constituent data.
     * @param artist author of this track
     * @param title name of this track
     * @param album album on which track appeared
     * @param genre category in which this track falls
     * @param year year in which this track was produced
     * @param size number of bytes this tracks takes up on disk
     * @param time duration in milliseconds of this track
     * @param bitrate rate at which this track was sampled
     * @param id internal id for this track
     */
    public Track (String artist,
                  String title,
                  String album,
                  String genre,
                  int year,
                  int size,
                  int time,
                  int bitrate,
                  int id)
    {
        myArtist = artist;
        myTitle = title;
        myAlbum = album;
        myGenre = genre;
        myYear = year;
        mySize = size;
        myTime = time;
        myBitrate = bitrate;
        myID = id;
    }

 	public void prettyPrint(){
 		System.out.println("************");
 		System.out.println(getArtist());
 		System.out.println(getTitle());
 		System.out.println(getAlbum());
 		System.out.println(getGenre());
 		System.out.println(getYear());
 		System.out.println(getSize());
 		System.out.println(getTime());
 	}
 
    /**
     * Return track's artist.
     */
    public String getArtist ()
    {
        return myArtist;
    }

    /**
     * Return track's title.
     */
    public String getTitle ()
    {
        return myTitle;
    }

    /**
     * Return track's album.
     */
    public String getAlbum ()
    {
        return myAlbum;
    }

    /**
     * Return track's genre.
     */
    public String getGenre ()
    {
        return myGenre;
    }

    /**
     * Return track's size in bytes.
     */
    public int getYear ()
    {
        return myYear;
    }

    /**
     * Return track's size in bytes.
     */
    public int getSize ()
    {
        return mySize;
    }

    /**
     * Return track's time in milliseconds.
     */
    public int getTime ()
    {
        return myTime;
    }

    /**
     * Return track's sample rate.
     */
    public int getBitRate ()
    {
        return myBitrate;
    }

    /**
     * Return track's id.
     */
    public int getID ()
    {
        return myID;
    }
}
