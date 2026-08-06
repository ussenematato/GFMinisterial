package util;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

/**
 * Utilitário para carregar recursos (imagens, etc) tanto em desenvolvimento
 * quanto quando empacotado em JAR/Instalador.
 * Usa ClassLoader para procurar recursos no classpath.
 */
public class ResourceLoader {
    
    /**
     * Carrega uma imagem como BufferedImage a partir do classpath.
     * 
     * @param resourcePath Caminho do recurso no classpath (ex: "/logos/Logo.jpeg")
     * @return BufferedImage carregada ou null se não encontrada
     */
    public static BufferedImage loadImage(String resourcePath) {
        try {
            // Garantir que o caminho começa com /
            if (!resourcePath.startsWith("/")) {
                resourcePath = "/" + resourcePath;
            }
            
            // Tentar carregar usando ClassLoader (funciona em JAR e desenvolvimento)
            InputStream inputStream = ResourceLoader.class.getResourceAsStream(resourcePath);
            
            if (inputStream != null) {
                return ImageIO.read(inputStream);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem do classpath: " + resourcePath);
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Carrega uma imagem e a redimensiona.
     * 
     * @param resourcePath Caminho do recurso no classpath
     * @param width Largura desejada
     * @param height Altura desejada
     * @return ImageIcon redimensionada ou null se não encontrada
     */
    public static ImageIcon loadAndScaleImage(String resourcePath, int width, int height) {
        BufferedImage img = loadImage(resourcePath);
        
        if (img != null) {
            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        
        return null;
    }
    
    /**
     * Obtém a URL de um recurso no classpath.
     * 
     * @param resourcePath Caminho do recurso no classpath
     * @return URL do recurso ou null se não encontrada
     */
    public static URL getResourceURL(String resourcePath) {
        if (!resourcePath.startsWith("/")) {
            resourcePath = "/" + resourcePath;
        }
        return ResourceLoader.class.getResource(resourcePath);
    }
}
