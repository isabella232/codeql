import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class ZipTest {
  public void m1(ZipEntry entry, File dir) {
    String name = entry.getName();
    File file = new File(dir, name);
    FileOutputStream os = new FileOutputStream(file); // ZipSlip
    RandomAccessFile raf = new RandomAccessFile(file, "rw"); // ZipSlip
    FileWriter fw = new FileWriter(file); // ZipSlip
  }

  public void m2(ZipEntry entry, File dir) {
    String name = entry.getName();
    File file = new File(dir, name);
    File canFile = file.getCanonicalFile();
    String canDir = dir.getCanonicalPath();
    if (!canFile.toPath().startsWith(canDir))
      throw new Exception();
    FileOutputStream os = new FileOutputStream(file); // OK
  }

  public void m3(ZipEntry entry, File dir) {
    String name = entry.getName();
    File file = new File(dir, name);
    if (!file.toPath().normalize().startsWith(dir.toPath()))
      throw new Exception();
    FileOutputStream os = new FileOutputStream(file); // OK
  }

  private void validate(File tgtdir, File file) {
    File canFile = file.getCanonicalFile();
    if (!canFile.toPath().startsWith(tgtdir.toPath()))
      throw new Exception();
  }

  public void m4(ZipEntry entry, File dir) {
    String name = entry.getName();
    File file = new File(dir, name);
    validate(dir, file);
    FileOutputStream os = new FileOutputStream(file); // OK
  }

  public void m5(ZipEntry entry, File dir) {
    String name = entry.getName();
    File file = new File(dir, name);
    Path absfile = file.toPath().toAbsolutePath().normalize();
    Path absdir = dir.toPath().toAbsolutePath().normalize();
    if (!absfile.startsWith(absdir))
      throw new Exception();
    FileOutputStream os = new FileOutputStream(file); // OK
  }
}
