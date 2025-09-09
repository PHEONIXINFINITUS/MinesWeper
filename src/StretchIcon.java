import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;

public class StretchIcon extends ImageIcon {
    private static final long serialVersionUID = 1L;
    private boolean proportionate = true;

    public StretchIcon(Image image, boolean proportionate) {
        super(image);
        this.proportionate = proportionate;
    }

    public StretchIcon(String filename, boolean proportionate) {
        super(filename);
        this.proportionate = proportionate;
    }

    @Override
public void paintIcon(Component c, Graphics g, int x, int y) {
    Image image = getImage();
    if (image == null) return;

    int compWidth = c.getWidth();
    int compHeight = c.getHeight();

    int drawWidth = compWidth;
    int drawHeight = compHeight;

    if (proportionate) {
        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);
        if (imgWidth > 0 && imgHeight > 0) {
            double aspectRatio = (double) imgWidth / imgHeight;

            if ((double) compWidth / compHeight > aspectRatio) {
                // Too wide → scale based on height
                drawHeight = compHeight;
                drawWidth = (int) (compHeight * aspectRatio);
            } else {
                // Too tall → scale based on width
                drawWidth = compWidth;
                drawHeight = (int) (compWidth / aspectRatio);
            }
        }
    }

    // Center the image inside the component
    int xPos = (compWidth - drawWidth) / 2;
    int yPos = (compHeight - drawHeight) / 2;

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(image, xPos, yPos, drawWidth, drawHeight, null);
    g2.dispose();
}

}   