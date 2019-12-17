package com.esp8266.location.mapMyIndia;

import java.util.ArrayList;
import java.util.List;

import com.esp8266.location.LatLang;

public class Constants {
public static final LatLang home55 = new LatLang(30.6515854,76.8788200 , "Home 55/27");
public static final LatLang home1273 = new LatLang(30.667582,76.885926 , "Home 1273/25");
public static final LatLang home1157 = new LatLang(30.667010,76.8856068 , "Home 1157/25");
public static final LatLang infosys = new LatLang(30.7258348,76.8460987 , "Infosys");
public static final LatLang phcbarwala = new LatLang(30.557332,76.936412 , "PHC Barwala");
public static final LatLang gurukul = new LatLang(30.6671466,76.86251 , "The Gurukul Sec 20");

public static  List<LatLang> kusumGeoFencing = new ArrayList<LatLang>();
static {
	kusumGeoFencing.add(home55);
	kusumGeoFencing.add(home1157);
	kusumGeoFencing.add(phcbarwala);
}

}
