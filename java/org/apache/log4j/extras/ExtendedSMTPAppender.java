package org.apache.log4j.extras;

import java.util.ArrayList;
import javax.mail.Header;
import javax.mail.MessagingException;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.net.SMTPAppender;

/**
 * An SMTPAppender which has additional capabilities.
 *
 * <ul>
 *   <li>Arbitrary headers: <code>log4j.appender.A1.SMTPHeader=Field: value</code>
 * <ul>
 *
 * @author Christopher Schultz
 */
public class ExtendedSMTPAppender
    extends SMTPAppender
{
    private ArrayList<Header> headers;

    /**
     * Add a header to SMTP messages sent by this appender.
     * Multiple headers can be added by calling <code>setSMTPHeader</code>
     * multiple times.
     *
     * @param header The header to add e.g. <code>Auto-Submitted: auto-generated</code>
     */
    public synchronized void setSMTPHeader(String header)
    {
        if(null == headers)
            headers = new ArrayList<Header>();

        int split = header.indexOf(':');
        if(split >= 0)
            headers.add(new Header(header.substring(0, split).trim(),
                                   header.substring(split+1).trim()));
        else
            headers.add(new Header(header.trim(), ""));
    }

    @Override
    public synchronized void activateOptions()
    {
        super.activateOptions();

        if(null != headers)
        {
            try {
                for(Header h : headers)
                    msg.addHeader(h.getName(), h.getValue());
            } catch(MessagingException me) {
                LogLog.error("Could not set SMTP message headers.", me);
            }

            headers = null; // Don't need those any more
        }
    }
}
