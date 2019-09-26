package com.agilecrm;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.glassfish.jersey.filter.LoggingFilter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SuppressWarnings("Duplicates")
public class CreateExcelDemoProd {

    private static final String url = "x";
    private static final String apiKey = "x";
    private static final String userMail = "x";
    private static HSSFWorkbook workbook = new HSSFWorkbook();



    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private static String convertEpochToDateString(Integer unixSeconds){
        // convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds*1000L);
        // the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }


    public static void main(String[] args) throws IOException {

        GUIInterface guiInterface = new GUIInterface();
        guiInterface.runInterfaceGUI();

    }


    public static void generateFile(LocalDate localDateStart, LocalDate localDateEnd) throws IOException, ParseException {

        workbook = null;
        workbook = new HSSFWorkbook();

        final String strDate1 = localDateStart.toString();
        Long millis1 = new SimpleDateFormat("yyyy-MM-dd").parse(strDate1).getTime();
        final String strDate2 = localDateEnd.toString();
        Long millis2 = new SimpleDateFormat("yyyy-MM-dd").parse(strDate2).getTime();
        Long startL = millis1/1000;
        Long endL = millis2/1000;
        String startChoose = startL.toString();
        String endChoose = endL.toString();

        System.out.println(startChoose);
        System.out.println(endChoose);





        //-----------Partie CONFIG EXCEL--------
        //Creation d'une sheet et d'une methode pour EXCEL
        HSSFSheet sheet = workbook.createSheet("Evenements sheet");

        int rownum = 0;
        Cell cell;
        Row row;

        HSSFCellStyle style = createStyleForTitle(workbook);
        HSSFCellStyle cellStyle = null;

        row = sheet.createRow(rownum);

        //creation des entêtes de colonnes
        // Evenement
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Évènement");
        cell.setCellStyle(style);
        // Proprietaire
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Propriétaire");
        cell.setCellStyle(style);
        // Date_Start
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Date_Start");
        cell.setCellStyle(style);
        // Date_End
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Date_End");
        cell.setCellStyle(style);
        // com.agilecrm.Event Start ?
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Start ?");
        cell.setCellStyle(style);

        //--------------Partie CONFIG API--------------
        ClientConfig config = new ClientConfig();
        config.register(new LoggingFilter());
        Client client = ClientBuilder.newClient(config);

        String entityEvents = client
                .target(url)
                .register(new HttpBasicAuthFilter(userMail, apiKey))
                .path("/events")
                .queryParam("start", startChoose)
                .queryParam("end", endChoose)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        System.out.println(entityEvents);


        JSONArray jsonArray = new JSONArray(entityEvents);
        List<Event> events = new ArrayList<Event>();


        for(int i =0; i< jsonArray.length(); i++)
            if (jsonArray.get(i) instanceof JSONObject) {

                JSONObject jsnObj = (JSONObject) jsonArray.get(i);
                ObjectMapper mapper = new ObjectMapper();





                String title = (String) jsnObj.get("title");
                Integer createTime = (Integer) jsnObj.get("created_time");
                Integer start = (Integer) jsnObj.get("start");
                Integer end = (Integer) jsnObj.get("end");
                Boolean isEventStarred = (Boolean) jsnObj.get("is_event_starred");

                Event event = new Event();
                event.setTitle(title);
                event.setCreated_time(createTime);
                event.setStart(start);
                event.setStart(end);
                event.setIs_event_starred(isEventStarred);

                if (jsnObj.has("owner")){
                    Owner12 owner1 = mapper.readValue(String.valueOf(jsnObj.get("owner")), Owner12.class);
                    event.setOwners(owner1);
                }

                events.add(event);
            }



        // Data
        for (Event event : events) {
            rownum++;
            row = sheet.createRow(rownum);
            // Evenement (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(event.getTitle());
            sheet.setColumnWidth((short)5,(short)(5*256));
            cellStyle = workbook.createCellStyle();
            cell.setCellStyle(cellStyle);

            // Propritaire (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(event.getOwners().getName());
            // Date_Start (C)
            cell = row.createCell(2, CellType.NUMERIC);
            String dateStartString = convertEpochToDateString(event.getStart());
            cell.setCellValue(dateStartString);
            // Grade (D)
                /*cell = row.createCell(3, CellType.NUMERIC);
                cell.setCellValue(emp.getGrade());
                // Bonus (E)
                String formula = "0.1*C" + (rownum + 1) + "*D" + (rownum + 1);
                cell = row.createCell(4, CellType.FORMULA);
                cell.setCellFormula(formula);*/
        }

        try {
            saveExcel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }



    public static void saveExcel() throws FileNotFoundException {
        File file = new File("C:/demo/evenements_agile.xls");
        file.getParentFile().mkdirs();
        FileOutputStream outFile = new FileOutputStream(file);
        try {
            workbook.write(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created file: " + file.getAbsolutePath());
    }

}