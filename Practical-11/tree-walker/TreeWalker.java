import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TreeWalker {
    public static void main(String[] args) {
        System.out.println("========== Practical 11 - Problem 3: NIO Tree Walker & Report ==========");

        // We can inspect the current project root or a folder
        Path startPath = Path.of(".").toAbsolutePath().normalize();
        System.out.println("Walking folder tree from: " + startPath);

        AtomicInteger fileCount = new AtomicInteger(0);
        AtomicInteger dirCount = new AtomicInteger(0);
        AtomicLong totalSizeBytes = new AtomicLong(0);

        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    // Skip hidden / build / git directories to keep traversal focused
                    String dirName = dir.getFileName() != null ? dir.getFileName().toString() : "";
                    if (dirName.startsWith(".") || dirName.equals("bin") || dirName.equals("target")) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    dirCount.incrementAndGet();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (attrs.isRegularFile()) {
                        fileCount.incrementAndGet();
                        totalSizeBytes.addAndGet(attrs.size());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("--- Traversal Summary ---");
            System.out.println("Total Directories Visited: " + dirCount.get());
            System.out.println("Total Regular Files Found: " + fileCount.get());
            System.out.printf("Total Size: %,d bytes (%.2f KB)\n",
                    totalSizeBytes.get(), totalSizeBytes.get() / 1024.0);

        } catch (IOException e) {
            System.err.println("Tree walk error: " + e.getMessage());
        }
    }
}
