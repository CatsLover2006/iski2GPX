package ca.litten.iskiconverter;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws Throwable {
        if (args.length == 0) System.out.println("We need an input file!");
        if (args.length <= 1) {
            System.out.println("We need an output file!");
            return;
        }
        File file = new File(args[0]);
        if (!file.canRead()) {
            System.out.println("We can't read the file...");
            return;
        }
        FileInputStream fileStream = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        fileStream.read(buffer);
        fileStream.close();
        JSONObject object = new JSONObject(new String(buffer, StandardCharsets.UTF_8));
        String data = object.getString("track");
        String[] dataPoints = data.split(" ");
        StringBuilder output = new StringBuilder();
        output.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n\n")
                .append("<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"byHand\" version=\"1.1\"\n")
                .append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n")
                .append("xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">\n")
                .append("<trk>\n<trkseg>\n");
        boolean isInFeet = !object.getBoolean("has_dimension_m");
        for (String dataPoint : dataPoints) {
            // Point format: long, lat, elevation, unix epoch, speed
            String[] point = dataPoint.split(",");
            double elevation = Double.parseDouble(point[2]);
            if (isInFeet) elevation /= 3.281;
            double pointTime = Double.parseDouble(point[3]);
            Instant i = Instant.ofEpochMilli((long) (pointTime * 1000));
            ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
            System.out.println(DateTimeFormatter.ISO_INSTANT.format(z));
            output.append("\n<trkpt lon=\"").append(point[0]).append("\" lat=\"")
                    .append(point[1]).append("\">\n<ele>").append(elevation)
                    .append("</ele>\n<time>")
                    .append(DateTimeFormatter.ISO_INSTANT.format(z))
                    .append("</time>\n</trkpt>\n");
        }
        output.append("\n</trkseg>\n</trk>\n</gpx>\n");
        file = new File(args[1]);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(output.toString().getBytes(StandardCharsets.UTF_8));
        System.out.println();
        System.out.println("Saved to:");
        System.out.println(file);
    }
}
