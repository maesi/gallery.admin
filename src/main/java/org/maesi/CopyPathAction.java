package org.maesi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopyPathAction implements PathAction {

  Logger logger = LoggerFactory.getLogger(CopyPathAction.class);

  private Path destinationDir;

  public CopyPathAction(Path destinationDir) {
    this.destinationDir = destinationDir;
  }

  @Override
  public Path execute(Path image) {
    try {
      Path copiedImage = Paths.get(destinationDir.toString(), image.getFileName().toString());
      Files.copy(image, copiedImage);
      return copiedImage;
    } catch (IOException e) {
      logger.warn("copy problem", e);
      return image;
    }
  }
}
