package org.maesi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.maesi.CopyPathAction;
import org.maesi.PathAction;

public class CopyPathActionTest {

  private List<Path> tempPaths;

  @Before
  public void createTempFilesMap() {
    tempPaths = new ArrayList<>();
  }

  @After
  public void deleteTempFiles() throws IOException {
    for (Path p : tempPaths) {
      Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
          Files.deleteIfExists(dir);
          return super.postVisitDirectory(dir, exc);
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          Files.deleteIfExists(file);
          return super.visitFile(file, attrs);
        }
      });
    }
  }

  @Test
  public void copy() throws Exception {
    Path sourceDir = Files.createTempDirectory(CopyPathActionTest.class.getName().toString() + "_src_");
    tempPaths.add(sourceDir);
    Path destinationDir = Files.createTempDirectory(CopyPathActionTest.class.getName().toString() + "_dest_");
    tempPaths.add(destinationDir);

    Path file = Files.createTempFile(sourceDir, "", ".jpg");
    PathAction testee = new CopyPathAction(destinationDir);
    Path newPath = testee.execute(file);
    String expectedFileLocation =
        destinationDir.toString() + FileSystems.getDefault().getSeparator() + file.getFileName().toString();

    assertEquals(expectedFileLocation, newPath.toString());
    assertTrue(Files.exists(file));
    assertTrue(Files.exists(Paths.get(expectedFileLocation)));
  }
}
