import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class EdgeDetector {

    // 這是講義的核心：計算水平方向的變化 (Ix)
    public BufferedImage findHorizontalEdges(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();
        // 建立一張新的空白圖片來存結果
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 用迴圈走過每一個像素 (避開最外圈避免座標出錯)
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                
                // 1. 取得左邊像素與右邊像素的亮度
                int leftGray = getGrayscale(input.getRGB(x - 1, y));
                int rightGray = getGrayscale(input.getRGB(x + 1, y));

                // 2. 套用講義公式：Ix ≈ (f(x+1, y) - f(x-1, y)) / 2
                int gradientX = (rightGray - leftGray) / 2;

                // 3. 為了讓肉眼看得到邊緣，我們加上 128 (中性灰)
                // 這樣：變亮的地方會大於 128，變暗的地方會小於 128
                int edgeVal = Math.min(255, Math.max(0, gradientX + 128));
                
                // 將計算結果填入新圖片
                output.setRGB(x, y, new Color(edgeVal, edgeVal, edgeVal).getRGB());
            }
        }
        return output;
    }

    // 輔助工具：把 RGB 轉成灰階亮度
    private int getGrayscale(int rgb) {
        Color c = new Color(rgb);
        return (c.getRed() + c.getGreen() + c.getBlue()) / 3;
    }

    // 程式啟動點
    public static void main(String[] args) {
        EdgeDetector detector = new EdgeDetector();
        try {
            // 讀取檔案
            BufferedImage input = ImageIO.read(new File("input.jpg"));
            
            // 執行運算
            BufferedImage result = detector.findHorizontalEdges(input);

            // 儲存結果
            ImageIO.write(result, "jpg", new File("output_edge.jpg"));
            
            System.out.println("執行成功！請查看資料夾中的 output_edge.jpg");
        } catch (IOException e) {
            System.out.println("錯誤：找不到 input.jpg，請確認檔案放在一起。");
        }
    }
}