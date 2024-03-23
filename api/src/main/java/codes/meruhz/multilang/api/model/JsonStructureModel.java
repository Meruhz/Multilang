package codes.meruhz.multilang.api.model;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class JsonStructureModel {

    private final @NotNull File folder, file;
    private final @NotNull String name;

    private @NotNull JsonElement structure;

    public JsonStructureModel(@NotNull File folder, @NotNull String name) {
        if(!folder.isDirectory() && !folder.mkdirs()) {
            throw new RuntimeException("cannot create folder: " + folder.getAbsolutePath());
        }

        name = name.replace(" ", "_");
        this.name = name.endsWith(".json") ? name : name + ".json";

        this.folder = folder;
        this.file = new File(this.getFolder(), this.getName());
        this.structure = new JsonObject();

        this.createFile().join();
    }

    public final @NotNull File getFolder() {
        return this.folder;
    }

    public final @NotNull File getFile() {
        return this.file;
    }

    public final @NotNull String getName() {
        return this.name;
    }

    public @NotNull JsonElement getStructure() {
        return this.structure;
    }

    public void setStructure(@NotNull JsonElement structure, boolean autoLoad) {
        synchronized (this) {
            this.structure = structure;
        }

        if(autoLoad) this.load().join();
    }

    public void setStructure(@NotNull JsonElement structure) {
        this.setStructure(structure, false);
    }

    @Blocking
    protected @NotNull CompletableFuture<Boolean> createFile() {
        @NotNull CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                @NotNull File parent = this.getFile().getParentFile();
                if(!parent.exists() && !parent.mkdirs()) {
                    throw new IOException("cannot create file directory: " + parent.getAbsolutePath());
                }

                completableFuture.complete(this.getFile().createNewFile());

            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    @Blocking
    protected @NotNull CompletableFuture<Void> save(@NotNull String format) {
        @NotNull CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        // Run save logic asynchronously
        CompletableFuture.runAsync(() -> {
            try (FileOutputStream fileOutputStream = new FileOutputStream(this.getFile())) {
                @NotNull BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));

                // Write the configuration in the specified format
                synchronized (this) {
                    bufferedWriter.write(format);
                }

                bufferedWriter.close();
                completableFuture.complete(null);

            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    public @NotNull CompletableFuture<Void> load() {
        @NotNull CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {

                if(this.getFile().length() == 0) {
                    this.save(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this.getStructure())).join();

                } else {
                    @NotNull JsonElement structure = JsonParser.parseString(JsonStructureModel.getFileContent(this.getFile()));
                    this.setStructure(structure);

                    if(!this.getStructure().toString().equals(structure.toString())) {
                        this.load().join();
                    }
                }

                completableFuture.complete(null);

            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        JsonStructureModel that = (JsonStructureModel) o;
        return Objects.equals(folder, that.folder) && name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(folder, name);
    }

    public static @NotNull InputStream getFromResources(@NotNull String fileName) {
        fileName = "/" + fileName;

        @Nullable InputStream inputStream = JsonStructureModel.class.getResourceAsStream(fileName);
        if(inputStream == null) {
            throw new NullPointerException("no resources found: " + fileName);
        }

        return inputStream;
    }

    public static @NotNull String getFileContent(@NotNull File file) throws IOException {
        @NotNull FileInputStream fileInputStream = new FileInputStream(file);
        @NotNull BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
        @NotNull StringBuilder content = new StringBuilder();
        @NotNull String line;

        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }

        bufferedReader.close();
        return content.toString();
    }
}
