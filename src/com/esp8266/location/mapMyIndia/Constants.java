package com.esp8266.location.mapMyIndia;

import java.util.ArrayList;
import java.util.List;

import com.esp8266.location.LatLang;

public class Constants {
public static final LatLang home55 = new LatLang(30.6515854,76.8788200 , "Home 55/27");
public static final LatLang home1273 = new LatLang(30.667582,76.885926 , "Home 1273/25");
public static final LatLang home1157 = new LatLang(30.667010,76.8856068 , "Home 1157/25");
public static final LatLang chcraipurani = new LatLang(30.5835968951446,77.0256654918194 , "CHC raipurani");
public static final LatLang infosys = new LatLang(30.7258348,76.8460987 , "Infosys");
public static final LatLang phcbarwala = new LatLang(30.557332,76.936412 , "PHC Barwala");
public static final LatLang phcKot = new LatLang(30.623992769324,76.9440566003323 , "PHC Kot");
public static final LatLang sector6GH = new LatLang(30.7047881,76.8536179 , "Sector 6 GH");
public static final LatLang redBishop = new LatLang(30.7062122037605,76.8652345240116 , "Red Bishop");
public static final LatLang gurukul = new LatLang(30.6671466,76.86251 , "The Gurukul Sec 20");

public static  List<LatLang> kusumGeoFencing = new ArrayList<LatLang>();
static {
	kusumGeoFencing.add(home55);
	kusumGeoFencing.add(home1157);
	kusumGeoFencing.add(sector6GH);
	kusumGeoFencing.add(redBishop);
	kusumGeoFencing.add(phcbarwala);
	kusumGeoFencing.add(chcraipurani);
}

}
