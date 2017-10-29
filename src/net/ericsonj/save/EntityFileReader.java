package net.ericsonj.save;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ejoseph
 * @param <E>
 */
public abstract class EntityFileReader<E> implements RunnableBatch<E>, AutoCloseable {

    private static final Logger LOG = Logger.getLogger(EntityFileReader.class.getName());

    private final File file;
    private BufferedReader br;
    private FileReader fr;
    private String currentLine;
    private int lineNumber;

    public EntityFileReader(File file) throws FileNotFoundException {
        this.file = file;
        this.br = null;
        this.fr = null;
        readFile();
    }

    private void readFile() throws FileNotFoundException {
        fr = new FileReader(file);
        br = new BufferedReader(fr);
    }

    @Override
    public boolean hasMoreElements() {
        try {
            boolean hme = (currentLine = br.readLine()) != null;
            if (hme) {
                this.lineNumber++;
            }
            return hme;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public E getEntity() {
        return getLine2Entity(currentLine);
    }

    public abstract E getLine2Entity(String line);

    @Override
    public void close() throws IOException {
        this.lineNumber = 0;
        if (br != null) {
            br.close();
        }
        if (fr != null) {
            fr.close();
        }
        LOG.log(Level.INFO, "Close Entity file reader");
    }

    public int getLineNumber() {
        return lineNumber;
    }

}
