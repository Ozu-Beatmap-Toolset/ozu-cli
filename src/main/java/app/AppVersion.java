package app;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import util.file.IOFile;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppVersion {

    public static final String VERSION = "1.3.4-alpha";

    public static void main(String[] args) throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        final String modelVersion = model.getVersion();
        final File cliJavaFile = new File("src\\main\\java\\app\\AppVersion.java");
        Optional<InputStream> inputStreamOpt = IOFile.fileToInputStream(cliJavaFile);
        inputStreamOpt.ifPresent(inputStream -> {
            String fileContent;
            try {
                fileContent = new String(inputStream.readAllBytes());
                fileContent = fileContent.replaceAll("VERSION" +
                        ".*;", "VERSION = " +
                        "\"" + modelVersion + "\";");

                IOFile.createFileWithContent(cliJavaFile.getAbsolutePath(), Arrays.stream(fileContent.split("\n")).collect(Collectors.toList()));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
