
/**
 * @author: Ashwin Kamalakannan
 * last Edited: October 28, 2019
 * 
 * Contains the implementations of the overriden methods from the FileInterface
 * inferface. Will be used for all server side logic.
 */

import java.io.*;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class FileImpl extends UnicastRemoteObject implements FileInterface {

    private String name;
    private final String dataFolder = "songs/";

    /**
     * Constructor for the class. Calls super class methods and sets the name of the
     * interface.
     * 
     * @param s
     * @throws RemoteException
     */
    public FileImpl(String s) throws RemoteException {
        super();
        name = s;
    }

    /**
     * Checks data folder for the specified song, reads it in using a buffered input
     * stream and converts it to a byte array to be returned. Used by client to
     * download songs to their local data folder.
     * 
     * @param fileName
     * @return byte[]
     */
    public byte[] downloadSong(String fileName) {
        try {
            File file = new File(dataFolder + fileName);
            byte[] buffer = new byte[(int) file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(dataFolder + fileName));
            System.out.println(super.getClientHost() + " has requested for file '" + fileName + "'");
            input.read(buffer, 0, buffer.length);
            input.close();
            return (buffer);
        } catch (Exception e) {
            System.out.println("FileImpl download error: " + e.getMessage());
            e.printStackTrace();
            return (null);
        }
    }

    /**
     * File arrives in a byte array, converts to a new file and writes through a
     * buffered output stream to the data folder with the specified name. Used by
     * the client to upload new tracks to the playlist.
     * 
     * @param content, FileName
     */
    public void uploadSong(byte[] content, String fileName) {
        try {
            File file = new File(fileName);
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(dataFolder + file));
            System.out.println(super.getClientHost() + " has uploaded file '" + fileName + "'");
            output.write(content);
            output.close();
        } catch (Exception e) {
            System.out.println("FileImpl upload error: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    /**
     * Deletes specified file from data folder. Used by client to delete tracks from
     * playlist.
     * 
     * @param fileName
     */
    public void deleteSong(String fileName) {
        try {
            System.out.println("Deleting: " + fileName);
            new File(dataFolder + fileName).delete();
        } catch (Exception e) {
            System.out.println("FileImpl delete error: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    /**
     * Reads contents of data folder into an arraylist and returns it. Used by
     * client to stay in sync when new tracks are added the server playlist.
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> checkAvailableSongs() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            for (File fileEntry : new File(dataFolder).listFiles()) {
                list.add(fileEntry.getName());
            }
        } catch (Exception e) {
            System.out.println("FileImpl check available songs error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Gets image from an api given a size & keyword. Converts the image to a byte
     * array and returns. Used by client to get the album art to display on the
     * window.
     * 
     * @param size
     * @return byte[]
     */
    public byte[] downloadImage(int size) {
        try {
            BufferedImage image = ImageIO
                    .read(new URL("https://loremflickr.com/" + size + "/" + size + "/post+malone"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] buffer = baos.toByteArray();
            return (buffer);
        } catch (Exception e) {
            System.out.println("FileImpl image download error: " + e.getMessage());
            e.printStackTrace();
            return (null);
        }
    }
}