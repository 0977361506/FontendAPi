package com.vnpost.elearning.Beans;



import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ImageUtils {
    public static void compressImage(String inputPath, String outputPath, float level, String typeImage) throws IOException {
        //range of level 0.0 => 1.0
        //recommend level 0.3 ~0.5
        BufferedImage image;
        IIOMetadata metadata;


        try (ImageInputStream in = ImageIO.createImageInputStream(Files.newInputStream(Paths.get(inputPath)))) {
            ImageReader reader = ImageIO.getImageReadersByFormatName(typeImage).next();
            reader.setInput(in, true, false);
            image = reader.read(0);
            metadata = reader.getImageMetadata(0);
            reader.dispose();
        }

        try (ImageOutputStream out = ImageIO.createImageOutputStream(Files.newOutputStream(Paths.get(outputPath)))) {
            ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(image);
            ImageWriter writer = ImageIO.getImageWriters(type, typeImage).next();

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(level);
            }

            writer.setOutput(out);
            writer.write(null, new IIOImage(image, null, metadata), param);
            writer.dispose();
        }
    }


}
    

