package codes.meruhz.multilang.api.main;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Paths;

public final class Multilang {

    private static @NotNull File storagesDirectory = new File(Paths.get(System.getProperty("user.dir")).toAbsolutePath() + File.separator + "languages" + File.separator);

    private Multilang() {
        throw new UnsupportedOperationException("cannot instantiate this");
    }

    public static @NotNull File getStoragesDirectory() {
        return storagesDirectory;
    }

    public static synchronized void setStoragesDirectory(@NotNull File storagesDirectory) {
        Multilang.storagesDirectory = storagesDirectory;
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
