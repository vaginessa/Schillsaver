package handler;


import controller.MainScreenController;
import misc.Logger;

import java.io.File;
import java.util.Formatter;
import java.util.Locale;

public class ArchiveHandler {
    /**
     * Compresses the specified handler while outputting the command-line
     * results to the screen.
     *
     * The resulting archive will bear the name of the input handler.
     * If the input handler is test.jpg, then the output archive will be test.jpg.7z
     * as an example.
     * @param outputDirectory The directory in which to place the output file(s).
     * @param selectedFile The handler to compress.
     * @param controller The controller for the view in which the output text area resides.
     * @param configHandler The object that handles settings for encoding, decoding, compression, and a number of other features.
     * @return The compressed archive.
     */
    public File packFile(final String outputDirectory, final File selectedFile, final MainScreenController controller, final ConfigHandler configHandler) {
        // Basic command settings ripped from http://superuser.com/a/742034
        final StringBuilder stringBuilder = new StringBuilder();
        final Formatter formatter = new Formatter(stringBuilder, Locale.US);

        formatter.format("\"%s\" %s \"%s.%s\" \"%s\"",
                        configHandler.getCompressionProgramPath(),
                        configHandler.getCompressionCommands(),
                        selectedFile.getAbsolutePath(),
                        configHandler.getDecodeFormat(),
                        outputDirectory);

        controller.getView().getTextArea_output().appendText(stringBuilder.toString() + System.lineSeparator() + System.lineSeparator() + System.lineSeparator());

        CommandHandler.runProgram(stringBuilder.toString(), controller);

        // Return a File poingint to the newly created archive:
        final File file = new File(selectedFile.getAbsoluteFile() + "." + configHandler.getDecodeFormat());


        if(!file.exists()) {
            Logger.writeLog("Could not create " + file.getAbsolutePath() + " shutting down.", Logger.LOG_TYPE_ERROR);
            System.exit(1);
        }
        return file;
    }

    /**
     * Compresses the specified handler(s), while outputting the command-line
     * results to the screen, into a single archive.
     *
     * The resulting archive will bear the specified name.
     * @param outputDirectory The directory in which to place the output file(s).
     * @param selectedFiles The files to compress.
     * @param controller The controller for the view in which the output text area resides.
     * @param configHandler The object that handles settings for encoding, decoding, compression, and a number of other features.
     * @param archiveName The name to give the compressed archive.
     * @return The compressed archive.
     */
    public File packFiles(final String outputDirectory, final File[] selectedFiles, final MainScreenController controller,  final ConfigHandler configHandler, final String archiveName) {
        // Basic command settings ripped from http://superuser.com/a/742034
        final StringBuilder stringBuilder = new StringBuilder();
        final Formatter formatter = new Formatter(stringBuilder, Locale.US);

        formatter.format("\"%s\" %s \"%s.%s\"",
                        configHandler.getCompressionProgramPath(),
                        configHandler.getCompressionCommands(),
                        archiveName,
                        outputDirectory);

        for(final File f : selectedFiles) {
            // todo If it's possible to do the below to lines with the formatter, then do so.
            stringBuilder.append(" ");
            stringBuilder.append("\"" + f.getAbsolutePath() + "\"");
        }

        controller.getView().getTextArea_output().appendText(stringBuilder.toString() + System.lineSeparator() + System.lineSeparator() + System.lineSeparator());

        CommandHandler.runProgram(stringBuilder.toString(), controller);

        // Return a File int to the newly created archive:
        final File file = new File(archiveName + "." + configHandler.getDecodeFormat());

        if(!file.exists()) {
            Logger.writeLog("Could not create " + archiveName + " shutting down.", Logger.LOG_TYPE_ERROR);
            System.exit(1);
        }

        return file;
    }
}
