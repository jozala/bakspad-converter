package pl.aetas.bakspad.converter

import java.nio.file.FileSystems
import java.nio.file.Path

class NotesConverter {

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
        def text = file.text
        file.write(processText(text))
    }

    static main(String[] args) {
        Path dataDirectory = FileSystems.getDefault().getPath("data");
        def converter = new NotesConverter(dataDirectory);
        converter.processNotesAsString(replaceNewLineWithHtmlBrTag)
    }

    static def Closure replaceNewLineWithHtmlBrTag = {
         String text -> text.replaceAll("\\\\n", '<br />')
    }

}