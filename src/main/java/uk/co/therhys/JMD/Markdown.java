package uk.co.therhys.JMD;

import uk.co.therhys.MD.Converter;

public class Markdown {
    private final String markdown;
    private final static Converter converter = new Converter();

    public Markdown(String markdown){
        this.markdown = markdown;
    }

    public String toHTML(){
        String md = converter.md2html(markdown);

        return md.substring(md.indexOf("<html>"));
    }
}
