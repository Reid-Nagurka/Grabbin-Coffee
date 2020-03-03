package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

/**
 * A VERY simple class to return a mail-link string that will work with SendGrid :)
 */
public class MailLinkCreator {
    private String senderName;
    private String recipientName;
    private String senderEmail;
    private String recipientEmail;
    private String eventDateOnly;
    private String eventTimeOnly;

    /**
     * Creates the MailLinkCreator class!
     * @param senderName the name of the person SENDING the email
     * @param recipientName the name of the person RECEIVING the email
     * @param senderEmail the EMAIL of the person SENDING the email
     * @param recipientEmail the EMAIL of the person RECEIVING the email
     * @param eventDateOnly the DATE STRING of when the meeting is occurring
     * @param eventTimeOnly the TIME STRING of when the meeting is occurring
     */
    public MailLinkCreator(String senderName, String recipientName, String senderEmail, String recipientEmail, String eventDateOnly, String eventTimeOnly) {
        this.senderName = changeSpaceCharacters(senderName);
        this.recipientName = changeSpaceCharacters(recipientName);
        this.senderEmail = changeSpaceCharacters(senderEmail);
        this.recipientEmail = changeSpaceCharacters(recipientEmail);
        this.eventDateOnly = changeSpaceCharacters(eventDateOnly);
        this.eventTimeOnly = changeSpaceCharacters(eventTimeOnly);
    }

    private String changeSpaceCharacters(String content){
        int length = content.length();
        char [] chars = content.toCharArray();

        int spaceCount = 0;
        for (int i = 0; i < length; i++) {
            if (chars[i] == ' ') {
                spaceCount++;
            }
        }
        int newLength = length + 2 * spaceCount;
        char [] charsNew = new char [newLength];
        for (int i = length - 1; i >= 0; i--) {
            if (chars[i] == ' ') {
                charsNew[newLength - 1] = '0';
                charsNew[newLength - 2] = '2';
                charsNew[newLength - 3] = '%';
                newLength = newLength - 3;
            } else {
                charsNew[newLength - 1] = chars[i];
                newLength = newLength - 1;
            }
        }
        return new String(charsNew);
    }

    public void setEventDateOnly(String eventDateOnly) {
        this.eventDateOnly = eventDateOnly;
    }

    public void setEventTimeOnly(String eventTimeOnly) {
        this.eventTimeOnly = eventTimeOnly;
    }

    public String getMailLink(){
        String start = "mailto:"+this.recipientEmail+"?subject=I%20would%20love%20to%20grab%20coffee!";
        String body = "&body=Hi%20"+this.recipientName+"%2C%0D%0A%0D%0AI%20would%20love%20to%20get%20coffee%20on%20"+this.eventDateOnly+"%20at%20"+this.eventTimeOnly+"!%20The%20location%20works%20for%20me%20as%20well.%0D%0A%0D%0ALooking%20forward%20to%20seeing%20you%2C%0D%0A"+this.senderName;
        String test = changeSpaceCharacters(start+body);

        return changeSpaceCharacters(test);
    }

    public String getDeclineLink(){
        String start = "mailto:"+this.recipientEmail+"?subject=I%20cannot%20meet%20this%20time!";
        String body = "&body=Hi%2C%20"+this.recipientName+"%2C%0D%0A%0D%0AI%20have%20to%20offer%20an%20apology!%20My%20schedule%20does%20not%20allow%20me%20to%20meet%20this%20coming%20week.%20Perhaps%20some%20time%20in%20the%20future%3F%0D%0A%0D%0AHave%20a%20good%20one%2C%0D%0A"+this.senderName;
        return changeSpaceCharacters(start+body);
    }
}
