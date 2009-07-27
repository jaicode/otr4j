package net.java.otr4j.message;

import java.util.Vector;



public final class QueryMessage extends QueryMessageBase {

	public String toString() {
		String txt = MessageConstants.BASE_HEAD + "?";

		for (int version : getVersions()) {
			txt += version;
		}

		txt += "?";
		return txt;
	}

	public QueryMessage(Vector<Integer> versions) {
		super(MessageConstants.QUERY);
		this.setVersions(versions);
	}

	public QueryMessage(String msgText) {
		super(MessageConstants.QUERY);
		if (!msgText.startsWith(MessageConstants.QUERY1_HEAD)
				&& !msgText.startsWith(MessageConstants.QUERY2_HEAD))
			return;

		msgText = msgText.substring(MessageConstants.BASE_HEAD.length());
		char[] chars = msgText.toCharArray();
		Vector<Integer> versions = new Vector<Integer>();
		Boolean stop = false;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			switch (c) {
			case 'V':
			case 'v':
				// Ignore, signifies version numbers will follow.
				break;
			case '?':
				if (i == 0) {
					// Signifies Version 1.
					versions.add(1);
					if (chars.length == 1)
						stop = true;
				} else {
					// Signifies end of version description
					stop = true;
				}
				break;
			default:
				// Control chars are v and ?, everything else should be version
				// descriptors, but no character versions exists, so skip the
				// evil character.
				try {
					versions.add(Integer.parseInt(String.valueOf(c)));
				} catch (NumberFormatException ex) {
					continue;
				}
			}

			if (stop)
				break;
		}

		this.setVersions(versions);
	}

}
