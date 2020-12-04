/**
 *
 */
package de.immomio.mail;

/**
 * @author Johannes Hiemer.
 */
public class ReceivedEmail {

    private String html;

    private String plain;

    private String xml;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getPlain() {
        return plain;
    }

    public void setPlain(String plain) {
        this.plain = plain;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
