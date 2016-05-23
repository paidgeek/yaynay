package com.moybl.yaynay;

import com.google.api.client.util.DateTime;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class Util {

	private static PrettyTime prettyTime;

	static {
		prettyTime = new PrettyTime();
	}

	public static String formatPrettyTime(DateTime dateTime) {
		return prettyTime.format(new Date(dateTime.getValue()));
	}

}
