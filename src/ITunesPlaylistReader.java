import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;


public class ITunesPlaylistReader
{
    private ITunesXMLHandler myHandler;

    /**
     * Create a basic reader.
     */
    public ITunesPlaylistReader ()
    {
        try
        {
            myHandler = new ITunesXMLHandler();
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }

    /**
     * Read ITunes data from a given file.
     */
    public void read (String filename)
    {
        try
        {
            myHandler.read(filename);
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }

    /**
     * Write ITunes data to a given file.
     */
    public void write (String filename)
    {
        try
        {
            myHandler.write(filename);
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }

    /**
     * Get all the tracks defined in the files read so far.
     */
    public List getTracks ()
    {
        return getPlaylist("Tracks");
    }

    /**
     * If the ITunes data includes playlists, get only those tracks that
     * are part of the given playlist.  If the given playlist does not
     * exist, an empty list is returned.
     */
    public List getPlaylist (String name)
    {
        List results = new ArrayList();

        Map tracks = (Map)(myHandler.getFirstElement().get(name));
        Iterator iter = tracks.values().iterator();
        while (iter.hasNext())
        {
            Map trackInfo = (Map)iter.next();
            results.add(new Track(
                (String)trackInfo.get("Artist"),
                (String)trackInfo.get("Name"),
                (String)trackInfo.get("Album"),
                (String)trackInfo.get("Genre"),
                ((Integer)trackInfo.get("Year")).intValue(),
                ((Integer)trackInfo.get("Size")).intValue(),
                ((Integer)trackInfo.get("Total Time")).intValue(),
                ((Integer)trackInfo.get("Bit Rate")).intValue(),
                ((Integer)trackInfo.get("Track ID")).intValue()));
        }

        return results;
    }

    /*
     * This class does all the hard work ...
     *
     * Notes:
     *
     * The parsing generates a list of all the elements in the XML structure.
     * Each structure has a Map for every <dict> element, a List for every 
     * <array> element, and a string for every tag and attribute.
     * 
     * Possible Bugs:
     *
     *  - Specifics of various formats are not currently dealt with at all.
     *    For example, <data> elements are supposed to be base64 encoded, but
     *    it is ignored for the moment.
     *  - It may be possible for a <plist> to have more than one <dict> or
     *    <array> element. Presently it is assumed there is only one.
     */
    private class ITunesXMLHandler extends DefaultHandler
    {
        private XMLReader myReader;
        private String myText;
        private Stack myVariables;
        private Stack myKeys;
        private List myElements;

        
        public ITunesXMLHandler () throws Exception
        {
            myReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            myReader.setContentHandler(this);
            myReader.setErrorHandler(this);
        }


        public void read (String filename) throws Exception
        {
            myReader.parse(new InputSource(new FileReader(filename)));
        }

        public void write (String filename) throws Exception
        {
            FileWriter out = new FileWriter(new File(filename));
            // write header
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            out.write("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
            out.write("<plist version=\"1.0\">\n");
            out.write("<dict>\n");
            // made up header data
            out.write("<key>Major Version</key><integer>1</integer>\n");
            out.write("<key>Minor Version</key><integer>1</integer>\n");
            out.write("<key>Application Version</key><string>4.5</string>\n");
            out.write("<key>Music Folder</key><string>file://localhost/Music/iTunes/iTunes%20Music/</string>\n");
            out.write("<key>Library Persistent ID</key><string>F3349961DC433C7B</string>\n");
            out.write("<key>Tracks</key>\n");
            out.write("<dict>\n");
            // write tracks
            List tracks = getPlaylist("Tracks");
            Iterator iter = tracks.iterator();
            while (iter.hasNext())
            {
                Track track = (Track)iter.next();
                out.write("<key>" + track.getID() + "</key>\n");
                out.write("<dict>\n");
                out.write("<key>Track ID</key><integer>" + track.getID() + "</integer>\n");
                out.write("<key>Name</key><string>" + track.getTitle() + "</string>\n");
                out.write("<key>Artist</key><string>" + track.getArtist() + "</string>\n");
                out.write("<key>Album</key><string>" + track.getAlbum() + "</string>\n");
                out.write("<key>Genre</key><string>" + track.getGenre() + "</string>\n");
                out.write("<key>Year</key><string>" + track.getYear() + "</string>\n");
                out.write("<key>Size</key><string>" + track.getSize() + "</string>\n");
                out.write("<key>Total Time</key><integer>" + track.getTime() + "</integer>\n");
                out.write("<key>Bit Rate</key><integer>" + track.getBitRate() + "</integer>\n");
                out.write("</dict>\n");
            }
            out.write("</dict>");
            out.write("</dict>");
            out.write("</plist>");
            out.close();
        }

        public void startDocument ()
        {
            myVariables = new Stack();
            myKeys = new Stack();
            myElements = new ArrayList();
            myText = "";
        }

        public void endDocument ()
        {}

        public void startElement (String uri, String name, String qName, Attributes atts)
        {
            if (name.equals("dict"))
            {
                myVariables.push(new TreeMap());
            }
            else if (name.equals("array"))
            {
                myVariables.push(new ArrayList());
            }
            myText = "";
        }

        public void endElement (String uri, String name, String qName)
        {
            if (name.equals("key"))
            {
                myKeys.push(myText);
            }
            else
            {
                Object o;
                if (name.equals("dict") || name.equals("array"))
                {
                    // just finished array or dict, then pop it and add it to parent
                    o = myVariables.pop();
                    if (myVariables.empty())
                    {
                        // if no parent, just add it to top level
                        myElements.add(o);
                        return;
                    }
                }
                else if (name.equals("integer"))
                {
                    o = new Integer(Integer.parseInt(myText));
                }
                else if (name.equals("real"))
                {
                    o = new Double(Double.parseDouble(myText));
                }
                else if (name.equals("true") || name.equals("false"))
                {
                    o = Boolean.valueOf(name);
                }
                else // if (name.equals("date") || name.equals("string") || name.equals("data"))
                {
                    // add date, string, and data values as strings
                    // this is not always the right thing to do because 
                    // <data> elements really have base64 encoded data
                    // but since nothing is being done with it,
                    // a string representation is fine
                    o = myText;
                }

                if (myVariables.empty() && name.equals("plist"))
                {
                    // if stack is empty and <plist> is ending, do nothing more
                    return;
                }
                else if (myVariables.empty())
                {
                    System.out.println("Unexpected " + name + " when myVariables "
                                       + "was empty.");
                    System.exit(1);
                }

                if (myVariables.peek() instanceof Map)
                {
                    ((TreeMap)myVariables.peek()).put(myKeys.pop(), o);
                }
                else if (myVariables.peek() instanceof List)
                {
                    ((ArrayList)myVariables.peek()).add(o);
                }
            }
            myText = "";
        }

        public Map getFirstElement ()
        {
            return (Map)myElements.get(0);
        }

        public void characters (char ch[], int start, int length)
        {
            // ch[] actually ends up being a vector of all of the characters
            // parsed in any segment, so we just add the characters which
            // are between start and start+length to the myText string,
            // which are the characters that weren't part of a tag or
            // attribute:
            for (int k = start; k < length + start; k++ )
            {
                myText += ch[k];
            }
        }

        private String replace (String arg)
        {
            // convert UNICODE characters to plain text
            if (arg == null) return "null";
            else             return arg.replaceAll("&", "&#38;");
        }
    }

    // for testing
    public static void main (String args[]) throws Exception
    {
        ITunesPlaylistReader player = new ITunesPlaylistReader();

        player.read("library.xml");
        player.write("output.xml");
    }
}
