/* -- JFLAP 4.0 --
 *
 * Copyright information:
 *
 * Susan H. Rodger, Thomas Finley
 * Computer Science Department
 * Duke University
 * April 24, 2003
 * Supported by National Science Foundation DUE-9752583.
 *
 * Copyright (c) 2003
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the author.  The name of the author may not be used to
 * endorse or promote products derived from this software without
 * specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package file.xml;

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.*;

import pumping.*;

/**
 * This is the transducer for encoding and decoding 
 * {@link pumping.RegularPumpingLemma} objects.
 * 
 * @author Jinghui Lim
 * @see gui.pumping.PumpingLemmaChooser
 *
 */
public class RegPumpingLemmaTransducer extends PumpingLemmaTransducer 
{
    /**
     * The type of pumping lemma.
     */
    public static String TYPE = "regular pumping lemma";
    /**
     * The tag for the length of <i>x</i>.
     */
    public static String X_NAME = "xLength";
    /**
     * The tag for the length of <i>y</i>.
     */
    public static String Y_NAME = "yLength";
    
    public Serializable fromDOM(Document document) 
    {
        RegularPumpingLemma pl = (RegularPumpingLemma)PumpingLemmaFactory.createPumpingLemma
            (TYPE, document.getElementsByTagName(LEMMA_NAME).item(0).getTextContent());
        
        //Decode m, w, & i.         
        pl.setM(Integer.parseInt(document.getElementsByTagName(M_NAME).item(0).getTextContent()));
        pl.setW(document.getElementsByTagName(W_NAME).item(0).getTextContent());
        pl.setI(Integer.parseInt(document.getElementsByTagName(I_NAME).item(0).getTextContent()));
        
        //Decode the attempts
        NodeList attempts = document.getDocumentElement().getElementsByTagName(ATTEMPT);
        for(int i = 0; i < attempts.getLength(); i++)
            pl.addAttempt(attempts.item(i).getTextContent());
        
        //Decode the first player.        
        pl.setFirstPlayer(document.getElementsByTagName(FIRST_PLAYER).item(0).getTextContent());
        
        //Decode the decomposition.
        int xLength = Integer.parseInt(document.getElementsByTagName(X_NAME).item(0).getTextContent());
        int yLength = Integer.parseInt(document.getElementsByTagName(Y_NAME).item(0).getTextContent());
        pl.setDecomposition(new int[]{xLength, yLength});
        
        return pl;
    }

    public Document toDOM(Serializable structure) 
    {   
        RegularPumpingLemma pl = (RegularPumpingLemma)structure;
        Document doc = newEmptyDocument();
        Element elem = doc.getDocumentElement();        
        elem.appendChild(createElement(doc, LEMMA_NAME, null, pl.getTitle()));
        elem.appendChild(createElement(doc, FIRST_PLAYER, null, pl.getFirstPlayer()));
        elem.appendChild(createElement(doc, M_NAME, null, "" + pl.getM()));
        elem.appendChild(createElement(doc, W_NAME, null, "" + pl.getW()));
        elem.appendChild(createElement(doc, I_NAME, null, "" + pl.getI()));
        elem.appendChild(createElement(doc, X_NAME, null, "" + pl.getX().length()));
        elem.appendChild(createElement(doc, Y_NAME, null, "" + pl.getY().length()));
        
        //Encode the list of attempts.
        ArrayList attempts = pl.getAttempts();
        if(attempts != null && attempts.size() > 0)        
            for(int i = 0; i < attempts.size(); i++)
                elem.appendChild(createElement(doc, ATTEMPT, null, (String)attempts.get(i)));
        
        return doc;
    }

    public String getType() 
    {
        return TYPE;
    }
}
