package com.onevour.sdk.impl.modules.bluetooth.services.v1;

import java.io.IOException;
import java.io.OutputStream;

public class PrinterWriter {

    private final String TAG = PrinterWriter.class.getSimpleName();

    private OutputStream outputStream;

    public PrinterWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeText(String text) throws IOException {
        outputStream.write(text.getBytes());
    }

    // sample
    public void createSample() {
        try {
            writeText("3.000 10 PCS 30.000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
