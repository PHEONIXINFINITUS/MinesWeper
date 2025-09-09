import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Tile extends JLabel implements MouseListener {
    int i, j;
    boolean isRevealed;
    boolean isMined;
    boolean isFlagged;
    int numberOfBombs;
    static Clip clip;
    static
    {
        try {
            clip=AudioSystem.getClip();
        } catch (LineUnavailableException e) {
        }
    }
    public final static HashMap<String,ImageIcon> icons =new HashMap<>();
    public final static HashMap<String,File> audio =getAssests("assests/SFX");
    static 
    {
        for ( Map.Entry<String,File> entry : getAssests("assests").entrySet()) {
            String s = entry.getKey();
            ImageIcon c = new ImageIcon(entry.getValue().getAbsolutePath());
            StretchIcon ic = new StretchIcon(c.getImage(), true);
            icons.put(s, ic);
        }
    }
    static HashMap<String,File> getAssests(String pathname)
    {
        HashMap<String,File> map = new HashMap<>();
        File f = new File(pathname);
        if(!f.exists())
        {
            System.out.println("file not exists" + f.getAbsolutePath());
        }

        if(f.isDirectory())
        {
            for (File file : f.listFiles()) {
                if(file.isFile())
                {
                    String key = file.getName().substring(0, file.getName().lastIndexOf("."));
                    map.put(key, file);
                    System.out.println("adding "+key);
                }
            }
        }
        return map;
    }
    public Tile(int i, int j) {
        this.i = i;
        this.j = j;
        this.setSize(20, 20);
        this.addMouseListener(this);
        this.setIcon(icons.get("TileUnknown"));
    }
    public void setMined()
    {
        isMined=true;
        
    }
    public boolean isMined() {
        return isMined;
    }
    public boolean isRevealed() {
        return isRevealed;
    }
    public void setRevealed(boolean isRevealed) {
        if(this.isMined)
        return;
        this.isRevealed = isRevealed;
        repaint();
    }
    public void setMined(boolean isMined) {
        this.isMined = isMined;
    }
    public boolean isFlagged() {
        return isFlagged;
    }
    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
        repaint();         
    }
    public int getNumberOfBombs() {
        return numberOfBombs;
    }
    public void setNumberOfBombs(int numberOfBombs) {
        if(isMined)
        {
            this.numberOfBombs=-1;
            return;
        }
        this.numberOfBombs = numberOfBombs;
    }
    @Override
    public void repaint() {
        if(isFlagged)
        {
            this.setIcon(icons.get("TileFlag"));
        }
        else{
        if(isRevealed)
        {
            if(numberOfBombs==0)
            this.setIcon(icons.get("TileEmpty"));
            else if(numberOfBombs>0)
            this.setIcon(icons.get("Tile"+numberOfBombs));
        }
        else
        {
            // this.setIcon(icons.get("TileUnknown"));
            this.setIcon(icons.get("TileUnknown"));
        }
    }
        super.repaint();
    }
    @Override
    public void mousePressed(MouseEvent arg0){
        System.out.println("MOuuuuse pre-re-ressed");
        if(SwingUtilities.isLeftMouseButton(arg0)){
            if(!Interface.intilized)
        {
            Interface.intilize(Interface.gridArray, Interface.dim,i,j);
            Interface.intilized=true;
            System.out.println("intilized");
        }
        if(isFlagged)
        return;
        if(isMined)
        {
            this.setIcon(icons.get("TileExploded"));
            repaint();
            Interface.gameOver(false);
        }
        else
        {
                System.out.println("trying to reveal "+i+ " and "+ j);
                if(!isRevealed)
                playAudio("click");
                Interface.reveal(i,j);
                if(Interface.isWin())
                Interface.gameOver(true);
        }
        }
        else if(SwingUtilities.isRightMouseButton(arg0))
        {
            if(!isRevealed)
            this.setFlagged(!isFlagged);
        }
        
    }
    @Override
    public void mouseEntered(MouseEvent arg0) {
       
    }
    @Override
    public void mouseExited(MouseEvent arg0) {
       
    }
    @Override
    public void mouseReleased(MouseEvent arg0) {
       
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }
    public static void playAudio(String audioName)
    {
        try {
            if(clip.isOpen())
            clip.close();
            clip.open(AudioSystem.getAudioInputStream(audio.get(audioName)));
            clip.start();
            System.out.println("clip started");
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            System.out.println("EXceptiom");
        }
    }
}
