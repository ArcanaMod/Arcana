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
		PATREON_ONE
	}

	public UserPermissionLevel[] getUserLevel(String username) throws IOException {
		if (cache == null) {
			URL url = new URL("https://dczippl.tk/arcana.php");
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = IOUtils.toString(in, encoding);
			String[] users = body.replace("<pre>", "").replace("</pre>", "").split("��c");
			for (String user : users) {
				// TODO: Make it more precise.
				if (user.contains(username)) {
					UserPermissionLevel[] upr = new UserPermissionLevel[1];
					upr[0] = UserPermissionLevel.DEVELOPER;
					cache = upr;
					return upr;
				}
			}
			return new UserPermissionLevel[0];
		} else return cache;
	}
}