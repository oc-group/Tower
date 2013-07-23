package com.droidplanner.file.IO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.droidplanner.drone.variables.waypoint;
import com.droidplanner.file.FileManager;
import com.droidplanner.file.FileStream;

public class MissionWriter {
	private waypoint home;
	private List<waypoint> waypoints;
	private String name = "";

	public MissionWriter(waypoint home, List<waypoint> waypoints, String name) {
		this.home = home;
		this.waypoints = waypoints;
		this.name = name;
	}

	public MissionWriter(waypoint home, List<waypoint> waypoints) {
		this(home, waypoints, "waypoints");
	}

	public boolean saveWaypoints() {
		try {
			if (!FileManager.isExternalStorageAvaliable()) {
				return false;
			}
			FileOutputStream out = FileStream.getWaypointFileStream(name);

			writeFirstLine(out);

			writeWaypointsLines(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void writeFirstLine(FileOutputStream out) throws IOException {
		out.write(String.format(Locale.ENGLISH,
				"QGC WPL 110\n0\t1\t0\t16\t0\t0\t0\t0\t%f\t%f\t%f\t1\n",
				home.getCoord().latitude, home.getCoord().longitude,
				home.getHeight()).getBytes());

	}

	private void writeWaypointsLines(FileOutputStream out) throws IOException {
		for (int i = 0; i < waypoints.size(); i++) {
			waypoint wp = waypoints.get(i);
			out.write(String
					.format(Locale.ENGLISH,
							"%d\t0\t%d\t%d\t0.000000\t0.000000\t0.000000\t0.000000\t%f\t%f\t%f\t1\n",
							i + 1, wp.getFrame(), wp.getCmd().getType(),
							wp.getCoord().latitude, wp.getCoord().longitude,
							wp.getHeight()).getBytes());
		}
	}
}
