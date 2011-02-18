package com.rusticisoftware.hostedengine.client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class UploadToken
{
    private String server = null;
    private String tokenId = null;
    
    public String getServer(){
        return server;
    }
    
    public void setServer(String value){
        server = value;
    }
    
    public String getTokenId(){
        return tokenId;
    }
    
    public void setTokenId(String value){
        tokenId = value;
    }
    
    public UploadToken (Document xmlDoc){
        NodeList tokenList = xmlDoc.getElementsByTagName("token");
        if (tokenList.getLength() > 0) {
            Element token = (Element)tokenList.item(0);
            Element server = (Element)token.getElementsByTagName("server").item(0);
            Element id = (Element)token.getElementsByTagName("id").item(0);

            this.server = server.getTextContent();
            this.tokenId = id.getTextContent();
        }
    }

}
