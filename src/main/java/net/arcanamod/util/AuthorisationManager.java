package net.arcanamod.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Patreon, Developer and Contributor authorisation.
 * This class ask external server of player is Patreon, Developer and Contributor.
 */
public class AuthorisationManager {
	public UserPermissionLevel[] cache;

	public enum UserPermissionLevel{
		DEVELOPER,
		CONTRIBUTOR,
		PATREON_ONE,
		PLAYER
	}

	public UserPermissionLevel[] getUserLevel(String username) throws IOException {
		if (cache == null) cache = getUserLevelNoCached(username);;
		return cache;
	}

	private UserPermissionLevel[] getUserLevelNoCached(String username) throws IOException {
			URL url = new URL("https://dczippl.tk/arcana.php");
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = IOUtils.toString(in, encoding);
			String[] users = body.replace("<pre>", "")
					.replace("</pre>", "").split("\u0005");
			for (String user : users) {
				// TODO: Make it more precise.
				if (user.contains(username)) {
					UserPermissionLevel[] upr = new UserPermissionLevel[1];
					String ui = user.split("\u0004")[1];
					upr[0] = ui.equals("1")
							? UserPermissionLevel.DEVELOPER : ui.equals("2")
							? UserPermissionLevel.CONTRIBUTOR : ui.equals("3")
							? UserPermissionLevel.PATREON_ONE : UserPermissionLevel.PLAYER;
					return upr;
				}
			}
			return new UserPermissionLevel[0];
	}
}