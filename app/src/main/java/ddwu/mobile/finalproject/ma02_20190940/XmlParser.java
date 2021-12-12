package ddwu.mobile.finalproject.ma02_20190940;

import android.util.Log;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class XmlParser {

    private static final String TAG = "networkManager";

    public enum TagType { NONE, NAME, CONO, SEARCH, WSP, WSU, WA, WW, SOIL, FRTLZR, PRPGT, GRWTVE, WINTER_LTP};

    final static String FAULT_RESULT = "faultResult";
    final static String TAG_ITEM = "item";
    final static String TAG_CONO = "cntntsNo";
    final static String TAG_NAME = "cntntsSj";
    final static String TAG_SEARCH = "sText";
    final static String TAG_WSP = "watercycleSprngCodeNm";
    final static String TAG_WSU = "watercycleSummerCodeNm";
    final static String TAG_WA = "watercycleAutumnCodeNm";
    final static String TAG_WW = "watercycleWinterCodeNm";
    final static String TAG_SOIL = "soilInfo";
    final static String TAG_FRTLZR = "frtlzrInfo";
    final static String TAG_PRPGT = "prpgtEraInfo";
    final static String TAG_GRWT = "grwtveCodeNm";
    final static String TAG_WINTER_LW_TP = "winterLwetTpCodeNm";

    private XmlPullParser parser;

    public XmlParser() {
        XmlPullParserFactory factory = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public DetailDTO detail_parse(String xml) {
        DetailDTO dto = new DetailDTO();
        //String resultDetail = "";
        TagType tagType = TagType.NONE;

        try {
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equals(TAG_WSP)) {
                            tagType = TagType.WSP;
                        } else if (tag.equals(TAG_WSU)) {
                            tagType = TagType.WSU;
                        } else if (tag.equals(TAG_WA)) {
                            tagType = TagType.WA;
                        } else if (tag.equals(TAG_WW)) {
                            tagType = TagType.WW;
                        } else if (tag.equals(TAG_SOIL)) {
                            tagType = TagType.SOIL;
                        } else if (tag.equals(TAG_FRTLZR)) {
                            tagType = TagType.FRTLZR;
                        } else if (tag.equals(TAG_PRPGT)) {
                            tagType = TagType.PRPGT;
                        } else if (tag.equals(TAG_GRWT)) {
                            tagType = TagType.GRWTVE;
                        } else if (tag.equals(TAG_WINTER_LW_TP)) {
                            tagType = TagType.WINTER_LTP;
                        } else if (tag.equals(FAULT_RESULT)) {
                            return null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case WSP:
                                dto.setWsp(parser.getText());
                                break;
                            case WSU:
                                dto.setWsu(parser.getText());
                                break;
                            case WA:
                                dto.setWa(parser.getText());
                                break;
                            case WW:
                                dto.setWw(parser.getText());
                                break;
                            case SOIL:
                                dto.setSoil("토양: " + parser.getText());
                                break;
                            case FRTLZR:
                                dto.setFrtlzr("비료: " + parser.getText());
                                break;
                            case PRPGT:
                                dto.setPrpgt("번식시기: " + parser.getText());
                                break;
                            case GRWTVE:
                                dto.setGrwt("생장속도: " + parser.getText());
                                break;
                            case WINTER_LTP:
                                dto.setWinterLwTp("겨울최저온도: " + parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    public ArrayList<PlantDTO> parse(String xml) {
        ArrayList<PlantDTO> resultList = new ArrayList<>();
        PlantDTO dto = null;
        TagType tagType = TagType.NONE;
        String s_text = "";
        try {
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equals(TAG_ITEM)) {
                            dto = new PlantDTO();
                        } else if (tag.equals(TAG_NAME)) {
                            if (dto != null) {
                                tagType = TagType.NAME;
                            }
                        } else if (tag.equals(TAG_CONO)) {
                            if (dto != null) {
                                tagType = TagType.CONO;
                            }
                        } else if (tag.equals(FAULT_RESULT)) {
                            return null;
                        } else if (tag.equals(TAG_SEARCH)) {
                            tagType = TagType.SEARCH;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (dto != null) {
                            if (parser.getName().equals(TAG_ITEM)) {
                                resultList.add(dto);
                                dto = null;
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case SEARCH:
                                s_text = parser.getText();
                                break;
                            case NAME:
                                if (!s_text.equals("") && parser.getText().contains(s_text)) {
                                    dto.setName(parser.getText());
                                } else if (s_text.equals("")) {
                                    dto.setName(parser.getText());
                                } else {
                                    dto = null;
                                }
                                break;
                            case CONO:
                                dto.setCono(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return resultList;
    }
}
