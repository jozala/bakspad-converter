package pl.aetas.bakspad.converter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.nio.file.FileSystems
import java.nio.file.Path

class NotesConverter {

    private final static Logger LOGGER = LogManager.getLogger();

    private final Path dataDirectory

    NotesConverter(Path dataDirectory) {
        this.dataDirectory = dataDirectory
    }

    def processNotesAsString(Closure processText) {
        dataDirectory.toFile().eachFileRecurse {processFileInPlace(it, processText) }
    }

//    def processNotesAsJsonObjects(Closure processNote) {
//        JsonSlurper jsonSlurper = new JsonSlurper();
//        dataDirectory.each {}
//    }

    private static def processFileInPlace(File file, Closure processText) {
        LOGGER.info("Processing file {}", file.name)
        def text = file.text
        file.write(processText(text))
    }

    static void main(String[] args) {
        Path dataDirectory = FileSystems.getDefault().getPath("data");
        def converter = new NotesConverter(dataDirectory)
        converter.backupDataFiles(FileSystems.getDefault().getPath("data-backup"))
        LOGGER.info("Converting \\\\n to <br /> started")
        converter.processNotesAsString(replaceNewLineWithHtmlBrTag)
        LOGGER.info("Converting \\\\n to <br /> finished")

    }

    def backupDataFiles(Path destinationPath) {
        LOGGER.info("Creating backup from {} to {}",
                dataDirectory.toAbsolutePath().toString(), destinationPath.toAbsolutePath().toString())

        new AntBuilder().copy( todir: destinationPath.toAbsolutePath().toString()) {
            fileset( dir: dataDirectory.toAbsolutePath().toString())
        }

        LOGGER.info("Backup completed")
    }

    static def Closure replaceNewLineWithHtmlBrTag = {
         String text -> text.replaceAll("\\\\n", '<br />')
    }

}