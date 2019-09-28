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
import static java.util.stream.Collectors.joining;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.json.JsonArray;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        //SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

        // give a timezone reference for formatting (see comment at the bottom)
        //sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
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
        // Temps de travail (en heures)
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Temps travail");
        cell.setCellStyle(style);
        // E-mail
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("e-mail du propriétaire");
        cell.setCellStyle(style);
        // Phone
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("téléphone du propriétaire");
        cell.setCellStyle(style);
        // Deals
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Affaires liées");
        cell.setCellStyle(style);
        // Date_Created
        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("Date Création");
        cell.setCellStyle(style);
        // Description
        cell = row.createCell(9, CellType.STRING);
        cell.setCellValue("Description");
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

                List<String> dealNames = new ArrayList<>();


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
                event.setEnd(end);
                event.setIs_event_starred(isEventStarred);
                if (jsnObj.has("description")) {
                    String description = (String) jsnObj.get("description");
                    event.setDescription(description);
                }

                if (jsnObj.has("owner")){
                    Owner12 owner1 = mapper.readValue(String.valueOf(jsnObj.get("owner")), Owner12.class);
                    event.setOwners(owner1);
                }


                //String dealName = jsnObj.getJSONArray("deals").getString(0);
                //System.out.println(dealName);
                //String testD = ((JSONObject) jsonArray.get(i)).getString("deals");
                //System.out.println(((JSONObject) jsonArray.get(i)).getJSONArray("deals"));

                JSONArray testK = ((JSONObject) jsonArray.get(i)).getJSONArray("deals");
                //System.out.println(testK.length());

                for (int j = 0; j < testK.length(); j++) {
                    if (testK.get(j) instanceof JSONObject) {
                        JSONObject jsnObjK = (JSONObject) testK.get(j);
                        if (jsnObjK.has("name")) {
                            String nameDEAL = (String) jsnObjK.get("name");
                            System.out.println(nameDEAL);
                            dealNames.add(nameDEAL);
                        }
                    }
                }

                event.setDealName(dealNames);

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

            // Date_End (D)
            cell = row.createCell(3, CellType.NUMERIC);
            String dateEndString = convertEpochToDateString(event.getEnd());
            cell.setCellValue(dateEndString);
                /*cell = row.createCell(3, CellType.NUMERIC);
                cell.setCellValue(emp.getGrade());*/

            // Temps_travail (E)
            cell = row.createCell(4, CellType.NUMERIC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDateTime startDate = LocalDateTime.parse(dateStartString, formatter);
            LocalDateTime endDate =LocalDateTime.parse(dateEndString, formatter) ;

            long timeWorkMinutes = ChronoUnit.MINUTES.between(startDate, endDate);
            System.out.println("minutes = " + timeWorkMinutes);
            double timeWorkHours = (double)timeWorkMinutes/60;
            System.out.println("hours = " + timeWorkHours);

            cell.setCellValue(timeWorkHours);


            // E-mail proprio (F)
            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue(event.getOwners().getEmail());

            // Telephone proprio (G)
            cell = row.createCell(6, CellType.NUMERIC);
            cell.setCellValue(event.getOwners().getPhone());


            //Deals
            cell = row.createCell(7, CellType.STRING);
            final String joined = event.getDealsName().stream()
                    .collect(joining(" "));
            cell.setCellValue(joined);


            // Date création (H)
            cell = row.createCell(8, CellType.NUMERIC);
            String createdTimeString = convertEpochToDateString(event.getCreated_time());
            cell.setCellValue(createdTimeString);


            // Description (H)
            cell = row.createCell(9, CellType.NUMERIC);
            cell.setCellValue(event.getDescription());

                /*String formula = "0.1*C" + (rownum + 1) + "*D" + (rownum + 1);
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
        //File file = new File("C:/demo/evenements_agile.xls");
        File file = new File("./evenements_agile.xls");
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
